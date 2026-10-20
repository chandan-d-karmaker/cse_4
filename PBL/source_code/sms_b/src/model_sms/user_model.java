/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model_sms;
import java.sql.*;

/**
 *
 * @author Md. Riadul Islam
 */
public class user_model {
    
    public void insert_data(String id,String pass,String name,String phone,String type)
    {
        String url="jdbc:mysql://localhost:3306/mysql";
        String user="root";
        String pass1="root";
        try{
           Connection cn = DriverManager.getConnection(url,user,pass1);
           Statement st=cn.createStatement();
           st.execute("insert into sms_b.sms_uesr value(\""+id+"\",\"1234\",\"Nahid\",\"017232837\",\"admin\")");
         
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        
    }
    
}
