package sms_view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import sms_con.user_con;

/** Admin screen: add / delete users. */
public class user_view extends JFrame {

    private final user_con uc = new user_con();
    private final JTextField tfUser = new JTextField(10);
    private final JPasswordField pfPass = new JPasswordField(10);
    private final JComboBox<String> cbRole = new JComboBox<>(new String[]{"Cashier", "Admin"});
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"id", "username", "role"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public user_view() {
        setTitle("Manage Users");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Username")); form.add(tfUser);
        form.add(new JLabel("Password")); form.add(pfPass);
        form.add(new JLabel("Role"));     form.add(cbRole);
        JButton add = new JButton("Add");
        JButton del = new JButton("Delete selected");
        add.addActionListener(e -> add_user());
        del.addActionListener(e -> delete_user());
        form.add(add); form.add(del);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setSize(720, 360);
        setLocationRelativeTo(null);
        load_table();
    }

    private void load_table() {
        try {
            model.setRowCount(0);
            for (Object[] r : uc.get_users()) model.addRow(r);
        } catch (Exception ex) { error(ex); }
    }

    private void add_user() {
        try {
            uc.set_user(tfUser.getText(), new String(pfPass.getPassword()), (String) cbRole.getSelectedItem());
            tfUser.setText(""); pfPass.setText("");
            load_table();
        } catch (Exception ex) { error(ex); }
    }

    private void delete_user() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a user first."); return; }
        try {
            uc.remove_user((Integer) model.getValueAt(row, 0));
            load_table();
        } catch (Exception ex) { error(ex); }
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
