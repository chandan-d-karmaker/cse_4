package sms_view;

import java.awt.*;
import javax.swing.*;
import sms_con.session;

/** Home screen. Admin sees Products/Users/Sales History; Cashier sees POS/Sales History. */
public class main_view extends JFrame {

    public main_view() {
        setTitle("Shopping Mall Sales Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel hello = new JLabel("Logged in as " + session.username + " (" + session.role + ")",
                SwingConstants.CENTER);
        hello.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        if (session.is_admin()) {
            add_button(buttons, "Manage Products", () -> new product_view());
            add_button(buttons, "Manage Users", () -> new user_view());
        } else {
            add_button(buttons, "New Sale (POS)", () -> new pos_view());
        }
        add_button(buttons, "Sales History", () -> new sales_history_view());

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            session.clear();
            new login_view().setVisible(true);
            dispose();
        });
        buttons.add(logout);

        add(hello, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        pack();
        setSize(Math.max(getWidth(), 340), getHeight());
        setLocationRelativeTo(null);
    }

    private void add_button(JPanel panel, String text, java.util.function.Supplier<JFrame> opener) {
        JButton b = new JButton(text);
        b.addActionListener(e -> opener.get().setVisible(true));
        panel.add(b);
    }
}
