package sms_model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** All SQL for the products table. */
public class product_model {

    public void insert_product(String name, String category, BigDecimal price, int stock) throws SQLException {
        String sql = "INSERT INTO products(name,category,price,stock_qty) VALUES(?,?,?,?)";
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setBigDecimal(3, price);
            ps.setInt(4, stock);
            ps.executeUpdate();
        }
    }

    public void update_product(int id, String name, String category, BigDecimal price, int stock) throws SQLException {
        String sql = "UPDATE products SET name=?, category=?, price=?, stock_qty=? WHERE id=?";
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setBigDecimal(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public void delete_product(int id) throws SQLException {
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Rows of {id, name, category, price, stock}. Empty keyword = everything. */
    public List<Object[]> get_all_products(String keyword) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT id,name,category,price,stock_qty FROM products "
                   + "WHERE name LIKE ? OR category LIKE ? ORDER BY name";
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3),
                                          rs.getBigDecimal(4), rs.getInt(5)});
                }
            }
        }
        return list;
    }
}
