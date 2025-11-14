import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Login {

    private static final String USERS_FILE = "users.txt"; // Manager credentials
    private static final String CASHIERS_FILE = "cashiers.txt"; // Cashier credentials

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setting a custom look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel for the login form
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.decode("#F5F5F5"));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#6200EA"));
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Center panel for form inputs
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.decode("#F5F5F5"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField passwordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#F5F5F5"));
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.decode("#6200EA"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.add(loginButton);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean validCredentials = false;
                boolean isManager = false;

                // Check manager credentials
                try {
                    if (validateUserCredentials(USERS_FILE, username, password)) {
                        validCredentials = true;
                        isManager = true;
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Error reading user data.");
                }

                // Check cashier credentials if not manager
                if (!validCredentials) {
                    try {
                        if (validateUserCredentials(CASHIERS_FILE, username, password)) {
                            validCredentials = true;
                            isManager = false;
                        }
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "Error reading cashier data.");
                    }
                }

                // Proceed based on the validity of the credentials
                if (validCredentials) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    frame.dispose(); // Close the login frame

                    if (isManager) {
                        new ManagerDashboard().display();
                    } else {
                        new CashierDashboard().display();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                }
            }
        });
    }

    private static boolean validateUserCredentials(String fileName, String username, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String storedUsername = parts[0];
            String storedPassword = parts[1];

            // Check if the credentials match
            if (storedUsername.equals(username) && storedPassword.equals(password)) {
                reader.close();
                return true;
            }
        }

        reader.close();
        return false;
    }
}
