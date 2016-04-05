import java.awt.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
public class FileEditGUI {

    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private JTextArea textArea;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileEditGUI window = new FileEditGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public FileEditGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 650, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(650, 450));
        JButton btnEnter = new JButton("Enter");
        btnEnter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {       
            	
                Operations_Display op = new Customer_Database_Controller();
                
                op.SelectOperation(1);
                
                textArea.setText(op.UploadFileDirectory(textField.getText()));
                
                
                
                
            }
            
        });
        
        textField = new JTextField();
        textField.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        
        textArea = new JTextArea();
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {       
                
                Operations_Display op = new Customer_Database_Controller();
                op.close();
                frame.dispose();
               
            }
        });
        JButton btnCommit = new JButton("Commit");
        btnCommit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {       
                Operations_Display op = new Customer_Database_Controller();
                //op.SelectOperation(1);
                //textArea.setText(op.UploadFileDirectory(textField.getText()));
                // do commit
                //op.commitTO();
                table.setModel(setTable(op.getResult()));
            }
            public DefaultTableModel setTable(ResultSet rs){
                DefaultTableModel data_mode = new DefaultTableModel();;
                try {
                  int var_count = rs.getMetaData().getColumnCount();
                  String[] var = new String[var_count];
                  for(int i = 1; i<= var_count; i++){
                      var[i-1] = rs.getMetaData().getColumnName(i);
                  }
                  data_mode.setColumnIdentifiers(var);
                  while(rs.next()){
                      String[] row = new String[var_count];
                      for(int i = 1; i<= var_count; i++){
                          row[i-1] = rs.getString(i);
                      }
                      data_mode.addRow(row);
                    }
                } catch (SQLException ignore) {}
                return data_mode;
            }
        });
        JLabel lblMessageThisVersion = new JLabel("Message: Provide filename that is in the specified directory choosen in the 'config.xml' file. Program commits insert automatically then displays the updated row from the database, rollback button available(not implemented yet).");
      GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblMessageThisVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(152))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 89, Short.MAX_VALUE)
									.addGap(33))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnCommit, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
									.addGap(35))))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textField, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
									.addGap(46)
									.addComponent(btnEnter, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)))
							.addGap(35))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMessageThisVersion)
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEnter, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
					.addGap(6)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCommit)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)))
					.addContainerGap())
		);
        
        table = new JTable();
        //table.setModel(new DefaultTableModel());
        scrollPane.setViewportView(table);
        frame.getContentPane().setLayout(groupLayout);
    }
}
