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
import java.util.List;
import java.util.Properties;

public class Customer_Database_Controller implements Operations_Display {

	// Database name
	static final String whitebox_db = "whitebox_db"; 
	
	OperationsEnum Op;
	private StringBuilder stringy = new StringBuilder();
	
    private Statement statement = null;
    private ResultSet resultSet = null;
    Connection connect = null;

	// declared like this since we implement functionality that doesn't modify the database columns
	static final int CINF_num = 6;
	static final int PayINF_num = 10;
	static final int ProINF_num = 24;
	static final int ManINF_num = 26;
	static final int ComINF_num = 28;
	
	// Table names, add more later
	private String C_INFO = "customer_info";
	private String Pay_INFO = "payment_info";
	private String Pro_INFO = "product_info";
	private String Man_INFO = "manufacturer_table";
	private String Com_INFO = "component_type_table";
	
	// Lists for Filter
	private List<String> TableSave = new ArrayList<String>();
	private List<String> ColSave = new ArrayList<String>();
	private List<String> UserSave = new ArrayList<String>();
	
	// For insert
	private List<String> Col_Contents = new ArrayList<String>();
	
	Boolean FirstOp = true;
	
	// instead of determining which tables to link, just link them all.
	private String JOINS = "FROM whitebox_db.customer_info AS CINFO JOIN whitebox_db.payment_info AS PYINFO ON CINFO.Build_ID = PYINFO.Build_ID JOIN whitebox_db.product_info AS PRINFO ON CINFO.Build_ID = PRINFO.Build_ID JOIN whitebox_db.manufacturer_table AS MANNAME ON PRINFO.Manufacturer = MANNAME.Manufacturer JOIN whitebox_db.component_type_table AS COMP ON PRINFO.Component_type = COMP.Component_type";
	
	public Customer_Database_Controller(OperationsEnum Op){
		this.Op = Op;
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
	    String host = props.getProperty("hostURL");
	    String userName = props.getProperty("Username");
	    String password = props.getProperty("Password");
	    
	    try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
		} catch (ClassNotFoundException e) {
			
			System.out.println("Connection to Customer Database failed, Unable to load driver class");
			
		}
	    
	    connect = null;
		try {
			connect = DriverManager.getConnection(host, userName, password);
		} catch (SQLException e1) {
			
			// need acknowledgement method
			System.out.println("Could not connect to Database: " + host + ", confirm host, username, and password in config.xml are correct");
			
		} 
		
	}
	
	public void SelectOperation(int Operation){
		
		if(FirstOp){
			
			try {
				CustomerDB_Connect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FirstOp = false;
		}
		
		// empty string
		stringy.setLength(0);
		
		// if 1, upload file
		// Operations Display calls "UploadFileDirectory
		
		// if 2, Operations Display calls "filter
		
		switch(Op){
		
			case UPLOAD_FILE:
				// partly set up sql to execute
				//stringy.append("");
				break;
				
			case VIEW_FILTER:
				// partly set up sql to execute
				stringy.append("SELECT ");
				break;
		
		}
		
	}
	
	Path filePath;
    BufferedInputStream bufferedInputStream = null;
    FileInputStream  fileInputStream= null;
	
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
		
		String Lines = ContentsOfFile;
		
		
		
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
	
	public void BuildFilterSQL(){
		
		int leng = TableSave.size();
		
		for(int i = 0; i < leng; i ++){
		
			stringy.append(TableSave.get(i) + "." + ColSave.get(i) + ",");
			
		}
		
		stringy.deleteCharAt(stringy.length());

		//append all FROM, JOIN, ON statements.
		stringy.append(" " + JOINS + " ");
		
		boolean end = true;
		boolean all_null = true;
		int count = 0;
		
		for(String US: UserSave){
			
			if(US.equalsIgnoreCase(null)){
				
			}
			else {
				
				all_null = false;
				
			}
			
		}
		
		int len = UserSave.size();
		
		if(all_null == false){
			
			stringy.append("WHERE ");
			
			while(end){
				String spec = UserSave.get(count);
				
				if(spec.equalsIgnoreCase(null)){
					
					count ++;
					continue;
					
				}
				else {
					
					stringy.append(TableSave.get(count) + "." + ColSave.get(count) + " LIKE '" + UserSave.get(count) + "' ");
					
					if(!((count+1) == len)){
						stringy.append("AND ");
					}
					else {
						
						stringy.append(";");
						
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
		
	}
	
	private String CheckTable(int Col_Num){
		
		String Table_title = null;
		
		if(Col_Num < CINF_num){
			
			Table_title = C_INFO;
			
		} 
		else if(Col_Num < PayINF_num){
			
			Table_title = Pay_INFO;
			
		}
		else if(Col_Num < ProINF_num){
			
			Table_title = Pro_INFO;
			
		}
		else if(Col_Num < ManINF_num){
			
			Table_title = Man_INFO;
			
		} else if(Col_Num < ComINF_num){
			
			Table_title = Com_INFO;
			
		}
		
		return Table_title;
		
	}
	
	// Execute SQL
	protected ResultSet ExecuteOperation(StringBuilder stringy2){
		
		String SQLJob = stringy2.toString();
		
		try{
		
			statement = connect.createStatement();
			resultSet = statement.executeQuery(SQLJob);
					
		} catch (Exception e) {
			
			//send to acknowledgement method
            System.out.println("readDataBase Exception. " + e.getMessage());
        } 
        
        finally {
            close();
        }

		return resultSet;
	}
	
	public String Addfilter(int Col, String User_input, boolean done){
		// GUI should check for ";" and not pass if present
		
		
		if(done == false){
			int Column_name = Col;
			String Table_name = null;
			String FilterString = User_input;
			String ColumnString = null;
			
			Customer_Database_INFO CDI = new Customer_Database_INFO();
			
			// method to append table to the column, ex. CINFO.Name
			// Can save into an array for later use for FROM and JOIN and ON but,
			// I will probably just append the exhaustive command that includes all tables and links.
			Table_name = CheckTable(Column_name);
			
			try{
				ColumnString = CDI.Column_names[Column_name];
			} catch (Exception e){
				// only if GUI has passed incorrect value.
				String Acknowledgement = "Column not found, GUI possibly passing incorrect values.";
				return Acknowledgement;
				
			}
			
			TableSave.add(Table_name);
			ColSave.add(ColumnString);
			
			UserSave.add(FilterString);
		} 
		else {

			BuildFilterSQL();
			
		}
		
		return "Click 'Apply Filter' to execute";
		
	}

	public String Cancelfilter(int row) {
		int rowClear = row;
		stringy.setLength(0);
		
		if(rowClear == -1){
			
			TableSave.clear();
			ColSave.clear();
			UserSave.clear();
			
		}
		else {
			
			TableSave.remove(rowClear);
			ColSave.remove(rowClear);
			UserSave.remove(rowClear);	
			
		}
		
		return "Click 'Apply Filter' to execute.";
	}
	
	
	// place holder for method that returns data
	
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

