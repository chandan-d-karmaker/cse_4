/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sms_con;
import sms_model.user_model;

/**
 *
 * @author Md. Riadul Islam
 */
public class user_con extends user_model{
    private String id,pass,name,type;
    
    public void set_user(String id,String pass, String name,String type)
    {
         this.id=id;
         this.pass=pass;
         this.name=name;
         this.type=type;
         insert_user(id, pass,  name, type);
         //System.out.println(name);
    }
    public Object[] get_user()
    {
       return new Object[]{id,pass,name,type};
    }
    
}
