package sms_view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import sms_con.sale_con;

/** Sales history + today's total. */
public class sales_history_view extends JFrame {

    private final sale_con sc = new sale_con();
    private final JLabel lblToday = new JLabel("Today's total: 0.00");
    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"invoice", "date", "cashier", "subtotal", "discount", "tax", "total", "payment"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public sales_history_view() {
        setTitle("Sales History");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        lblToday.setFont(lblToday.getFont().deriveFont(Font.BOLD, 15f));
        JButton refresh = new JButton("Refresh");
        JButton receipt = new JButton("View receipt");
        refresh.addActionListener(e -> load());
        receipt.addActionListener(e -> view_receipt());
        top.add(lblToday); top.add(refresh); top.add(receipt);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setSize(900, 460);
        setLocationRelativeTo(null);
        load();
    }

    private void load() {
        try {
            model.setRowCount(0);
            for (Object[] r : sc.get_all_sales()) model.addRow(r);
            lblToday.setText("Today's total: " + sc.today_total());
        } catch (Exception ex) { error(ex); }
    }

    private void view_receipt() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a sale first."); return; }
        try {
            JTextArea area = new JTextArea(sc.build_receipt((Integer) model.getValueAt(row, 0)));
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Receipt", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) { error(ex); }
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
