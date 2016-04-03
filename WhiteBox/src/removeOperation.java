
import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class removeOperation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class removeOperation implements ActionListener
{
    DatabaseGUI d;
    OperationType type;
    public removeOperation(DatabaseGUI d)
    {
        this.d = d;
    }
    public void actionPerformed(ActionEvent e){
         type = new OperationType(d.textField_2.getText(),d.operation,d.Table_names);
         if(remove(type))System.out.println("removed");
         String s = "";
         for(OperationType o: d.opeartion_list){
             if(o != d.opeartion_list.getFirst()){s = s +", ";}
             s = s + "["+o.getTable() + ": " + (o.getOperation()+1) +": "+o.getInput()+"]";
            }
          //System.out.println(s);  
          d.textArea.setText(s);  
          //System.out.println("---------------------------");
    }
    public boolean remove (OperationType o){
        String s = o.getInput();
        for(OperationType o1: d.opeartion_list){
            String s1 = o1.getInput();
            //System.out.println("o="+o.getOperation() + " s="+o1.getOperation());
            if(o.getOperation() == (o1.getOperation()+1) && s.equals(s1)){
                d.opeartion_list.remove(o1);
                return true;
            }
        }
        return false;
    }
}
