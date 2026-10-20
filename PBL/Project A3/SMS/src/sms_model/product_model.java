/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sms_model;

/**
 *
 * @author Aditya
 */
public class product_model {
    
    public static final int LOW_STOCK_LIMIT = 5;
 
    
    private int id;
    private String name;
    private String category;
    private double price;
    private int stockQty;
 
 
    public product_model() {
    }
 

    public product_model(int id, String name, String category, double price, int stockQty) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQty = stockQty;
    }
 
    
    public product_model(String name, String category, double price, int stockQty) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQty = stockQty;
    }
 
   
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getCategory() {
        return category;
    }
 
    public void setCategory(String category) {
        this.category = category;
    }
 
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }
 
    public int getStockQty() {
        return stockQty;
    }
 
    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }
 
    
    public void reduceStock(int qty) {
        if (qty <= 0) {
            System.out.println("Quantity must be more than 0.");
        }
        if (qty > stockQty) {
            System.out.println("Not enough stock for " + name + ".");
        }
        stockQty = stockQty - qty;
    }
 
 
    public boolean isLowStock() {
        return stockQty <= LOW_STOCK_LIMIT;
    }
}
