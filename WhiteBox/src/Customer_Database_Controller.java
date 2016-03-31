import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import java.sql.ResultSetMetaData;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Customer_Database_Controller implements Operations_Display {

    // Database name
    static final String whitebox_db = "whitebox_db"; 
    
    //OperationsEnum Op;
    private StringBuilder stringy = new StringBuilder();
    
    private Statement statement = null;
    private ResultSet resultSet = null;
    Connection connect = null;

    // declared like this since we implement functionality that doesn't modify the database columns
    //final int CINF_num = 7;
    //final int PayINF_num = 12;
    //final int ProINF_num = 27;
    //final int ManINF_num = 30;
    //final int ComINF_num = 33;
    
    // Table names, add more later
    //private String C_INFO = "CINFO";
    //private String Pay_INFO = "PYINFO";
    //private String Pro_INFO = "PRINFO";
    //private String Man_INFO = "MANNAME";
    //private String Com_INFO = "COMP";
    
    // Lists for Filter
    //private LinkedList<String> TableSave;
    //private List<String> ColSave = new ArrayList<String>();
    //private List<String> UserSave = new ArrayList<String>();
    
    protected String Acknowledgement = null;
    
    // For insert
    private List<String> Col_Contents = new ArrayList<String>();
    
    Boolean FirstOp = true;
    
    /*
     * FROM whitebox_db.customer_info AS CINFO
     * JOIN whitebox_db.payment_info AS PYINFO ON CINFO.Build_ID = PYINFO.Build_ID
     * JOIN whitebox_db.product_info AS PRINFO ON CINFO.Build_ID = PRINFO.Build_ID
     * JOIN whitebox_db.manufacturer_table AS MANNAME ON PRINFO.Manufacturer = MANNAME.Manufacturer
     * JOIN whitebox_db.component_type_table AS COMP ON PRINFO.Component_type = COMP.Component_type
     * 
     */
    // instead of determining which tables to link, just link them all.
    private String JOINS = "FROM whitebox_db.customer_info JOIN whitebox_db.payment_info ON customer_info.Build_ID = payment_info.Build_ID JOIN whitebox_db.product_info ON customer_info.Build_ID = product_info.Build_ID JOIN whitebox_db.manufacturer_table ON product_info.Manufacturer = manufacturer_table.Manufacturer JOIN whitebox_db.component_type_table ON product_info.Component_type = component_type_table.Component_type";

    Path filePath;
    BufferedInputStream bufferedInputStream = null;
    FileInputStream  fileInputStream= null;
    
    public Customer_Database_Controller(){
        //this.Op = Op;
        //TableSave = new LinkedList<String> ();
    }
    
    public void CustomerDB_Connect() throws FileNotFoundException{
        
        // XML file
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("./config.xml"); // ./config.xml
        try {
            props.loadFromXML(fis);
        } catch (InvalidPropertiesFormatException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        // keys for connection
        //String host = props.getProperty("hostURL");
        //String host2 = host + ";";
        
        String userName = props.getProperty("Username");
        String password = props.getProperty("Password");
        
        //hard coded for now
        String host = "jdbc:mysql://localhost:3306/whitebox_db?autoReconnect=true&useSSL=false";
        /*
        try {
           
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("Connection to Customer Database failed, Unable to load driver class");
            
        }
        */
        
        try {
            connect = DriverManager.getConnection(host, userName, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            Acknowledgement = "Cannot connect to the database!" + e;
            throw new IllegalStateException("Cannot connect to the database!", e);
        }
        
    }

    
    public void SelectOperation(int Operation){
        
        if(FirstOp){
            
            try {
                CustomerDB_Connect();
                
            } catch (IOException e) {
                // TODO Auto-generated catch bl         ock
                e.printStackTrace();
            }
            FirstOp = false;
        }
        
        // empty string
        stringy.setLength(0);
        
        // if 1, upload file
        // Operations Display calls "UploadFileDirectory
        
        // if 2, Operations Display calls "filter
        
        /*switch(Op){
        
            case UPLOAD_FILE:
                // partly set up sql to execute
                //stringy.append("");
                break;
                
            case VIEW_FILTER:
                // partly set up sql to execute
                stringy.append("SELECT ");
                break;
            */
           
        if (Operation == 1){
          // for upload
        }
        else if (Operation == 2){
          // for filter
            stringy.append("SELECT ");
        }
        
    }
    
    // construct the SQL string to be executed on the database
    public String BuildFilterSQL(LinkedList<OperationType> filList){
       
        Customer_Database_INFO CDI = new Customer_Database_INFO();
        String Table_name = null;
        String ColumnString = null;
        String star = "*";
        
        boolean UserInput_all_null = true;
        
        for(OperationType filter: filList){
            
            // method to append table to the column, ex. CINFO.Name
            System.out.println("operation " + filter.getOperation());
            
            //Table_name = CheckTable(filter.getOperation());
            //TableSave.add(Table_name);
            
            try{
                //String[]temp = CDI.getCol(filter.getTable());
                
                ColumnString = CDI.Column_names[CDI.getOffSet(filter.getTable(),filter.getOperation())];
                //ColumnString = temp[CDI.getOffSet(CDI.getCol(filter.getTable()),filter.getOperation())];
                if (ColumnString.contains("All")){
                    ColumnString = "*";
                }
            } catch (Exception e){
                // only if GUI has passed incorrect value.
                Acknowledgement = "Column not found, GUI possibly passing incorrect values.";
                return Acknowledgement;
                
            }
            //isContains method failing
            if(ColumnString.contains(star)){
                stringy.append(ColumnString + ",");
            }
            else {
                Table_name = CDI.Table_names[filter.getTable()];
                stringy.append(Table_name + "." + ColumnString + ",");
            }
            
            if((filter.getInput().equalsIgnoreCase(""))){
                
            }
            else {
                
                UserInput_all_null = false;
                
            }
            
        }
        
        stringy.deleteCharAt(stringy.length()-1);
        
        boolean Join = true;
        
        int count = 0;
        
        for(OperationType filter: filList){
            //append all FROM, JOIN, ON statements.
            Table_name = CDI.Table_names[filter.getTable()];
            if(ColumnString.contains("*")){
                stringy.append(" FROM " + Table_name);
                Join = false;
            }
        }
        if(Join){
            stringy.append(" " + JOINS + " ");
        }
        
        String spec = null;
        int len = filList.size();
        
        if(UserInput_all_null == false){
            
            stringy.append("WHERE ");
            
            while(true){
                spec = filList.get(count).getInput();
                
                if(spec.equalsIgnoreCase(null)){
                    
                    count ++;
                    continue;
                    
                }
                else {
                    ColumnString = CDI.Column_names[CDI.getOffSet(filList.get(count).getTable(),filList.get(count).getOperation())];
                    Table_name = CDI.Table_names[filList.get(count).getTable()];
                    if (ColumnString.contains("All")){
                    ColumnString = "*";
                }
                    
                    if(!(count<1)) {
                        stringy.append("AND ");
                    }
                    if(ColumnString.contains("*")){
                        stringy.append(Table_name + "." + "BUILD_ID" + " LIKE '" + spec + "' ");
                    }
                    else {
                        stringy.append(Table_name + "." + ColumnString + " LIKE '" + spec + "' ");
                    }
                    count ++;
                    if (count>= len){
                        stringy.append(";");
                        break;
                    }
                }
            }
        }
        else {
            
            stringy.append(";");
            
        }
            
        // Call method to execute SQL built
        // need method in GUI to take ResultSet an argument
        ExecuteOperation(stringy);
        
        //acknowledge method in GUI
        return Acknowledgement;
    }
    
     private static boolean isContain(String source, String subItem){
         String pattern = "\\b"+subItem+"\\b";
         Pattern p=Pattern.compile(pattern);
         Matcher m=p.matcher(source);
         return m.find();
    }
   
    
    public ResultSet getResult(){
        
        return resultSet;
    }
    
    // Execute SQL
    protected void ExecuteOperation(StringBuilder stringy2){
        Acknowledgement = "SQL executed, Table returned.";
        String SQLJob = stringy2.toString();
        System.out.println("SQLJob = "+SQLJob);
        try{
        
            statement = connect.createStatement();
            resultSet = statement.executeQuery(SQLJob);
            
           
            
        } catch (Exception e) {
            
            //send to acknowledgement method
            System.out.println("readDataBase Exception. " + e.getMessage());
            Acknowledgement = "Filter combination not compatible, please try another.";
        } 
        
        finally {
            
           //close();
        }
        
    }
    
    /*
    public String UploadFileDirectory(String Directory){
        
        String FileContents = null;
        
        try {
            filePath = Paths.get(Directory);            
            File file = new File(Directory);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF-16LE"));
            
            try {
                FileContents = getContents(br);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                return "Could not read file";
                
            }
            
        }
        catch (FileNotFoundException ex){
            
            return "File not found.";
            
        } 
        catch (UnsupportedEncodingException e) {
            
            return "Could not read file, encoding error.";
            //e.printStackTrace();
        }
        
        BuildInsertSQL(FileContents);
        
        return "File extracted and customer database updated";
    }
    
    // method to extract data, also create a folder with the build ID
    // move template file into build ID folder
    //protected void ExtractFile
    
    public void BuildInsertSQL(String ContentsOfFile){
        
        //String Lines = ContentsOfFile;
        
        
        
    }
    
    public String getContents(BufferedReader buff)
                throws FileNotFoundException
      {
        StringBuilder FileString = new StringBuilder();
        try
        {
          
          String line = buff.readLine();
          while (line != null)
          {
              FileString.append(line);
            
              FileString.append("<br />");
            line = buff.readLine();
          }
          
          buff.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        return FileString.toString();
      }
    */
    
    // needs to be called when GUI is closed
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } 
        catch (Exception e) {

        }
    }
    
}

