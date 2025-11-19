/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

public abstract class User {

    private final String userID;
    private final String role;
    private String username;
    private String email;
    private String passwordHash;

    protected User(String userId, String role, String username, String email, String passwordHash) {
        this.userID = userId;
        this.role = role;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getID() {
        return userID;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isPasswordCorrect(String passwordText) {
        String hash = JsonDatabaseManager.HashUtil.hashPassword(passwordText);
        return this.passwordHash.equals(hash);
    }

    @Override
    public String toString() {
        return userID + ", " + role + ": " + "Username: " + username + ", Email: " + email;
    }
}