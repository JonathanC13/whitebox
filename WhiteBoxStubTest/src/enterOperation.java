import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
import java.sql.*;
/**
 * Write a description of class swag here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class enterOperation implements ActionListener
{
    //LinkedList<OperationType> list = new  LinkedList<OperationType>();
    String result;
    DatabaseGUI d;
    OperationType type;
    Operations_Display a;

    public enterOperation(DatabaseGUI d){
        this.d = d;
        a = new Customer_Database_ControllerStub();
    }
    
    public void actionPerformed(ActionEvent e){
         a.SelectOperation(2);
         done();
         result = a.BuildFilterSQL(d.opeartion_list); 
         d.textArea_1.setText(result);
         a.getResult();
         /*
         for(OperationType o: d.opeartion_list){
             System.out.println("Input: "+o.getInput() + ". Operation: " +o.getOperation() +". Last?: "+ o.isThelast());
            }*/
          //d.opeartion_list = new LinkedList<OperationType>();
          //d.textArea.setText("");
          //System.out.println("---------------------------");
         //d.table.setModel(setTable(a.getResult()));
      
          
          
          
          
    }
    public void done(){
        d.opeartion_list.getLast().setThelast(true);
        
    }
    public DefaultTableModel setTable(ResultSet rs){
        if(rs == null) return null;
        DefaultTableModel data_mode = new DefaultTableModel();;
        try {
          int var_count = rs.getMetaData().getColumnCount();
          String[] var = new String[var_count];
          for(int i = 1; i<= var_count; i++){
              //System.out.println("swag = "+rs.getMetaData().getColumnName(i));
              var[i-1] = rs.getMetaData().getColumnName(i);
          }
          //System.out.println(rs.next());
          data_mode.setColumnIdentifiers(var);
          while(rs.next()){
              //System.out.println("yo");
              String[] row = new String[var_count];
              for(int i = 1; i<= var_count; i++){
                  //System.out.println("yolo = "+rs.getMetaData().getColumnName(i));
                  row[i-1] = rs.getString(i);
              }
              data_mode.addRow(row);
            }
        } catch (SQLException ignore) {}
        return data_mode;
    }
}
