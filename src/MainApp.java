import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class MainApp {
    private static final String USERS_FILE = "users.txt";
    private static final String CASHIERS_FILE = "cashiers.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                List<User> users = FileManager.loadUsers(USERS_FILE);
                String username = JOptionPane.showInputDialog("Enter Username:");
                String password = JOptionPane.showInputDialog("Enter Password:");

                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        if (user.isManager()) {
                            new ManagerDashboard().display();
                        } else {
                            new CashierDashboard().display();
                        }
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid credentials!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
            }
        });
    }
}
