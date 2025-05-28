package models;

public class User {
    private String name;
    private String userId;

    public User(String name, String userId) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        this.name = name;
        this.userId = userId;
    }

    // Getters
    public String getName() { 
        return name; 
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("User ID: %s, Name: %s", userId, name); // More descriptive output
    }
}