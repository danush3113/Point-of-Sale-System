import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<User> loadUsers(String fileName) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], Boolean.parseBoolean(parts[2])));
                }
            }
        }
        return users;
    }

    public static void saveUser(String fileName, User user) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(user.getUsername() + "," + user.getPassword() + "," + user.isManager());
            bw.newLine();
        }
    }

    public static List<Product> loadProducts(String fileName) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    products.add(new Product(parts[0], parts[1], Double.parseDouble(parts[2]), Integer.parseInt(parts[3])));
                }
            }
        }
        return products;
    }

    public static void saveProducts(String fileName, List<Product> products) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : products) {
                bw.write(product.getName() + "," + product.getCategory() + "," + product.getPrice() + "," + product.getQuantity());
                bw.newLine();
            }
        }
    }

    public static List<Cashier> loadCashiers(String fileName) throws IOException {
        List<Cashier> cashiers = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String username = parts[0];
            String password = parts[1];
            boolean isActive = Boolean.parseBoolean(parts[2]);
            cashiers.add(new Cashier(username, password, isActive));
        }
        reader.close();
        return cashiers;
    }

    public static void saveCashiers(String fileName, List<Cashier> cashiers) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Cashier cashier : cashiers) {
            writer.write(cashier.getUsername() + "," + cashier.getPassword() + "," + cashier.isActive());
            writer.newLine();
        }
        writer.close();
    }

}
