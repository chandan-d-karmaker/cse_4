/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller_sms;
import model_sms.user_model;
/**
 *
 * @author Md. Riadul Islam
 */
public class user_con extends user_model{
    private String id,pass,name,phone,type;
    
    public void user_set(String id,String pass,String name,String phone,String type)
    {
        this.id=id;
        this.pass=pass;
        this.name=name;
        this.phone=phone;
        this.type=type;
        insert_data(id,pass,name,phone,type);
        //System.out.println(this.name);
    }
    
    public Object[] uesr_get()
    {
        return new Object[]{id,pass,name,phone,type};
    }
    
}
