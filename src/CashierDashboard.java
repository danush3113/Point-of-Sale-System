import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CashierDashboard {
    private List<Product> products;
    private static final String PRODUCTS_FILE = "products.txt";

    public CashierDashboard() {
        try {
            products = FileManager.loadProducts(PRODUCTS_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading products: " + e.getMessage());
        }
    }

    public void display() {
        JFrame frame = new JFrame("Cashier Dashboard");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.decode("#F5F5F5"));

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#6200EA"));
        JLabel titleLabel = new JLabel("Cashier Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Product List Panel
        JPanel productPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product p : products) {
            productListModel.addElement(p.getName() + " - " + p.getCategory() + " - $" + p.getPrice() + " - Qty: " + p.getQuantity());
        }

        JList<String> productList = new JList<>(productListModel);
        productList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(productList);
        productPanel.add(scrollPane, BorderLayout.CENTER);
        productPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.decode("#F5F5F5"));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(Color.decode("#6200EA"));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));

        searchPanel.add(new JLabel("Search Product:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().toLowerCase();
                DefaultListModel<String> filteredListModel = new DefaultListModel<>();

                for (Product p : products) {
                    if (p.getName().toLowerCase().contains(searchTerm) || p.getCategory().toLowerCase().contains(searchTerm)) {
                        filteredListModel.addElement(p.getName() + " - " + p.getCategory() + " - $" + p.getPrice() + " - Qty: " + p.getQuantity());
                    }
                }

                productList.setModel(filteredListModel);
            }
        });

        // Purchase Panel
        JPanel purchasePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        purchasePanel.setBackground(Color.decode("#F5F5F5"));
        JTextField quantityField = new JTextField(5);
        JButton purchaseButton = new JButton("Purchase");

        purchaseButton.setBackground(Color.decode("#03DAC5"));
        purchaseButton.setForeground(Color.BLACK);
        purchaseButton.setFont(new Font("Arial", Font.BOLD, 14));

        purchasePanel.add(new JLabel("Quantity:"));
        purchasePanel.add(quantityField);
        purchasePanel.add(purchaseButton);

        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProduct = productList.getSelectedValue();
                if (selectedProduct != null) {
                    String[] parts = selectedProduct.split(" - ");
                    String productName = parts[0];
                    int quantity = Integer.parseInt(quantityField.getText());

                    Product product = findProductByName(productName);
                    if (product != null && product.getQuantity() >= quantity) {
                        product.setQuantity(product.getQuantity() - quantity);
                        try {
                            FileManager.saveProducts(PRODUCTS_FILE, products);
                            JOptionPane.showMessageDialog(null, "Product purchased successfully!");

                            // Update Product List
                            productListModel.clear();
                            for (Product p : products) {
                                productListModel.addElement(p.getName() + " - " + p.getCategory() + " - $" + p.getPrice() + " - Qty: " + p.getQuantity());
                            }

                            // Generate and Save Bill
                            double totalCost = product.getPrice() * quantity;
                            generateBill(product, quantity, totalCost);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error processing purchase: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Not enough stock available!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No product selected!");
                }
            }
        });

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(searchPanel, BorderLayout.WEST);
        frame.add(productPanel, BorderLayout.CENTER);
        frame.add(purchasePanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private Product findProductByName(String name) {
        for (Product p : products) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private void generateBill(Product product, int quantity, double totalCost) {
        // Prepare the bill content
        String bill = "Receipt\n--------------------\n" +
                "Product: " + product.getName() + "\n" +
                "Quantity: " + quantity + "\n" +
                "Price: $" + product.getPrice() + "\n" +
                "Total: $" + totalCost + "\n" +
                "Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n" +
                "--------------------\n";

        // File to save the bill
        String billFile = "purchas.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(billFile, true))) {
            // Append the bill content to the file
            writer.write(bill);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Bill saved to " + billFile);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving bill: " + ex.getMessage());
        }
    }
}
