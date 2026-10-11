package model;

public class User {
    private int id;
    private String username;
    private String role;

    public User() {
        this("Cashier", 1);
    }

    public User(String username, int id) {
        this.username = username;
        this.id = id;
        this.role = "Cashier";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return username;
    }
}
