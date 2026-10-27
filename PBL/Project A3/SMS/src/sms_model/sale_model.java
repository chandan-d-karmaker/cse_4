package sms_model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** All SQL for sales and sale_items. */
public class sale_model {

    /**
     * Saves a sale and reduces stock in ONE JDBC transaction.
     * items: each row is {productId (Integer), qty (Integer), unitPrice (BigDecimal)}.
     * If anything fails (e.g. not enough stock) everything is rolled back.
     * Returns the new sale id.
     */
    public int save_sale(int cashierId, List<Object[]> items, BigDecimal subtotal,
                         BigDecimal discount, BigDecimal tax, BigDecimal total,
                         String mode) throws SQLException {
        Connection con = null;
        try {
            con = db_connection.get();
            con.setAutoCommit(false);

            int saleId;
            String insSale = "INSERT INTO sales(cashier_id,subtotal,discount,tax,total_amount,payment_mode) "
                           + "VALUES(?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(insSale, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cashierId);
                ps.setBigDecimal(2, subtotal);
                ps.setBigDecimal(3, discount);
                ps.setBigDecimal(4, tax);
                ps.setBigDecimal(5, total);
                ps.setString(6, mode);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    saleId = rs.getInt(1);
                }
            }

            try (PreparedStatement stock = con.prepareStatement(
                         "UPDATE products SET stock_qty = stock_qty - ? WHERE id=? AND stock_qty >= ?");
                 PreparedStatement item = con.prepareStatement(
                         "INSERT INTO sale_items(sale_id,product_id,quantity,unit_price) VALUES(?,?,?,?)")) {
                for (Object[] it : items) {
                    int pid = (Integer) it[0];
                    int qty = (Integer) it[1];
                    stock.setInt(1, qty);
                    stock.setInt(2, pid);
                    stock.setInt(3, qty);
                    if (stock.executeUpdate() == 0) {
                        throw new SQLException("Not enough stock for product id " + pid);
                    }
                    item.setInt(1, saleId);
                    item.setInt(2, pid);
                    item.setInt(3, qty);
                    item.setBigDecimal(4, (BigDecimal) it[2]);
                    item.executeUpdate();
                }
            }
            con.commit();
            return saleId;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    /** Rows of {id, date, cashier, subtotal, discount, tax, total, payment}. Newest first. */
    public List<Object[]> get_all_sales() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.id,s.sale_date,u.username,s.subtotal,s.discount,s.tax,s.total_amount,s.payment_mode "
                   + "FROM sales s JOIN users u ON u.id=s.cashier_id ORDER BY s.id DESC";
        try (Connection con = db_connection.get();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getInt(1), rs.getTimestamp(2), rs.getString(3),
                        rs.getBigDecimal(4), rs.getBigDecimal(5), rs.getBigDecimal(6),
                        rs.getBigDecimal(7), rs.getString(8)});
            }
        }
        return list;
    }

    /** One sale header, same column order as get_all_sales(). */
    public Object[] get_sale(int saleId) throws SQLException {
        for (Object[] row : get_all_sales()) {
            if ((Integer) row[0] == saleId) return row;
        }
        return null;
    }

    /** Rows of {product name, qty, unit price, line total}. */
    public List<Object[]> get_sale_items(int saleId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.name,si.quantity,si.unit_price,(si.quantity*si.unit_price) "
                   + "FROM sale_items si JOIN products p ON p.id=si.product_id WHERE si.sale_id=?";
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{rs.getString(1), rs.getInt(2),
                                          rs.getBigDecimal(3), rs.getBigDecimal(4)});
                }
            }
        }
        return list;
    }

    public BigDecimal today_total() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount),0) FROM sales WHERE DATE(sale_date)=CURDATE()";
        try (Connection con = db_connection.get();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getBigDecimal(1);
        }
    }
}
