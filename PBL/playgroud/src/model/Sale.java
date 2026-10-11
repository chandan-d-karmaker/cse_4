package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
    public static final double TAX_RATE = 0.08;

    private int id;
    private int cashierId;
    private String cashierName = "Cashier";
    private String paymentMode = "Cash";
    private Date saleDate = new Date();
    private double discount = 0.0;
    private final List<SaleItem> items = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCashierId() {
        return cashierId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
        this.cashierName = "Cashier " + cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        double total = 0;
        for (SaleItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    public double getTax() {
        return (getSubtotal() - discount) * TAX_RATE;
    }

    public double getTotalAmount() {
        return getSubtotal() - discount + getTax();
    }

    public void addItem(SaleItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void removeItem(int productId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProductId() == productId) {
                items.remove(i);
                return;
            }
        }
    }
}
