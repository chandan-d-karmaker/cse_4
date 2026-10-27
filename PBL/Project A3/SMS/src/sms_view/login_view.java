package sms_view;

import java.awt.*;
import javax.swing.*;
import sms_con.user_con;

public class login_view extends JFrame {

    private final JTextField tfUser = new JTextField(15);
    private final JPasswordField pfPass = new JPasswordField(15);

    public login_view() {
        setTitle("SMSMS - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Username"), g);
        g.gridx = 1; p.add(tfUser, g);
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Password"), g);
        g.gridx = 1; p.add(pfPass, g);

        JButton btn = new JButton("Login");
        btn.addActionListener(e -> do_login());
        g.gridx = 1; g.gridy = 2; g.anchor = GridBagConstraints.EAST; p.add(btn, g);
        getRootPane().setDefaultButton(btn);

        setContentPane(p);
        pack();
        setLocationRelativeTo(null);
    }

    private void do_login() {
        try {
            user_con uc = new user_con();
            if (uc.login(tfUser.getText().trim(), new String(pfPass.getPassword()))) {
                new main_view().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) { }
        EventQueue.invokeLater(() -> new login_view().setVisible(true));
    }
}
