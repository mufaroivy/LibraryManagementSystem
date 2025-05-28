package services;

import models.User;
import constants.CSVFilePaths;
import services.InputValidator; 
import utilities.Logger; // Import your Logger class

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane; // For user interaction

public class UserService {

    public void addUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.USERS_CSV, true))) {
            writer.write(String.format("User ID: %s, Name: %s", user.getUserId(), user.getName())); // Store in the specified format
            writer.newLine();
        } catch (IOException e) {
            Logger.logError("Error saving user data for User ID: " + user.getUserId(), e); // Log the error
            JOptionPane.showMessageDialog(null, "Error saving user data. Please try again.");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSVFilePaths.USERS_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse the line according to the specified format
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String userId = parts[0].split(": ")[1].trim(); // Extract userId
                    String name = parts[1].split(": ")[1].trim();   // Extract name
                    users.add(new User(name, userId)); // User(name, userId)
                } else {
                    Logger.logError("Skipping invalid line while reading users: " + line, new Exception("Invalid format")); // Log invalid line
                }
            }
        } catch (IOException e) {
            Logger.logError("Error reading user data.", e); // Log the error
            JOptionPane.showMessageDialog(null, "Error reading user data. Please try again.");
        }
        return users;
    }

    public boolean authenticateUser(String name, String userId) {
        boolean isAuthenticated = getAllUsers().stream()
                .anyMatch(user -> user.getName().equalsIgnoreCase(name) && user.getUserId().equals(userId));
        
        if (!isAuthenticated) {
            Logger.logError("Authentication failed for User ID: " + userId + " with Name: " + name, new Exception("Invalid credentials")); // Log failed authentication
            JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
        }
        
        return isAuthenticated;
    }

    public boolean registerUser(String name) {
        // Validate the user name
        if (!InputValidator.isValidCustomerName(name)) {
            Logger.logError("Invalid user name attempted: " + name, new Exception("Invalid user name")); // Log invalid user name
            JOptionPane.showMessageDialog(null, "Invalid user name. Please enter a valid name.");
            return false;
        }

        // Generate a unique user ID
        String userId = generateUniqueUserId();

        // Create a new user and add to the CSV
        User newUser = new User(name, userId);
        addUser(newUser);
        Logger.logError("User registered successfully with User ID: " + userId, null); // Log successful registration
        JOptionPane.showMessageDialog(null, "User registered successfully! User ID: " + userId); // Confirmation message
        return true; // Registration successful
    }

    private String generateUniqueUserId() {
        String userId;
        do {
            userId = InputValidator.generateUserID();
        } while (isUserIdExists(userId)); // Check if user ID already exists
        return userId;
    }

    private boolean isUserIdExists(String userId) {
        return getAllUsers().stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }
}