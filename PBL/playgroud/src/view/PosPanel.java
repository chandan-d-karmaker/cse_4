package view;

import controller.ProductController;
import controller.SaleController;
import model.Product;
import model.Sale;
import model.SaleItem;
import model.User;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 * PosPanel (VIEW)
 * ---------------
 * The billing screen (Cashier).
 *
 *   LEFT  side: search the products, pick one, choose a quantity, click "Add to Cart".
 *   RIGHT side: the cart, discount, tax, total, payment mode, "Complete Sale".
 *
 * The cart is a Sale object (model). The SaleController does the checking
 * (enough stock? valid discount?) and the saving.
 */
public class PosPanel extends JPanel {

    private ProductController productController = new ProductController();
    private SaleController saleController = new SaleController();

    private User loggedInUser;
    private Sale cart;                                   // the current bill
    private ArrayList<Product> shownProducts = new ArrayList<Product>();

    // Left side
    private JTextField txtSearch;
    private JTable tblProducts;
    private DefaultTableModel productModel;
    private JSpinner spnQty;

    // Right side
    private JTable tblCart;
    private DefaultTableModel cartModel;
    private JLabel lblSubtotal;
    private JTextField txtDiscount;
    private JLabel lblTax;
    private JLabel lblTotal;
    private JComboBox<String> cmbPayment;

    public PosPanel() {
        this(new User("Cashier", 1));
    }

    public PosPanel(User user) {
        this.loggedInUser = user;
        newCart();
        buildScreen();
        refresh();
    }

    // Starts a fresh, empty bill for the logged-in cashier.
    private void newCart() {
        cart = new Sale();
        cart.setCashierId(loggedInUser.getId());
    }

    // =====================================================
    //  BUILD THE SCREEN
    // =====================================================
    private void buildScreen() {
        setLayout(new GridLayout(1, 2, 10, 10));          // two halves: left | right
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildLeftSide());
        add(buildRightSide());
    }

    // ----- LEFT: products -----
    private JPanel buildLeftSide() {
        // Search bar
        txtSearch = new JTextField(15);
        txtSearch.addActionListener(e -> search());        // ENTER searches
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> search());
        JButton btnShowAll = new JButton("Show All");
        btnShowAll.addActionListener(e -> refresh());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        // Product table
        String[] columns = {"ID", "Name", "Category", "Price", "Stock"};
        productModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts = new JTable(productModel);
        tblProducts.setRowHeight(24);
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Quantity + Add to Cart
        spnQty = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));   // start, min, max, step
        JButton btnAdd = new JButton("Add to Cart");
        btnAdd.addActionListener(e -> addToCart());

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addPanel.add(new JLabel("Quantity:"));
        addPanel.add(spnQty);
        addPanel.add(btnAdd);

        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.setBorder(BorderFactory.createTitledBorder("1. Choose Products"));
        left.add(searchPanel, BorderLayout.NORTH);
        left.add(new JScrollPane(tblProducts), BorderLayout.CENTER);
        left.add(addPanel, BorderLayout.SOUTH);
        return left;
    }

    // ----- RIGHT: cart and payment -----
    private JPanel buildRightSide() {
        // Cart table
        String[] columns = {"Product", "Qty", "Unit Price", "Line Total"};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCart = new JTable(cartModel);
        tblCart.setRowHeight(24);
        tblCart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnRemove = new JButton("Remove Selected Item");
        btnRemove.addActionListener(e -> removeSelectedItem());
        JButton btnClearCart = new JButton("Clear Cart");
        btnClearCart.addActionListener(e -> clearCart());

        JPanel cartButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cartButtons.add(btnRemove);
        cartButtons.add(btnClearCart);

        // Totals area
        lblSubtotal = new JLabel("0.00");
        lblTax = new JLabel("0.00");
        lblTotal = new JLabel("0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 22));

        txtDiscount = new JTextField("0.00", 8);
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(e -> applyDiscountFromBox());
        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        discountPanel.add(txtDiscount);
        discountPanel.add(btnApply);

        cmbPayment = new JComboBox<String>(new String[]{"Cash", "Card"});

        long taxPercent = Math.round(Sale.TAX_RATE * 100);

        JPanel totalsPanel = new JPanel(new GridLayout(5, 2, 5, 8));
        totalsPanel.add(new JLabel("Subtotal:"));
        totalsPanel.add(lblSubtotal);
        totalsPanel.add(new JLabel("Discount (amount):"));
        totalsPanel.add(discountPanel);
        totalsPanel.add(new JLabel("Tax (" + taxPercent + "%):"));
        totalsPanel.add(lblTax);
        totalsPanel.add(new JLabel("TOTAL TO PAY:"));
        totalsPanel.add(lblTotal);
        totalsPanel.add(new JLabel("Payment mode:"));
        totalsPanel.add(cmbPayment);

        JButton btnComplete = new JButton("Complete Sale");
        btnComplete.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnComplete.setPreferredSize(new Dimension(200, 45));
        btnComplete.addActionListener(e -> completeSale());
        JPanel completePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        completePanel.add(btnComplete);

        JPanel bottom = new JPanel(new BorderLayout(5, 10));
        bottom.add(totalsPanel, BorderLayout.CENTER);
        bottom.add(completePanel, BorderLayout.SOUTH);

        JPanel middle = new JPanel(new BorderLayout(5, 5));
        middle.add(new JScrollPane(tblCart), BorderLayout.CENTER);
        middle.add(cartButtons, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(5, 10));
        right.setBorder(BorderFactory.createTitledBorder("2. Cart and Payment"));
        right.add(middle, BorderLayout.CENTER);
        right.add(bottom, BorderLayout.SOUTH);
        return right;
    }

    // =====================================================
    //  LOADING DATA
    // =====================================================

    // Reloads the product list (fresh stock numbers). The cart is kept.
    public void refresh() {
        txtSearch.setText("");
        showProducts(productController.getAllProducts());
    }

    private void search() {
        showProducts(productController.searchProducts(txtSearch.getText()));
    }

    private void showProducts(ArrayList<Product> list) {
        if (!productController.getErrorMessage().isEmpty()) {
            showError(productController.getErrorMessage());
        }
        shownProducts = list;
        productModel.setRowCount(0);

        for (Product p : list) {
            String category = (p.getCategory() == null) ? "" : p.getCategory();
            productModel.addRow(new Object[]{
                p.getId(), p.getName(), category, Money.format(p.getPrice()), p.getStockQty()
            });
        }
    }

    // Redraws the cart table and the totals from the cart (Sale) object.
    private void refreshCart() {
        cartModel.setRowCount(0);

        for (SaleItem item : cart.getItems()) {
            cartModel.addRow(new Object[]{
                item.getProductName(), item.getQuantity(),
                Money.format(item.getUnitPrice()), Money.format(item.getLineTotal())
            });
        }

        lblSubtotal.setText(Money.format(cart.getSubtotal()));
        txtDiscount.setText(Money.format(cart.getDiscount()));
        lblTax.setText(Money.format(cart.getTax()));
        lblTotal.setText(Money.format(cart.getTotalAmount()));
    }

    // =====================================================
    //  BUTTON ACTIONS
    // =====================================================

    private void addToCart() {
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            showError("Please click a product in the list first.");
            return;
        }
        Product product = shownProducts.get(row);
        int quantity = (Integer) spnQty.getValue();

        // The controller checks the quantity and the stock.
        if (!saleController.addToCart(cart, product, quantity)) {
            showError(saleController.getErrorMessage());
            return;
        }
        spnQty.setValue(1);
        refreshCart();
    }

    private void removeSelectedItem() {
        int row = tblCart.getSelectedRow();
        if (row < 0) {
            showError("Please click an item in the cart first.");
            return;
        }
        SaleItem item = cart.getItems().get(row);
        saleController.removeFromCart(cart, item.getProductId());
        refreshCart();
    }

    private void clearCart() {
        if (cart.getItems().isEmpty()) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this, "Remove everything from the cart?",
                "Clear Cart", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            newCart();
            refreshCart();
        }
    }

    // Reads the discount box and gives it to the cart.
    // Returns true if the discount is valid.
    private boolean applyDiscountFromBox() {
        String text = txtDiscount.getText().trim();
        double discount = 0;

        if (!text.isEmpty()) {
            try {
                discount = Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                showError("Discount must be a number, for example 50 or 12.50");
                return false;
            }
        }

        boolean ok = saleController.applyDiscount(cart, discount);
        if (!ok) {
            showError(saleController.getErrorMessage());
        }
        refreshCart();            // shows the real discount of the cart again
        return ok;
    }

    private void completeSale() {
        if (cart.getItems().isEmpty()) {
            showError("The cart is empty. Please add some products first.");
            return;
        }

        // Use the discount typed in the box, even if "Apply" was not clicked.
        if (!applyDiscountFromBox()) {
            return;
        }

        cart.setPaymentMode((String) cmbPayment.getSelectedItem());

        // Save the sale (the controller uses a transaction and reduces the stock).
        int invoiceNumber = saleController.saveSale(cart);

        if (invoiceNumber == -1) {
            showError(saleController.getErrorMessage());
            refresh();            // stock may have changed, so show the newest numbers
            return;
        }

        // Success! Read the sale back from the database and show the receipt.
        Sale savedSale = saleController.getSaleById(invoiceNumber);
        if (savedSale != null) {
            showReceipt(savedSale);
        } else {
            JOptionPane.showMessageDialog(this, "Sale saved. Invoice number: " + invoiceNumber);
        }

        newCart();                // ready for the next customer
        refreshCart();
        refresh();                // product list now shows the reduced stock
    }

    // =====================================================
    //  RECEIPT
    // =====================================================

    private void showReceipt(Sale sale) {
        JTextArea area = new JTextArea(buildReceiptText(sale));
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(430, 420));

        JOptionPane.showMessageDialog(this, scroll,
                "Receipt - Invoice #" + sale.getId(), JOptionPane.PLAIN_MESSAGE);
    }

    private String buildReceiptText(Sale sale) {
        String line = "--------------------------------------\n";
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(sale.getSaleDate());

        StringBuilder sb = new StringBuilder();
        sb.append("       SHOPPING MALL SALES\n");
        sb.append(line);
        sb.append("Invoice : ").append(sale.getId()).append("\n");
        sb.append("Date    : ").append(date).append("\n");
        sb.append("Cashier : ").append(sale.getCashierName()).append("\n");
        sb.append(line);

        for (SaleItem item : sale.getItems()) {
            sb.append(item.getProductName()).append("\n");
            sb.append(String.format(Locale.US, "   %3d x %-10s %17s\n",
                    item.getQuantity(),
                    Money.format(item.getUnitPrice()),
                    Money.format(item.getLineTotal())));
        }

        sb.append(line);
        sb.append(String.format(Locale.US, "%-20s %17s\n", "Subtotal:", Money.format(sale.getSubtotal())));
        sb.append(String.format(Locale.US, "%-20s %17s\n", "Discount:", Money.format(sale.getDiscount())));
        sb.append(String.format(Locale.US, "%-20s %17s\n", "Tax:", Money.format(sale.getTax())));
        sb.append(String.format(Locale.US, "%-20s %17s\n", "TOTAL:", Money.format(sale.getTotalAmount())));
        sb.append(String.format(Locale.US, "%-20s %17s\n", "Paid by:", sale.getPaymentMode()));
        sb.append(line);
        sb.append("        Thank you! Come again.\n");
        return sb.toString();
    }

    // =====================================================
    //  HELPERS
    // =====================================================

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}