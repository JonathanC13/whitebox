
/**
 * Write a description of class OperationType here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class OperationType
{
    private String input;
    private int operation;
    private boolean thelast;
    private int table;
    public OperationType(){
        this("",0,0);
    }
    public OperationType(String input, int operation,int table){
        this(input,operation,table,false);
    }
    public OperationType(String input, int operation,int table, boolean thelast){
        this.input = input;
        this.operation = operation;
        this.table = table;
        this.thelast = thelast;
    }
    public String getInput(){
        return input;
    }
    public int getOperation(){
        return operation;
    }
    public int getTable(){
        return table;
    }
    public boolean isThelast(){
        return thelast;
    }
    public void setInput(String s){
        input = s;
    }
    public void setOperation(int i){
        operation = i;
    }
    public void setThelast(boolean b){
        thelast = b;
    }
    public void setTable(){
        this.table = table;
    }
}
