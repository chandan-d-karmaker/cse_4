package test_ui;

import controller.ProductController;
import model.Product;
import view.Money;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * ProductPanel (VIEW)
 * -------------------
 * Product management screen (Admin): add, edit, delete, view, search.
 *
 * HOW TO USE: click a row in the table -> its data appears in the boxes below.
 * Change the boxes and click Update, or click Delete.
 * To add a new product, type in the boxes and click Add.
 */
public class ProductPanel extends JPanel {

    private final ProductController productController = new ProductController();

    // The products currently shown in the table (row 0 of the table = index 0 of this list).
    private ArrayList<Product> shownProducts = new ArrayList<>();

    // Search bar
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnShowAll;

    // Table
    private JTable tblProducts;
    private DefaultTableModel tableModel;

    // Product details boxes
    private JTextField txtName;
    private JTextField txtCategory;
    private JTextField txtPrice;
    private JTextField txtStock;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public ProductPanel() {
        buildScreen();
        refresh();
    }

    // =====================================================
    //  BUILD THE SCREEN
    // =====================================================
    private void buildScreen() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----- Top: search bar -----
        txtSearch = new JTextField(20);
        txtSearch.addActionListener(e -> search());          // pressing ENTER searches
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> search());
        btnShowAll = new JButton("Show All");
        btnShowAll.addActionListener(e -> refresh());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by name or category:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        // ----- Middle: table -----
        String[] columns = {"ID", "Name", "Category", "Price", "Stock", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            // Makes the table cells read-only (the user must use the boxes below).
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts = new JTable(tableModel);
        tblProducts.setRowHeight(24);
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // When a row is clicked, copy its data into the boxes.
        tblProducts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillBoxesFromSelectedRow();
            }
        });

        // ----- Bottom: details boxes + buttons -----
        txtName = new JTextField();
        txtCategory = new JTextField();
        txtPrice = new JTextField();
        txtStock = new JTextField();

        // Row 1 = four labels, row 2 = four boxes
        JPanel boxesPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        boxesPanel.add(new JLabel("Name"));
        boxesPanel.add(new JLabel("Category"));
        boxesPanel.add(new JLabel("Price"));
        boxesPanel.add(new JLabel("Stock Quantity"));
        boxesPanel.add(txtName);
        boxesPanel.add(txtCategory);
        boxesPanel.add(txtPrice);
        boxesPanel.add(txtStock);

        btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> {
            tblProducts.clearSelection();
            clearBoxes();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel detailsPanel = new JPanel(new BorderLayout(5, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        detailsPanel.add(boxesPanel, BorderLayout.CENTER);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(tblProducts), BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
    }

    // =====================================================
    //  LOADING DATA
    // =====================================================

    // Reloads ALL products from the database.
    public void refresh() {
        txtSearch.setText("");
        showProducts(productController.getAllProducts());
    }

    private void search() {
        showProducts(productController.searchProducts(txtSearch.getText()));
    }

    // Puts a list of products into the table.
    private void showProducts(ArrayList<Product> list) {
        if (!productController.getErrorMessage().isEmpty()) {
            showError(productController.getErrorMessage());
        }

        shownProducts = list;
        tableModel.setRowCount(0);                 // remove all old rows

        for (Product p : list) {
            String category = (p.getCategory() == null) ? "" : p.getCategory();
            String status = p.isLowStock() ? "LOW STOCK" : "OK";

            tableModel.addRow(new Object[]{
                p.getId(), p.getName(), category,
                Money.format(p.getPrice()), p.getStockQty(), status
            });
        }
        clearBoxes();
    }

    // Copies the selected table row into the boxes.
    private void fillBoxesFromSelectedRow() {
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            return;                                // nothing selected
        }
        Product p = shownProducts.get(row);
        txtName.setText(p.getName());
        txtCategory.setText(p.getCategory() == null ? "" : p.getCategory());
        txtPrice.setText(Money.format(p.getPrice()));
        txtStock.setText(String.valueOf(p.getStockQty()));
    }

    private void clearBoxes() {
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtStock.setText("");
    }

    // =====================================================
    //  BUTTON ACTIONS
    // =====================================================

    private void addProduct() {
        Product product = readProductFromBoxes();
        if (product == null) {
            return;                                // a message was already shown
        }

        if (productController.addProduct(product)) {
            showInfo("Product added successfully.");
            refresh();
        } else {
            showError(productController.getErrorMessage());
        }
    }

    private void updateProduct() {
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            showError("Please click a product in the table first.");
            return;
        }
        Product product = readProductFromBoxes();
        if (product == null) {
            return;
        }
        product.setId(shownProducts.get(row).getId());     // which product to update

        if (productController.updateProduct(product)) {
            showInfo("Product updated successfully.");
            refresh();
        } else {
            showError(productController.getErrorMessage());
        }
    }

    private void deleteProduct() {
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            showError("Please click a product in the table first.");
            return;
        }
        Product selected = shownProducts.get(row);

        int answer = JOptionPane.showConfirmDialog(this,
                "Delete the product \"" + selected.getName() + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        if (productController.deleteProduct(selected.getId())) {
            showInfo("Product deleted.");
            refresh();
        } else {
            showError(productController.getErrorMessage());
        }
    }

    // =====================================================
    //  HELPERS
    // =====================================================

    // Reads the four boxes and makes a Product object.
    // Returns null (after showing a message) if price or stock is not a number.
    private Product readProductFromBoxes() {
        double price;
        int stock;

        try {
            price = Double.parseDouble(txtPrice.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Price must be a number, for example 25.50");
            return null;
        }

        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Stock quantity must be a whole number, for example 40");
            return null;
        }

        // The controller checks the rest (empty name, price <= 0, and so on).
        return new Product(txtName.getText(), txtCategory.getText(), price, stock);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Done", JOptionPane.INFORMATION_MESSAGE);
    }
}