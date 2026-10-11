package model;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int stockQty;

    public Product() {
    }

    public Product(String name, String category, double price, int stockQty) {
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

    public boolean isLowStock() {
        return stockQty <= 10;
    }
}
