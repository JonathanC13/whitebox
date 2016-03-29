
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
	String UploadFileDirectory(String Directory);
	
	/*
	 * After select "view", int Operation = 2, Customer controller calls this method to ask user
	 * for filter.
	 * 
	 * refer to Scenario 2 and 3 in report.
	 * 
	 * "title" represents each column in the database, refer to list on basecamp.
	 * 
	 * If "User_input" is left empty, just make it null, User_input = null; 
	 * 
	 * When user clicks "More filters", Opertaions Display will send the information to this method,
	 * and Customer controller will store and return. GUI needs to allow user to add more, another 
	 * drop box and user input field. Send done = true when use clicks "Apply filter".
	 * 
	 * The String returned is the Acknowledgement message that should be displayed to the user. GUI needs a method named Acknowledgement that displays the message to the user.
	 */
	String Addfilter(int Col, String User_input, boolean done);
	
	/*
	 * Cancel one of the filters. int row is the index for the row, start from 0. For a "Clear All" button, can set row = -1 will clear all filters.
	 * 
	 */
	String Cancelfilter(int row);
	
	/*
	 * GUI needs to write a method I can call to return the filtered data and display to user.
	 * 
	 * ex. DisplayResult(ResultSet result)
	 */

	/* GUI needs a method that I can call that displays a message to the user.
	 * 
	 */


}
