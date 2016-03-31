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
	//String UploadFileDirectory(String Directory);
	
	String BuildFilterSQL(LinkedList<OperationType> filList);
	
	ResultSet getResult();
	/*
	 * GUI needs to write a method I can call to return the filtered data and display to user.
	 * 
	 * ex. DisplayResult(ResultSet result)
	 */

	/* GUI needs a method that I can call that displays a message to the user.
	 * 
	 */


}
