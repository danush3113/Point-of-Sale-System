public class Cashier {
    private String username;
    private String password;
    private boolean isActive;

    public Cashier(String username, String password, boolean isActive) {
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }
}
