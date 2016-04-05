import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Path;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;

import java.util.Properties;



import org.apache.commons.io.FileUtils;
;

public class Customer_Database_Controller implements Operations_Display {

    // Database name
    static final String whitebox_db = "whitebox_db"; 
    
    private int count = 0;
    
    //OperationsEnum Op;
    private StringBuilder stringy = new StringBuilder();
    
    private Statement statement = null;
    private ResultSet resultSet = null;
    Connection connect = null;

    
    
    protected String Acknowledgement = null;
    
    
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
    
    // Connect to the customer database stored with mySQL
    public void CustomerDB_Connect() throws FileNotFoundException{
        
        // XML file, config.xml
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
        
        // retrieve information set in the config.xml file
        String userName = props.getProperty("Username");
        String password = props.getProperty("Password");
        
        //hard coded for now since it isnt working when retrieved from the xml file.
        String host = "jdbc:mysql://localhost:3306/whitebox_db?autoReconnect=true&useSSL=false";
        /*
        try {
           
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("Connection to Customer Database failed, Unable to load driver class");
            
        }
        */
        
        //Attempt connection
        try {
            connect = DriverManager.getConnection(host, userName, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            Acknowledgement = "Cannot connect to the database!" + e;
            throw new IllegalStateException("Cannot connect to the database!", e);
        }
        
    }

    // GUI selects which operation after each corresponding button press to set up SQL query string.
    public void SelectOperation(int Operation){
        
    	// Connect to database initially, connection is set to auto connect because of this "autoReconnect=true" in the hostURL
        if(FirstOp){
            
            try {
                CustomerDB_Connect();
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            FirstOp = false;
        }
        
        // empty the string
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
        // upload selection, no initial append  
        if (Operation == 1){
          // for upload
        }
        // filter selection, starts with SELECT
        else if (Operation == 2){
          // for filter
            stringy.append("SELECT ");
        }
        
    }
    
    // User provides filename, it must be in the directory set in the config.xml file, then this copies the file to another set directory. Passes the file to BuildInsertSQL
    public String UploadFileDirectory(String FileName){
        
    	FileInputStream fis = null;
    	
    	
    	
        Properties props = new Properties();
        try{
        	fis = new FileInputStream("./config.xml"); 
        } catch (FileNotFoundException e){
        	
        	return "config file could not be found to determine set directory of your file.";
        	
        }
        try {
            props.loadFromXML(fis);
        } catch (InvalidPropertiesFormatException e2) {
            // TODO Auto-generated catch block
        	return "Could not read file: " + e2.getMessage();
            
        } catch (IOException e2) {
            // TODO Auto-generated catch block
        	return "Could not read file: " + e2.getMessage();
            
        }
        
        String directory = props.getProperty("CustomerFormTemplate");
        directory = directory + FileName + ".xml";
        //System.out.print(directory);
        String CopyDes = props.getProperty("CustomerForms");
        
        props = new Properties();
        try{
        	fis = new FileInputStream(directory); 
        } catch (FileNotFoundException e){
        	
        	return "File not found.";
        	
        }
        try {
            props.loadFromXML(fis);
        } catch (InvalidPropertiesFormatException e2) {
            // TODO Auto-generated catch block
        	return "Could not read file: " + e2.getMessage();
            
        } catch (IOException e2) {
            // TODO Auto-generated catch block
        	return "Could not read file: " + e2.getMessage();
            
        }
        
        int build_num = BuildInsertSQL(props);
        
        // Copy populated template to another location with specified filename. Not working at the moment.
        /* 
        String sBuild = Integer.toString(build_num);
        
        CopyDes = CopyDes + sBuild;
        
        // copy file to another directory
        File source = new File(directory);
        File dest = new File(CopyDes);
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        
        String message = "File extracted, commited to customer database, and filled template can be found at "  + CopyDes;
        
    	

    	
        return message;
    	
 
    }
    
    // construct the SQL string to be executed on the database
    public String BuildFilterSQL(LinkedList<OperationType> filList){
       
        Customer_Database_INFO CDI = new Customer_Database_INFO();
        String Table_name = null;
        String ColumnString = null;
        String star = "*";
        String All = "All";
        
        boolean UserInput_all_null = true;
        boolean first = true;
        String check = null;
        
        for(OperationType filter: filList){
            
        	if(first){
        		check = filter.getInput();
        		first = false;
        	}
        	
            // method to append table to the column, ex. CINFO.Name
            System.out.println("operation " + filter.getOperation());
            
            //Table_name = CheckTable(filter.getOperation());
            //TableSave.add(Table_name);
            
            try{
                //String[]temp = CDI.getCol(filter.getTable());
                
                ColumnString = CDI.Column_names[CDI.getOffSet(filter.getTable(),filter.getOperation())];
                //ColumnString = temp[CDI.getOffSet(CDI.getCol(filter.getTable()),filter.getOperation())];
                if (exactString(ColumnString, All)){
                    ColumnString = "*";
                }
            } catch (Exception e){
                // only if GUI has passed incorrect value.
                Acknowledgement = "Column not found, GUI possibly passing incorrect values.";
                return Acknowledgement;
                
            }
            
            if(exactString(ColumnString, star)){
                stringy.append(ColumnString + ",");
            }
            else {
                Table_name = CDI.Table_names[filter.getTable()];
                
                // View entire database
                if (filter.getTable() == 5){
                	
                	stringy.append("* ");
                	break;
                }
                
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
        boolean bCheck = false;
        for(OperationType filter: filList){
            //append all FROM, JOIN, ON statements.
            Table_name = CDI.Table_names[filter.getTable()];
            if(filter.getTable() != 5){
            	if(ColumnString.contains("*")){
	                stringy.append(" FROM " + Table_name);
	                Join = false;
            	}
            }
            else {
            	
            	if(!ColumnString.contains("*")){
            		bCheck = true;
            	}
            }
            
        }
        if(Join){
            stringy.append(" " + JOINS + " ");
        }
        
        if(bCheck){
        	
        	stringy.append(" Where customer_info.build_id LIKE '" + check +"'");
        	
        }
        
        String spec = "";
        int len = filList.size();
        
        if(UserInput_all_null == false){
            
            stringy.append(" WHERE ");
            
            while(true){
                spec = filList.get(count).getInput();
                
                if(spec.equalsIgnoreCase("")){
                    
                    count ++;
                    continue;
                    
                }
                else {
                    ColumnString = CDI.Column_names[CDI.getOffSet(filList.get(count).getTable(),filList.get(count).getOperation())];
                    Table_name = CDI.Table_names[filList.get(count).getTable()];
                    if (exactString(ColumnString, All)){
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
        ExecuteOperation(stringy, 2);
        
        //acknowledge method in GUI
        return Acknowledgement;
    }
    
    // check for exact string
     private static boolean exactString(String source, String pattern){
         
 	  	int fromIndex = 0;				//starting index
 	
 	  	int EndIndex =  pattern.length();
 	  	
 	  	String FromString = source.substring(fromIndex, EndIndex); 
 	  	
 	  	if (FromString.contains(pattern)){
 	  		
 	  		return true;
 	  		
 	  	}
 	  	return false;
    }
   
    
    public ResultSet getResult(){
        
        return resultSet;
    }
    
    // Execute SQL and set the resultSet so GUI can get and display.
    protected void ExecuteOperation(StringBuilder stringy2, int Operate){
        Acknowledgement = "SQL executed, Table returned.";
        String SQLJob = stringy2.toString();
        System.out.println("SQLJob = "+SQLJob);
        
        // for filter SELECT SQL query
        if(Operate == 2){
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
	        	
	        	// now closed with a button press elsewhere.
	        }
        } 
        // different way to execute for INSERT Query
        else if (Operate == 1){
        	
        	try{
        		connect.setAutoCommit(false);
        		
	            statement = connect.createStatement();
	            statement.executeUpdate(SQLJob);
	            
	            // maybe need to auto commit after each "insert into" to be able to "insert into" other tables that have references. Then the commit button becomes redundant and doesn't give the user the ability to choose to commit, but could just provide a roll back button.
	            //connect.commit();
	            
	        } catch (Exception e) {
	            
	            //send to acknowledgement method
	            System.out.println("readDataBase Exception. " + e.getMessage());
	            
	        } 
	        
	        finally {
	            
	           //close();
	        	
	        	// now closed with a button press elsewhere.
	        }
        	
        }
    }
    
    

    
    // Build SQL to insert. easy way first. 
    // Only first "insert into" works, the ones that follow contain a bug that relates to the query that is attempted to be executed on the customer database.
    // error; readDataBase Exception. Cannot add or update a child row: a foreign key constraint fails
    public int BuildInsertSQL(Properties proper){
        count = 0;
        
        // determine next build_ID since the database auto increments, this is the reason why the build_ID field in the customer_info table is set to default. Then the information in the other tables that relate to the same build_ID must explicitly set it in the "insert into"
    	try{
            
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM whitebox_db.customer_info;");
            
        } catch (Exception e) {
            
            //send to acknowledgement method
            System.out.println("readDataBase Exception. " + e.getMessage());
            
        } 
    	
    	// get build ID since auto increment in database
    	

    	try {
			while (resultSet.next()) {
			    
			    count ++;
			}
		} catch (SQLException e) {
			
			
			e.printStackTrace();
		} 
    	
    	count ++;
    	
    	// customer_info
    	String Name = proper.getProperty("Name");
    	String Email = proper.getProperty("Email");
    	String Phone = proper.getProperty("Phone");
    	String Address = proper.getProperty("Address");
    	String Delivery_date = proper.getProperty("Delivery_date");
    	
    	// payment_info
    	String Payment_method_use = proper.getProperty("Payment_method_use");
    	String Total_value = proper.getProperty("Total_value");
    	String Delivery_confirmation_link = proper.getProperty("Delivery_confirmation_link");
    	
    	//int iTotalVal = Integer.parseInt(Total_value);

    	// product_info
    	String Component_type = proper.getProperty("Component_type");
    	//int iCompType = Integer.parseInt(Component_type);
    	
    	String Manufacturer = proper.getProperty("Manufacturer");
    	//int iManu = Integer.parseInt(Manufacturer);
    	
    	String Product_description = proper.getProperty("Product_description");
    	String model_number = proper.getProperty("model_number");
    	String Serial_number = proper.getProperty("Serial_number");
    	String Rebate_value = proper.getProperty("Rebate_value");
    	String Price = proper.getProperty("Price");
    	String Warranty_period = proper.getProperty("Warranty_period");
    	String Warranty_expire = proper.getProperty("Warranty_expire");
    	String Invoice_date = proper.getProperty("Invoice_date");
    	String Invoice_number = proper.getProperty("Invoice_number");
    	String Sale_order_number = proper.getProperty("Sale_order_number");
    	String Item_SKU = proper.getProperty("Item_SKU");
    	
    	// insert for customer_info table
    	stringy.append("insert into " + whitebox_db + ".customer_info values (default, '" + Name + "', '" + Email + "', '" + Phone + "', '" + Address + "', '" + Delivery_date + "');");
    	ExecuteOperation(stringy, 1);
    	
    	stringy.setLength(0);
    	// insert for payment_info table
    	stringy.append("insert into " + whitebox_db + ".payment_info values (" + count + ", '" + Payment_method_use + "', " + Total_value + ", '" + Delivery_confirmation_link +"');");
    	ExecuteOperation(stringy, 1);
    	stringy.setLength(0);
    	// insert for product_info table
    	stringy.append("insert into " + whitebox_db + ".product_info values (" + count + ", " + Component_type + ", " + Manufacturer + ", '" + Product_description + "', " + model_number + 
    			", " + Serial_number + ", " + Rebate_value + ", " + Price + ", " + Warranty_period + ", '" + Warranty_expire + "', '" + Invoice_date + "', " + Invoice_number + ", " + Sale_order_number + ", " + Item_SKU + ");");
    	
    	System.out.println(stringy);
    	
    	ExecuteOperation(stringy, 1);
    	stringy.setLength(0);
    	
    	viewCommit();
    	
    	return count;
    }
    
    // for commit button
    public void commitTO(){
    	try{
    		connect.commit();
    	} catch (SQLException e){
    		
    		e.printStackTrace();
    		
    	}
    	
    	viewCommit();
    	
    }
    
    // after all "insert into" return the row that was just added.
    private void viewCommit(){
	    try{
	        
	        resultSet = statement.executeQuery("SELECT * " + JOINS + " WHERE customer_info.BUILD_ID LIKE '" + count + "';");
	        
	    } catch (Exception e) {
	        
	        //send to acknowledgement method
	        System.out.println("readDataBase Exception. " + e.getMessage());
	        
	    } 
	    
	    finally {
	        
	       //close();
	    	
	    	// now closed with a button press elsewhere.
	    }
    }
    // needs to be called when GUI is closed. Linked to cancel button
    public void close() {
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

