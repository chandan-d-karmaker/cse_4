package controller;

import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductController {
    private final List<Product> products = new ArrayList<>();
    private String errorMessage = "";

    public ProductController() {
        seedData();
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public ArrayList<Product> searchProducts(String keyword) {
        String query = (keyword == null) ? "" : keyword.trim();
        ArrayList<Product> result = new ArrayList<>();

        if (query.isEmpty()) {
            return new ArrayList<>(products);
        }

        for (Product product : products) {
            String name = product.getName() == null ? "" : product.getName();
            String category = product.getCategory() == null ? "" : product.getCategory();
            if (name.toLowerCase().contains(query.toLowerCase())
                    || category.toLowerCase().contains(query.toLowerCase())) {
                result.add(product);
            }
        }

        return result;
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            errorMessage = "Product cannot be null.";
            return false;
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            errorMessage = "Product name is required.";
            return false;
        }

        if (product.getPrice() <= 0) {
            errorMessage = "Price must be greater than zero.";
            return false;
        }

        if (product.getStockQty() < 0) {
            errorMessage = "Stock cannot be negative.";
            return false;
        }

        product.setId(nextId());
        products.add(product);
        errorMessage = "";
        return true;
    }

    public boolean updateProduct(Product product) {
        if (product == null) {
            errorMessage = "Product cannot be null.";
            return false;
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            errorMessage = "Product name is required.";
            return false;
        }

        if (product.getPrice() <= 0) {
            errorMessage = "Price must be greater than zero.";
            return false;
        }

        if (product.getStockQty() < 0) {
            errorMessage = "Stock cannot be negative.";
            return false;
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                errorMessage = "";
                return true;
            }
        }

        errorMessage = "The selected product was not found.";
        return false;
    }

    public boolean deleteProduct(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                products.remove(i);
                errorMessage = "";
                return true;
            }
        }

        errorMessage = "The selected product was not found.";
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private int nextId() {
        int maxId = 0;
        for (Product product : products) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }
        }
        return maxId + 1;
    }

    private void seedData() {
        addProduct(new Product("Laptop", "Electronics", 1200.00, 8));
        addProduct(new Product("Mouse", "Accessories", 25.50, 20));
        addProduct(new Product("Keyboard", "Accessories", 45.00, 12));
    }
}
