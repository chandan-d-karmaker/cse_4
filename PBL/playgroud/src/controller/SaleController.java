package controller;

import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.Sale;
import model.SaleItem;

public class SaleController {
    private final List<Sale> sales = new ArrayList<>();
    private String errorMessage = "";

    public boolean addToCart(Sale sale, Product product, int quantity) {
        if (sale == null) {
            errorMessage = "Cart is missing.";
            return false;
        }
        if (product == null) {
            errorMessage = "Please select a valid product.";
            return false;
        }
        if (quantity <= 0) {
            errorMessage = "Quantity must be greater than zero.";
            return false;
        }
        if (product.getStockQty() < quantity) {
            errorMessage = "Not enough stock for this product.";
            return false;
        }

        for (SaleItem item : sale.getItems()) {
            if (item.getProductId() == product.getId()) {
                int newQty = item.getQuantity() + quantity;
                if (product.getStockQty() < newQty) {
                    errorMessage = "Not enough stock for this product.";
                    return false;
                }
                item.setQuantity(newQty);
                errorMessage = "";
                return true;
            }
        }

        sale.addItem(new SaleItem(product.getId(), product.getName(), quantity, product.getPrice()));
        errorMessage = "";
        return true;
    }

    public void removeFromCart(Sale sale, int productId) {
        if (sale != null) {
            sale.removeItem(productId);
        }
    }

    public boolean applyDiscount(Sale sale, double discount) {
        if (sale == null) {
            errorMessage = "Cart is missing.";
            return false;
        }
        if (discount < 0) {
            errorMessage = "Discount cannot be negative.";
            return false;
        }
        if (discount > sale.getSubtotal()) {
            errorMessage = "Discount cannot exceed the subtotal.";
            return false;
        }
        sale.setDiscount(discount);
        errorMessage = "";
        return true;
    }

    public int saveSale(Sale sale) {
        if (sale == null) {
            errorMessage = "Cart is missing.";
            return -1;
        }
        if (sale.getItems().isEmpty()) {
            errorMessage = "The cart is empty.";
            return -1;
        }

        int nextId = sales.size() + 1;
        sale.setId(nextId);
        sales.add(sale);
        errorMessage = "";
        return nextId;
    }

    public Sale getSaleById(int saleId) {
        for (Sale sale : sales) {
            if (sale.getId() == saleId) {
                return sale;
            }
        }
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
