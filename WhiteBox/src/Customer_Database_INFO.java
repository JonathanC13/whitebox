import java.util.*;
public class Customer_Database_INFO {
    
    public String[] Table_names = {
        
        /* 0*/  "customer_info",
        /* 1*/  "payment_info",
        /* 2*/  "product_info",
        /* 3*/  "manufacturer_table",
        /* 4*/ "component_type_table"
        
    };
    
    // can put in xml file later, but since we are implementing limited functionality that doesn't pertain to modifying the database itself
    // it is not necessary.
    public String[] Column_names = {
            // customer_info
            /* 0*/  "All",
            /* 1*/  "Build_ID",
            /* 2*/  "Name",
            /* 3*/  "Email",
            /* 4*/  "Phone",
            /* 5*/  "Address",
            /* 6*/  "Delivery_Date",
            // payment_info
            /* 7*/  "All",
            /* 8*/  "Build_ID",
            /* 9*/  "Payment_method_use",
            /* 10*/  "Total_value",
            /* 11*/  "Delivery_confirmation_link",     
            // product_info
            /* 12*/  "All",
            /* 13*/ "Build_ID",
            /* 14*/ "Component_type",
            /* 15*/ "Manufacturer",
            /* 16*/ "Product_description",
            /* 17*/ "model_number",
            /* 18*/ "Serial_number",
            /* 19*/ "Rebate_value",
            /* 20*/ "Price",
            /* 21*/ "Warranty_period",
            /* 22*/ "Warranty_expire",
            /* 23*/ "Invoice_date",
            /* 24*/ "Invoice_number",
            /* 25*/ "Sale_order_number",
            /* 26*/ "Item_SKU",
            // manufacturer_table
            /* 27*/  "All",
            /* 28*/ "Manufacturer",
            /* 29*/ "Manufacturer_name",
            
            // component_type_table
            /* 30*/  "All",
            /* 31*/ "Component_type",
            /* 32*/ "Component_type_description"
            
    };
  
    public String[] neww; 
    public Customer_Database_INFO(){
        /*int count = 0;
        int thread = 0;
        String[] newww = new String[Column_names.length];
        for(String s: Column_names){
              count++;
              newww[count-1] = s;
              if(s.equals("*")){
                  thread++;
                  if(thread==2) break;
                }
          }
        neww = new String[count];
         for(int i = 0; i < count; i++){
             neww[i] = newww[i];
         }
           */
    }
    public String[] getCol(int i){
        
        String []newww = new String[Column_names.length];
        String s1 ="";
        int count = 0;
        int  tail= 0;
        int head= 0; 
        for(String s: Column_names){
            count++;
            s1 = s;
            if(s1.equals("All")){ head++; if(head <= i)tail = count;}
            if(head > i){count--; break;}
            if(head == i) {  System.out.println("swag = "+ count+"tail = "+ tail + " "+ s1); newww[count - tail] = s1;}
        }
        count++;
        System.out.println("i = "+ i +"count = "+ count +"tail ="+tail);
        String[]result = new String[count - tail];
        for(int j = 0; j < count - tail; j++){
             System.out.println(newww[j]);
             result [j] = newww[j];
        }
         //System.out.println("done");
        return result ;
    }
    public int getOffSet(int table, int op){
        int count = 0;
        for(String s: Column_names){
            
            if(s.equals("All"))table--;
            if(table<0)break;
            count++;
        }
        return count+op;
        
    }
}

