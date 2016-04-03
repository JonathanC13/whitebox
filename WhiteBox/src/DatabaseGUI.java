import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

public class DatabaseGUI {
    protected LinkedList<OperationType> opeartion_list;
    private JFrame frame;
    private JTextField textField_1;
    protected JTextField textField_2;
    protected JTextArea textArea;
    protected JTextArea textArea_1;
    protected JTable table; 
    protected int operation = 1;
    protected int Table_names = 1;
 
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DatabaseGUI window = new DatabaseGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the appl  ication.
     */
    public DatabaseGUI() {
        initialize();
        opeartion_list = new LinkedList<OperationType>();
       
        
        //opeartion_list.add(new OperationType());
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        Customer_Database_INFO database_info = new Customer_Database_INFO();
        frame = new JFrame();
        //frame.setBounds(100, 100, 780, 470);
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setMinimumSize(new Dimension(800, 500));
        JLabel lblInputAcceptsWild = new JLabel("Input accepts wild card, ex S% mean all entries that start with S");
        
        textField_1 = new JTextField();
        textField_1.setEditable(false);
        textField_1.setColumns(10);
        
        JButton btnApplyFilters = new JButton("Apply Filters");
        ActionListener c = new enterOperation(this);
        btnApplyFilters.addActionListener(c);
        
        JButton btnCancel = new JButton("Cancal");

        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {       
                
                Operations_Display op = new Customer_Database_Controller();
                op.close();
                frame.dispose();
               
            }
        });
        JButton btnClearAll = new JButton("Clear All");
        btnClearAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {       
                opeartion_list = new LinkedList<OperationType>();
                textArea.setText("");
                
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        
        textArea_1 = new JTextArea();
        textArea_1.setEditable(false);
        
       scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
       GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(160)
							.addComponent(lblInputAcceptsWild))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnApplyFilters, GroupLayout.PREFERRED_SIZE, 91, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnClearAll, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblInputAcceptsWild)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnCancel)
							.addComponent(btnClearAll)
							.addComponent(btnApplyFilters))
						.addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);

   
        table = new JTable();
       
        table.setModel(new DefaultTableModel(
            new Object[][] {},database_info.Column_names
        ));
        scrollPane_1.setViewportView(table);
        JPanel panel_1 = new JPanel();
        scrollPane.setViewportView(panel_1);
        panel_1.setForeground(Color.BLACK);
        panel_1.setBackground(Color.YELLOW);
        //-----------------------------------------------------------ComboBOX-------------------------------------- -------------//


        final JComboBox<String> comboBox_1 = new JComboBox<String>(database_info.getCol(Table_names));
        comboBox_1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                operation = comboBox_1.getSelectedIndex()+1;
            }
        }); 
        final JComboBox<String> comboBox = new JComboBox<String>(database_info.Table_names);
        comboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) { 
                 Table_names = comboBox.getSelectedIndex()+1;
                 String[] s = database_info.getCol(Table_names);
                 DefaultComboBoxModel<String> data_mode = new DefaultComboBoxModel<String>(s);
                 comboBox_1.setModel(data_mode);
 
            }
        });
        //-----------------------------------------------------------ComboBOX---------------------------------------------------//
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        String s = textField_2.getText();
        JButton btnAdd = new JButton("Add");
        ActionListener a = new addOperation(this);
        btnAdd.addActionListener(a);
        JButton button_2 = new JButton("Delete");
        ActionListener b = new removeOperation(this);
        button_2.addActionListener(b);
        
        
        
        
       GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
            gl_panel_1.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_panel_1.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
                        .addComponent(textArea, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                        .addGroup(gl_panel_1.createSequentialGroup()
                            .addComponent(comboBox, 0, 97, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(comboBox_1, 0, 117, Short.MAX_VALUE)
                            .addGap(18)
                            .addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(button_2, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)))
                    .addGap(15))
        );
        gl_panel_1.setVerticalGroup(
            gl_panel_1.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_1.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
                        .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAdd)
                        .addComponent(button_2)
                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(textArea, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addContainerGap())
        );
        
        /*
        ButtonADD a = new ButtonADD(panel_1, gl_panel_1,comboBox_1);
        ButtonREMOVE b = new ButtonREMOVE(panel_1, btnAdd ,button_2);
        btnAdd.addActionListener(a);
        button_2.addActionListener(b);*/
        
        
        
        panel_1.setLayout(gl_panel_1);
        frame.getContentPane().setLayout(groupLayout);
    }
}
