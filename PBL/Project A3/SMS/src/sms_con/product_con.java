package sms_con;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import sms_model.product_model;

public class product_con extends product_model {

    private BigDecimal check_price(String text) {
        try {
            BigDecimal p = new BigDecimal(text.trim());
            if (p.signum() < 0) throw new NumberFormatException();
            return p;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price must be a number, e.g. 45.50");
        }
    }

    private int check_stock(String text) {
        try {
            int s = Integer.parseInt(text.trim());
            if (s < 0) throw new NumberFormatException();
            return s;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stock must be a whole number (0 or more).");
        }
    }

    private void check_name(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("Product name is required.");
    }

    public void set_product(String name, String category, String price, String stock) throws SQLException {
        check_name(name);
        insert_product(name.trim(), category.trim(), check_price(price), check_stock(stock));
    }

    public void edit_product(int id, String name, String category, String price, String stock) throws SQLException {
        check_name(name);
        update_product(id, name.trim(), category.trim(), check_price(price), check_stock(stock));
    }

    public void remove_product(int id) throws SQLException {
        delete_product(id);
    }

    public List<Object[]> get_products(String keyword) throws SQLException {
        return get_all_products(keyword.trim());
    }
}
