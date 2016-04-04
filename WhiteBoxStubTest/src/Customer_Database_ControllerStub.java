import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Path;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Properties;

public class Customer_Database_ControllerStub extends Customer_Database_Controller{
     public String BuildFilterSQL(LinkedList<OperationType> filList){
         for(OperationType o: filList){
              System.out.println("table: "+o.getTable()+ " operation:" + o.getOperation()+ " input:" + o.getInput());
         }
         return "Succes";
     }
     public void SelectOperation(int Operation){
         System.out.println("You have Selected: " + Operation);
        }
     public String UploadFileDirectory(String s){
          System.out.println("You have Upload: " + s);
               return "Succes";
        }
     public void close(){
          System.out.println("Close");
         
        }
     public void commitTO(){
           System.out.println("Commited");
         
        }
     public ResultSet getResult(){
          System.out.println("Result Set Here");
         return null;
        }
}

