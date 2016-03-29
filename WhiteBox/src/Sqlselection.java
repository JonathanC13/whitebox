import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;	//don't use java.bean.Statement

/*	1. Download JDBC package, which includes sqljdbc41.jar, from Microsoft.
 * 	   Copy sqljdbc41.jar to a directory, and set it as one of the Referenced Libraries
 */

public class Sqlselection {
	
	//setting up URL
	static String JDBC_SQLSERVER = "jdbc:sqlserver:";
	static String SERVER_COMPUTER = "//MIGRANT-PC";			//Change this to your server name
	static String SQL_PORT = "1433";						//Change this to the SQL port. 1433 is default
	
	static String DATABASE_INSTANCE = "\\HOME";				//Change it to DB instance that has your DB
	static String DATABASE_NAME = "AdventureWorks2008R2";	//Change it to your DB name
	
	static String URL = JDBC_SQLSERVER + SERVER_COMPUTER + DATABASE_INSTANCE + ":" + SQL_PORT + ";databaseName=" + DATABASE_NAME;
	//static String url = "jdbc:sqlserver://MIGRANT-PC\\HOME:1433;databaseName=AdventureWorks2008R2";

	
	static String dbUserName = "sa";
	static String dbPassword = "Pa55w0rd";
    
    
	
	public static void main(String[] args) {
		
		test_select_statement();
		
		test_select_statement_better();
		
		test_stored_procedure();
	}
	
	
	
	//***************************************************************************************************
	// Test Select statement
	//***************************************************************************************************
	static void test_select_statement() {
		System.out.println("\n\n\n**********************************\n***** test Select Statements *****\n**********************************\n");

		
		Statement s1 = null;
		Connection con = null;
		
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //String userName = "sa";
            //String password = "Pa55w0rd";
            //String url = "jdbc:sqlserver://MIGRANT-PC\\HOME:1433"+";databaseName=AdventureWorks2008R2";
            con = DriverManager.getConnection(URL, dbUserName, dbPassword);
            
            s1 = con.createStatement();          
            ResultSet rs = s1.executeQuery("select TOP 10 [FirstName],[MiddleName],[LastName] FROM [AdventureWorks2008R2].[Person].[Person]");
            
            if(rs != null){
            	
            	int personCount = 1;
                while (rs.next()){      
                	String lastName = rs.getString(3);		//the first element's index is 1, NOT 0
                	if(rs.wasNull()) {
                		lastName = "";
                	}
                    String middleName = rs.getString(2);
                    if(rs.wasNull()) {
                    	middleName = ", ";
                    }
                    else {
                    	middleName = ", " + middleName;
                    }
                    String firstName = rs.getString(1);
                    if(rs.wasNull()) {
                    	firstName = "";
                    }
                    
                    String personName = personCount + ". " + lastName + middleName + " " + firstName;
                    System.out.println(personName);
                    
                    personCount++;
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
        	if(s1 != null) {
        		try {
        			s1.close();
        			con.close();
                } 
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        	}
        }
	}
	
	
	
	
	static public void test_select_statement_better() {
		System.out.println("\n\ntest_select_statement_better()-----");
				
		Connection con = null;
		
		//String userName = "sa";
        //String password = "Pa55w0rd";
        //String url = "jdbc:sqlserver://MIGRANT-PC\\HOME:1433"+";databaseName=AdventureWorks2008R2";
        
        try {
			con = getConnectionToDb(URL, dbUserName, dbPassword);		
			viewTable(con);
		}
		catch (Exception e)
        {
            e.printStackTrace();
        }	
        
        //now, connection is not required anymore. release it
        finally {
        	if(con!=null) {
        		try {
        			con.close();
        		}
        		catch(Exception e)
                {
                    e.printStackTrace();
                }
        	}
        }
	}
	
	static public Connection getConnectionToDb(String dbUrl, String dbUserName, String dbPassword) {
		Connection con = null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
		}
		catch (Exception e)
        {
            e.printStackTrace();
        }	
		return con;
	}
	
	public static void viewTable(Connection con) throws SQLException {

	    Statement stmt = null;
	    String query = "select TOP 10 [BusinessEntityID],[FirstName],[MiddleName],[LastName] FROM [AdventureWorks2008R2].[Person].[Person]";
	    
	    try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        
        	int personCount = 1;
	        while (rs.next()) {
	        	String counter = String.format("%3d", personCount);		// 3-digit counter
	        	
	        	int id = rs.getInt("BusinessEntityID");			//number must be read using getInt()
	        	String personId = String.format("%7d", id);
	        	
            	String lastName = rs.getString("LastName");		//uses column name as index instead of using number

                String middleName = rs.getString("MiddleName");	//from the table design, only middle name is nullable. So check it
                if(rs.wasNull()) {
                	middleName = "";
                }
                
                String firstName = rs.getString("FirstName");
                                
                String person = String.format("%s %s %15s, %-2s %15s", counter, personId, lastName, middleName, firstName);
                System.out.println(person);
                
                personCount++;
	        }
	    } catch (SQLException e ) {
	    	e.printStackTrace();
	    	
	    } finally {
	        if (stmt != null) { 
	        	stmt.close(); 
	        }
	    }
	}
	
	
	
	
	
	
	
	
	
	
	//***************************************************************************************************
	// Test stored procedures calls
	//***************************************************************************************************
	static public void test_stored_procedure() {
		System.out.println("\n\n\n\n\n**********************************\n***** test stored procedures *****\n**********************************\n");
				
		//String userName = "sa";
        //String password = "Pa55w0rd";
        //String url = "jdbc:sqlserver://MIGRANT-PC\\HOME:1433"+";databaseName=AdventureWorks2008R2";
        
        //use Try-with-resources. It will close Connection automatically
        try ( Connection con = getConnectionToDb(URL, dbUserName, dbPassword) ) {
        	System.out.println("-----------------------\n-----No parameters-----\n-----------------------");
        	executeSprocNoParams(con);
        	
        	System.out.println("\n-------------------------------\n-----With input parameters-----\n-------------------------------");
        	executeSprocInParams(con);
        	
        	System.out.println("\n----------------------------------\n-----With In & Out parameters-----\n----------------------------------");
        	executeSprocInOutParams(con);
        	
        	//con.close();
		}
		catch (Exception e)
        {
            e.printStackTrace();
        }	
	}
	
	
	/*
	 * Create a test store procedure (as show below) that does not require input or output parameters
		
		USE AdventureWorks2008R2
		GO
		
		CREATE PROCEDURE GetContactFormalNames
		AS
		BEGIN
			SELECT TOP 10 Title + ' ' + FirstName + ' ' + LastName AS FormalName
			FROM Person.PERSON
		END
		GO
		
		--run
		EXEC GetContactFormalNames
		GO
		
		--delete the stored procedure
		DROP PROC GetContactFormalNames
		GO
	 */
	static void executeSprocNoParams(Connection con) {
		
		//use Try-with-resources. Statement and ResultSet will be closed automatically.
		//use regular Try-Catch, Statement and ResultSet must be closed programmatically.
		try ( Statement stmt = con.createStatement();		//createStatement is used for the 'no input and no output' cases. 
			  ResultSet rs = stmt.executeQuery("{call dbo.GetContactFormalNames}") ) {
			
			while (rs.next()) {
				String formalName = rs.getString("FormalName");
				System.out.println( rs.wasNull() ? "-" : formalName);	//rs.wasNull() can be used only after its rs.getString() has been called
			}
			
			//rs.close();
			//stmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Use the existing St Proc from 'AdventureWorks2008R2' for testing
	public static void executeSprocInParams(Connection con) {
		
		//Uses prepareStatement for calling the stored procedure that needs input but no output
		try( PreparedStatement pstmt = con.prepareStatement("{call dbo.uspGetEmployeeManagers(?)}") ) {	//? is the placeholder for parameter #1
			pstmt.setInt(1, 50);	//set parameter #1 value to 50
			
			try( ResultSet rs = pstmt.executeQuery()) {
				
				while (rs.next()) {
					System.out.println("EMPLOYEE:");
					System.out.println(rs.getString("LastName") + ", " + rs.getString("FirstName"));
					System.out.println("MANAGER:");
					System.out.println(rs.getString("ManagerLastName") + ", " + rs.getString("ManagerFirstName"));
					System.out.println();
				}			
				//rs.close();
			}
			//pstmt.close();		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/* executeStoredInOutProcedure() needs the stored proc 'GetJobTitle'
	 * create this storced procedure so that the following function can be tested
	 * 
	 	USE AdventureWorks2008R2
		GO
		
		CREATE PROCEDURE GetJobTitle
			@businessEntityID INT,
			@employName NCHAR(64) OUTPUT,	
			@jobTitle NCHAR(64) OUTPUT
		AS	
		BEGIN
				-- returns data as output. Must use 'CallableStatement'
				SELECT @employName = Person.Person.FirstName + ' ' + Person.Person.LastName, @jobTitle = JobTitle	
				
				-- use this instead of above line to get data with ResultSet. Uses 'Statement'
				-- SELECT Person.Person.FirstName + ' ' + Person.Person.LastName, JobTitle
				
				FROM   HumanResources.Employee JOIN Person.Person   ON HumanResources.Employee.BusinessEntityID = Person.Person.BusinessEntityID
				WHERE HumanResources.Employee.BusinessEntityID = @businessEntityID
		END
		
		----- run in Query Windows -----
		//create variables to hold returned values
		DECLARE	@return_value int,  @employNameOut nchar(64),  @jobTitleOut nchar(64)
		
		EXEC @return_value = [dbo].[GetJobTitle]  @businessEntityID = 5,  @employName = @employNameOut OUTPUT,  @jobTitle = @jobTitleOut OUTPUT
		
		//To see the output
		SELECT	@employNameOut as N'@employNameOut',  @jobTitleOut as N'@jobTitleOut'
		
		//To see the return value
		SELECT	'Return Value' = @return_value
		
		
		
		--delete the stored procedure
		DROP PROC GetJobTitle
		GO
	 */
	public static void executeSprocInOutParams(Connection con) {
		
		//Uses prepareCall for calling the stored procedure that has input and output
		//This stored procedure has three parameters so use (?, ?, ?) as placeholder. The first parameter is IN, the second and third parameters are OUT
		try ( CallableStatement cstmt = con.prepareCall("{call dbo.GetJobTitle(?, ?, ?)}") ) {
			
			cstmt.setInt(1, 6);										//set first parameter as IN, BusinessEntityID
			cstmt.registerOutParameter(2, java.sql.Types.NCHAR);	//set second parameter as OUT, employName
			cstmt.registerOutParameter(3, java.sql.Types.NCHAR);	//set third parameter as OUT, jobTitle
			
			cstmt.execute();
			System.out.println("Name: " + cstmt.getString(2));		//read OUT from CallableStatement. Not from ResultSet
			System.out.println("Job Title: " + cstmt.getString(3));	//read OUT from CallableStatement. Not from ResultSet
			
			//cstmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
