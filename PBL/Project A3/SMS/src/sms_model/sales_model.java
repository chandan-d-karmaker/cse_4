/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sms_model;

import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author Aditya
 */
public class sales_model {
    
    public static final double TAX_RATE = 0.10;
 
    
    private int id;                 
    private Date saleDate;
    private int cashierId;
    private double subtotal;        
    private double discount;        
    private double tax;
    private double totalAmount;    
    private String paymentMode;     
 
  
    private String cashierName;               
    private ArrayList<saleItem_model> items;    
 
   
    public sales_model() {
        items = new ArrayList<>();    
        discount = 0;
        paymentMode = "Cash";
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public Date getSaleDate() {
        return saleDate;
    }
 
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
 
    public int getCashierId() {
        return cashierId;
    }
 
    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }
 
    public double getSubtotal() {
        return subtotal;
    }
 
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
 
    public double getDiscount() {
        return discount;
    }
 
    public void setDiscount(double discount) {
        this.discount = discount;
    }
 
    public double getTax() {
        return tax;
    }
 
    public void setTax(double tax) {
        this.tax = tax;
    }
 
    public double getTotalAmount() {
        return totalAmount;
    }
 
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
 
    public String getPaymentMode() {
        return paymentMode;
    }
 
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
 
    public String getCashierName() {
        return cashierName;
    }
 
    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }
 
    public ArrayList<saleItem_model> getItems() {
        return items;
    }
 
    public void setItems(ArrayList<saleItem_model> items) {
        this.items = items;
    }
 
   
    public void addItem(saleItem_model newItem) {
        for (saleItem_model item : items) {
            if (item.getProductId() == newItem.getProductId()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }
 
    
    public void removeItem(int productId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProductId() == productId) {
                items.remove(i);
                return;
            }
        }
    }
 
   
    public int getQuantityInCart(int productId) {
        for (saleItem_model item : items) {
            if (item.getProductId() == productId) {
                return item.getQuantity();
            }
        }
        return 0;
    }
 

    public double calculateTotal() {
        double sum = 0;
        for (saleItem_model item : items) {
            sum = sum + item.getLineTotal();
        }
        subtotal = round(sum);
 
        double afterDiscount = subtotal - discount;
        tax = round(afterDiscount * TAX_RATE);
        totalAmount = round(afterDiscount + tax);
        return totalAmount;
    }
 
    
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
 
}
