package sms_view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import sms_con.product_con;

/** Admin screen: add / edit / delete / search products. */
public class product_view extends JFrame {

    private final product_con pc = new product_con();
    private final JTextField tfName = new JTextField(12);
    private final JTextField tfCategory = new JTextField(10);
    private final JTextField tfPrice = new JTextField(7);
    private final JTextField tfStock = new JTextField(5);
    private final JTextField tfSearch = new JTextField(12);
    private final DefaultTableModel model =
        new DefaultTableModel(new String[]{"id", "name", "category", "price", "stock"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    private final JTable table = new JTable(model);

    public product_view() {
        setTitle("Manage Products");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Name")); form.add(tfName);
        form.add(new JLabel("Category")); form.add(tfCategory);
        form.add(new JLabel("Price")); form.add(tfPrice);
        form.add(new JLabel("Stock")); form.add(tfStock);
        JButton add = new JButton("Add");
        JButton upd = new JButton("Update");
        JButton del = new JButton("Delete");
        JButton clr = new JButton("Clear");
        add.addActionListener(e -> add_product());
        upd.addActionListener(e -> update_product());
        del.addActionListener(e -> delete_product());
        clr.addActionListener(e -> clear_form());
        form.add(add); form.add(upd); form.add(del); form.add(clr);

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search")); search.add(tfSearch);
        JButton go = new JButton("Search");
        go.addActionListener(e -> load_table());
        tfSearch.addActionListener(e -> load_table());
        search.add(go);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.NORTH);
        north.add(search, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fill_form();
        });

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setSize(900, 480);
        setLocationRelativeTo(null);
        load_table();
    }

    private void load_table() {
        try {
            model.setRowCount(0);
            for (Object[] r : pc.get_products(tfSearch.getText())) model.addRow(r);
        } catch (Exception ex) { error(ex); }
    }

    private void fill_form() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        tfName.setText(String.valueOf(model.getValueAt(row, 1)));
        Object cat = model.getValueAt(row, 2);
        tfCategory.setText(cat == null ? "" : cat.toString());
        tfPrice.setText(String.valueOf(model.getValueAt(row, 3)));
        tfStock.setText(String.valueOf(model.getValueAt(row, 4)));
    }

    private void clear_form() {
        tfName.setText(""); tfCategory.setText(""); tfPrice.setText(""); tfStock.setText("");
        table.clearSelection();
    }

    private void add_product() {
        try {
            pc.set_product(tfName.getText(), tfCategory.getText(), tfPrice.getText(), tfStock.getText());
            clear_form(); load_table();
        } catch (Exception ex) { error(ex); }
    }

    private void update_product() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first."); return; }
        try {
            pc.edit_product((Integer) model.getValueAt(row, 0), tfName.getText(), tfCategory.getText(),
                            tfPrice.getText(), tfStock.getText());
            clear_form(); load_table();
        } catch (Exception ex) { error(ex); }
    }

    private void delete_product() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first."); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            pc.remove_product((Integer) model.getValueAt(row, 0));
            clear_form(); load_table();
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "This product cannot be deleted because it already appears in past sales.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
