/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sms_model;
import com.mysql.cj.protocol.Resultset;
import java.sql.*;

/**
 *
 * @author Md. Riadul Islam
 */
public class user_model {
    
    public void insert_user(String id,String pass, String name,String type)
    {
        String url="jdbc:mysql://localhost:3306/mysql";
        String user="root";
        String pass1="root";
        try {
            Connection con=DriverManager.getConnection(url, user, pass1);
            Statement st=con.createStatement();
            st.execute("insert into sms_a.user_data value(1003,\"1234\",\""+name+"\",\"Student\");");
            Resultset rs=st.executeQuery("seleet * for usgu")

            
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        
        
    }
    
}
