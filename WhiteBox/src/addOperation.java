import java.awt.event.*;
import java.util.*;
/**
 * Write a description of class swag here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class addOperation implements ActionListener
{
    //LinkedList<OperationType> list = new  LinkedList<OperationType>();
    DatabaseGUI d;
    OperationType type;
    public addOperation(DatabaseGUI d){
        this.d = d;
        
    }
    
    public void actionPerformed(ActionEvent e){
         type = new OperationType(d.textField_2.getText(),d.operation-1,d.Table_names-1);
         String s = "";
         if(!isExist(type))d.opeartion_list.add(type);
         for(OperationType o: d.opeartion_list){
             //s = s + "Input: "+o.getInput() + ". Operation: " +o.getOperation() +". Last?: "+ o.isThelast()+"\n";
             if(o != d.opeartion_list.getFirst()){s = s +", ";}
             s = s + "["+(o.getTable()+1) + ": " + (o.getOperation()+1) +": "+o.getInput()+"]";
            }
         System.out.println(s);
         d.textArea.setText(s);
         System.out.println("---------------------------");
    }
    public boolean isExist(OperationType o){
        String s = o.getInput();
        for(OperationType o1: d.opeartion_list){
            String s1 = o1.getInput();
            if(o.getOperation() == o1.getOperation() && s.equals(s1) && o.getTable() == o1.getTable()){
                return true;
            }
        }
        return false;
    }
}
