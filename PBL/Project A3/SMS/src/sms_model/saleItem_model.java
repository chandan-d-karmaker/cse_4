/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sms_model;

/**
 *
 * @author Aditya
 */
public class saleItem_model {
    // ---------- Attributes (same as the UML diagram) ----------
    private int id;
    private int saleId;
    private int productId;
    private int quantity;
    private double unitPrice;   
 
   
    private String productName;
 
   
    public saleItem_model() {
    }
 
  
    public saleItem_model(int productId, String productName, int quantity, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
 
    // ---------- Getters and Setters ----------
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public int getSaleId() {
        return saleId;
    }
 
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
 
    public int getProductId() {
        return productId;
    }
 
    public void setProductId(int productId) {
        this.productId = productId;
    }
 
    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
 
    public double getUnitPrice() {
        return unitPrice;
    }
 
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
 
    public String getProductName() {
        return productName;
    }
 
    public void setProductName(String productName) {
        this.productName = productName;
    }
 
    
    public double getLineTotal() {
        double total = quantity * unitPrice;
        return Math.round(total * 100.0) / 100.0;
    }
}
