package sms_con;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sms_model.sale_model;

/** POS logic: cart, totals, checkout, receipt text. */
public class sale_con extends sale_model {

    public static final BigDecimal TAX_RATE = new BigDecimal("0.05");   // one tax rate: 5%

    /** Cart rows: {productId, name, unitPrice, qty}. */
    private final List<Object[]> cart = new ArrayList<>();

    public List<Object[]> get_cart() { return cart; }

    /** product = {id, name, category, price, stock} as returned by product_con.get_products(). */
    public void add_to_cart(Object[] product, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be at least 1.");
        int id = (Integer) product[0];
        int stock = (Integer) product[4];
        for (Object[] row : cart) {
            if ((Integer) row[0] == id) {
                int newQty = (Integer) row[3] + qty;
                if (newQty > stock) throw new IllegalArgumentException("Only " + stock + " in stock.");
                row[3] = newQty;
                return;
            }
        }
        if (qty > stock) throw new IllegalArgumentException("Only " + stock + " in stock.");
        cart.add(new Object[]{id, product[1], product[3], qty});
    }

    public void remove_from_cart(int index) { cart.remove(index); }

    public void clear_cart() { cart.clear(); }

    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Object[] r : cart) {
            sum = sum.add(((BigDecimal) r[2]).multiply(BigDecimal.valueOf((Integer) r[3])));
        }
        return sum;
    }

    /** Bill-level discount as an amount. Blank = 0. */
    public BigDecimal parse_discount(String text, BigDecimal subtotal) {
        if (text == null || text.isBlank()) return BigDecimal.ZERO;
        BigDecimal d;
        try {
            d = new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Discount must be a number.");
        }
        if (d.signum() < 0 || d.compareTo(subtotal) > 0) {
            throw new IllegalArgumentException("Discount must be between 0 and the subtotal.");
        }
        return d;
    }

    public BigDecimal tax(BigDecimal subtotal, BigDecimal discount) {
        return subtotal.subtract(discount).multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total(BigDecimal subtotal, BigDecimal discount, BigDecimal tax) {
        return subtotal.subtract(discount).add(tax);
    }

    /** Completes the sale (transaction-safe). Returns the new sale id. */
    public int checkout(String discountText, String mode) throws SQLException {
        if (cart.isEmpty()) throw new IllegalArgumentException("The cart is empty.");
        BigDecimal sub = subtotal();
        BigDecimal disc = parse_discount(discountText, sub);
        BigDecimal tax = tax(sub, disc);
        BigDecimal tot = total(sub, disc, tax);

        List<Object[]> items = new ArrayList<>();
        for (Object[] r : cart) items.add(new Object[]{r[0], r[3], r[2]});

        int id = save_sale(session.user_id, items, sub, disc, tax, tot, mode);
        cart.clear();
        return id;
    }

    public String build_receipt(int saleId) throws SQLException {
        Object[] h = get_sale(saleId);
        StringBuilder sb = new StringBuilder();
        sb.append("      SHOPPING MALL RECEIPT\n");
        sb.append("--------------------------------------\n");
        sb.append("Invoice #").append(h[0]).append("\n");
        sb.append("Date   : ").append(h[1]).append("\n");
        sb.append("Cashier: ").append(h[2]).append("\n");
        sb.append("--------------------------------------\n");
        sb.append(String.format("%-18s %4s %7s %8s%n", "Item", "Qty", "Price", "Total"));
        for (Object[] it : get_sale_items(saleId)) {
            sb.append(String.format("%-18.18s %4d %7.2f %8.2f%n", it[0], it[1], it[2], it[3]));
        }
        sb.append("--------------------------------------\n");
        sb.append(String.format("%-30s %8.2f%n", "Subtotal", h[3]));
        sb.append(String.format("%-30s %8.2f%n", "Discount", h[4]));
        sb.append(String.format("%-30s %8.2f%n", "Tax (5%)", h[5]));
        sb.append(String.format("%-30s %8.2f%n", "TOTAL", h[6]));
        sb.append("Payment: ").append(h[7]).append("\n");
        sb.append("\n      Thank you!\n");
        return sb.toString();
    }
}
