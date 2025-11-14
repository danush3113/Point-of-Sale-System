import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class ManagerDashboard {
    private List<Product> products;
    private List<Cashier> cashiers;
    private static final String PRODUCTS_FILE = "products.txt";
    private static final String CASHIERS_FILE = "cashiers.txt";

    private DefaultListModel<String> productListModel;
    private DefaultListModel<String> cashierListModel;

    public ManagerDashboard() {
        try {
            products = FileManager.loadProducts(PRODUCTS_FILE);
            cashiers = FileManager.loadCashiers(CASHIERS_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
        productListModel = new DefaultListModel<>();
        cashierListModel = new DefaultListModel<>();
    }

    public void display() {
        JFrame frame = new JFrame("Manager Dashboard");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.decode("#F4F4F9"));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.decode("#6200EA"));
        JLabel titleLabel = new JLabel("Manager Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.decode("#FF4081"));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Roboto", Font.BOLD, 16));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            Login.main(null);
        });

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(logoutButton, BorderLayout.EAST);

        // Product Management Panel
        JPanel productPanel = new JPanel(new GridBagLayout());
        productPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#6200EA")), "Add New Product"));
        productPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblProductName = new JLabel("Product Name:");
        JTextField txtProductName = new JTextField(15);
        JLabel lblProductCategory = new JLabel("Product Category:");
        JTextField txtProductCategory = new JTextField(15);
        JLabel lblProductPrice = new JLabel("Product Price:");
        JTextField txtProductPrice = new JTextField(15);
        JLabel lblProductQuantity = new JLabel("Product Quantity:");
        JTextField txtProductQuantity = new JTextField(15);

        JButton btnAddProduct = new JButton("Add Product");
        btnAddProduct.setBackground(Color.decode("#03DAC5"));
        btnAddProduct.setFont(new Font("Roboto", Font.BOLD, 16));
        btnAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddProduct.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        productPanel.add(lblProductName, gbc);
        gbc.gridx = 1;
        productPanel.add(txtProductName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        productPanel.add(lblProductCategory, gbc);
        gbc.gridx = 1;
        productPanel.add(txtProductCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        productPanel.add(lblProductPrice, gbc);
        gbc.gridx = 1;
        productPanel.add(txtProductPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        productPanel.add(lblProductQuantity, gbc);
        gbc.gridx = 1;
        productPanel.add(txtProductQuantity, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        productPanel.add(btnAddProduct, gbc);

        // Add Product Action
        btnAddProduct.addActionListener(e -> {
            try {
                String name = txtProductName.getText();
                String category = txtProductCategory.getText();
                double price = Double.parseDouble(txtProductPrice.getText());
                int quantity = Integer.parseInt(txtProductQuantity.getText());

                products.add(new Product(name, category, price, quantity));
                FileManager.saveProducts(PRODUCTS_FILE, products);
                JOptionPane.showMessageDialog(null, "Product added successfully!");
                updateProductList();
                txtProductName.setText("");
                txtProductCategory.setText("");
                txtProductPrice.setText("");
                txtProductQuantity.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Product List Panel
        JPanel productListPanel = new JPanel(new BorderLayout());
        productListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#6200EA")), "Product List"));
        productListPanel.setBackground(Color.WHITE);

        JList<String> productList = new JList<>(productListModel);
        updateProductList();

        JScrollPane scrollPane = new JScrollPane(productList);
        JPanel productListButtons = new JPanel(new FlowLayout());
        JButton btnEditProduct = new JButton("Edit Product");
        JButton btnDeleteProduct = new JButton("Delete Product");

        btnEditProduct.setBackground(Color.decode("#03DAC5"));
        btnDeleteProduct.setBackground(Color.decode("#FF5722"));
        btnEditProduct.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDeleteProduct.setFont(new Font("Roboto", Font.BOLD, 14));

        btnEditProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditProduct.setFocusPainted(false);
        btnDeleteProduct.setFocusPainted(false);

        productListButtons.add(btnEditProduct);
        productListButtons.add(btnDeleteProduct);
        productListPanel.add(scrollPane, BorderLayout.CENTER);
        productListPanel.add(productListButtons, BorderLayout.SOUTH);

        // Cashier Management Panel
        JPanel cashierPanel = new JPanel(new GridBagLayout());
        cashierPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#6200EA")), "Add New Cashier"));
        cashierPanel.setBackground(Color.WHITE);

        JLabel lblCashierUsername = new JLabel("Username:");
        JTextField txtCashierUsername = new JTextField(15);
        JLabel lblCashierPassword = new JLabel("Password:");
        JPasswordField txtCashierPassword = new JPasswordField(15);
        JLabel lblCashierActive = new JLabel("Active (true/false):");
        JTextField txtCashierActive = new JTextField(15);

        JButton btnAddCashier = new JButton("Add Cashier");
        btnAddCashier.setBackground(Color.decode("#03DAC5"));
        btnAddCashier.setFont(new Font("Roboto", Font.BOLD, 16));
        btnAddCashier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddCashier.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        cashierPanel.add(lblCashierUsername, gbc);
        gbc.gridx = 1;
        cashierPanel.add(txtCashierUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cashierPanel.add(lblCashierPassword, gbc);
        gbc.gridx = 1;
        cashierPanel.add(txtCashierPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        cashierPanel.add(lblCashierActive, gbc);
        gbc.gridx = 1;
        cashierPanel.add(txtCashierActive, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cashierPanel.add(btnAddCashier, gbc);

        // Add Cashier Action
        btnAddCashier.addActionListener(e -> {
            try {
                String username = txtCashierUsername.getText();
                String password = new String(txtCashierPassword.getPassword());
                boolean isActive = Boolean.parseBoolean(txtCashierActive.getText());

                cashiers.add(new Cashier(username, password, isActive));
                FileManager.saveCashiers(CASHIERS_FILE, cashiers);
                JOptionPane.showMessageDialog(null, "Cashier added successfully!");
                txtCashierUsername.setText("");
                txtCashierPassword.setText("");
                txtCashierActive.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Cashier List Panel
        JPanel cashierListPanel = new JPanel(new BorderLayout());
        cashierListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#6200EA")), "Cashier List"));
        cashierListPanel.setBackground(Color.WHITE);

        JList<String> cashierList = new JList<>(cashierListModel);
        updateCashierList();

        JScrollPane cashierScrollPane = new JScrollPane(cashierList);
        JPanel cashierListButtons = new JPanel(new FlowLayout());
        JButton btnEditCashier = new JButton("Edit Cashier");
        JButton btnDeleteCashier = new JButton("Delete Cashier");

        btnEditCashier.setBackground(Color.decode("#03DAC5"));
        btnDeleteCashier.setBackground(Color.decode("#FF5722"));
        btnEditCashier.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDeleteCashier.setFont(new Font("Roboto", Font.BOLD, 14));

        btnEditCashier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteCashier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditCashier.setFocusPainted(false);
        btnDeleteCashier.setFocusPainted(false);

        cashierListButtons.add(btnEditCashier);
        cashierListButtons.add(btnDeleteCashier);
        cashierListPanel.add(cashierScrollPane, BorderLayout.CENTER);
        cashierListPanel.add(cashierListButtons, BorderLayout.SOUTH);

        // Main Panel Setup
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, productPanel, cashierPanel);
        splitPane.setDividerLocation(350);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private void updateProductList() {
        productListModel.clear();
        for (Product product : products) {
            productListModel.addElement(product.toString());
        }
    }

    private void updateCashierList() {
        cashierListModel.clear();
        for (Cashier cashier : cashiers) {
            cashierListModel.addElement(cashier.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManagerDashboard().display();
        });
    }
}
