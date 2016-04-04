import java.util.LinkedList;
import java.sql.ResultSet;
public interface Operations_Display {

	/* 
	 * Example Upload file = 1, view = 2. 
	 * After selection, Customer controller will call for additional information.
	 */
	void SelectOperation(int Operation);
	
	/*
	 * After select "Upload file", int Operation = 1, Customer controller calls this method to ask user
	 * for directory of template file.
	 */
	String UploadFileDirectory(String Filename);
	
	String BuildFilterSQL(LinkedList<OperationType> filList);
	void close();
	
	//get resultSet from filter
	ResultSet getResult();
	
	// commit an insert
	void commitTO();

}
