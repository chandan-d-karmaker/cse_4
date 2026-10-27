package sms_view;

import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import sms_con.product_con;
import sms_con.sale_con;

/** Billing screen: search product -> add to cart -> complete sale -> receipt. */
public class pos_view extends JFrame {

    private final product_con pc = new product_con();
    private final sale_con sc = new sale_con();

    private final JTextField tfSearch = new JTextField(14);
    private final JSpinner spQty = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final JTextField tfDiscount = new JTextField("0", 6);
    private final JComboBox<String> cbPay = new JComboBox<>(new String[]{"Cash", "Card"});
    private final JLabel lblSub = new JLabel("Subtotal: 0.00");
    private final JLabel lblTax = new JLabel("Tax (5%): 0.00");
    private final JLabel lblTotal = new JLabel("Total: 0.00");

    private final DefaultTableModel prodModel =
        new DefaultTableModel(new String[]{"id", "name", "category", "price", "stock"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    private final DefaultTableModel cartModel =
        new DefaultTableModel(new String[]{"id", "name", "price", "qty", "line total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    private final JTable prodTable = new JTable(prodModel);
    private final JTable cartTable = new JTable(cartModel);

    public pos_view() {
        setTitle("POS - New Sale");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // search bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search product")); top.add(tfSearch);
        JButton go = new JButton("Search");
        go.addActionListener(e -> load_products());
        tfSearch.addActionListener(e -> load_products());
        top.add(go);

        // left: products
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createTitledBorder("Products"));
        left.add(new JScrollPane(prodTable), BorderLayout.CENTER);
        JPanel addBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addBar.add(new JLabel("Qty")); addBar.add(spQty);
        JButton addBtn = new JButton("Add to cart");
        addBtn.addActionListener(e -> add_to_cart());
        addBar.add(addBtn);
        left.add(addBar, BorderLayout.SOUTH);

        // right: cart
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Cart"));
        right.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        JButton remove = new JButton("Remove selected");
        remove.addActionListener(e -> remove_from_cart());
        JPanel rm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rm.add(remove);
        right.add(rm, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.5);

        // bottom: totals + checkout
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 8));
        bottom.add(new JLabel("Discount")); bottom.add(tfDiscount);
        bottom.add(new JLabel("Payment")); bottom.add(cbPay);
        bottom.add(lblSub); bottom.add(lblTax); bottom.add(lblTotal);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 15f));
        JButton done = new JButton("Complete Sale");
        done.addActionListener(e -> checkout());
        bottom.add(done);
        tfDiscount.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refresh_totals(); }
            public void removeUpdate(DocumentEvent e) { refresh_totals(); }
            public void changedUpdate(DocumentEvent e) { refresh_totals(); }
        });

        add(top, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setSize(1050, 520);
        setLocationRelativeTo(null);
        load_products();
    }

    private void load_products() {
        try {
            prodModel.setRowCount(0);
            for (Object[] r : pc.get_products(tfSearch.getText())) prodModel.addRow(r);
        } catch (Exception ex) { error(ex); }
    }

    private void add_to_cart() {
        int row = prodTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first."); return; }
        Object[] product = new Object[5];
        for (int i = 0; i < 5; i++) product[i] = prodModel.getValueAt(row, i);
        try {
            sc.add_to_cart(product, (Integer) spQty.getValue());
            refresh_cart();
        } catch (Exception ex) { error(ex); }
    }

    private void remove_from_cart() {
        int row = cartTable.getSelectedRow();
        if (row < 0) return;
        sc.remove_from_cart(row);
        refresh_cart();
    }

    private void refresh_cart() {
        cartModel.setRowCount(0);
        for (Object[] r : sc.get_cart()) {
            BigDecimal line = ((BigDecimal) r[2]).multiply(BigDecimal.valueOf((Integer) r[3]));
            cartModel.addRow(new Object[]{r[0], r[1], r[2], r[3], line});
        }
        refresh_totals();
    }

    private void refresh_totals() {
        BigDecimal sub = sc.subtotal();
        lblSub.setText("Subtotal: " + sub);
        try {
            BigDecimal d = sc.parse_discount(tfDiscount.getText(), sub);
            BigDecimal tax = sc.tax(sub, d);
            lblTax.setText("Tax (5%): " + tax);
            lblTotal.setText("Total: " + sc.total(sub, d, tax));
        } catch (IllegalArgumentException ex) {
            lblTax.setText("Tax (5%): -");
            lblTotal.setText("Total: invalid discount");
        }
    }

    private void checkout() {
        try {
            int saleId = sc.checkout(tfDiscount.getText(), (String) cbPay.getSelectedItem());
            show_receipt(saleId);
            tfDiscount.setText("0");
            refresh_cart();
            load_products();          // stock has changed
        } catch (Exception ex) { error(ex); }
    }

    private void show_receipt(int saleId) throws java.sql.SQLException {
        JTextArea area = new JTextArea(sc.build_receipt(saleId));
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Receipt", JOptionPane.PLAIN_MESSAGE);
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
