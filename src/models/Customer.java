package models;

public class Customer {
    private String name;
    private String customerId;

    // Constructor that validates name and customerId
    public Customer(String name, String customerId) {
        if (name == null || name.trim().isEmpty() || customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Name and Customer ID cannot be null or empty.");
        }
        this.name = name;
        this.customerId = customerId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId; 
    }

    @Override
    public String toString() {
        return "Customer{Name='" + name + "', Customer ID='" + customerId + "'}";
    }

    // Method to return a CSV formatted string
    public String toCsvString() {
        return customerId + "," + name; // Returns the customer ID and name in CSV format
    }
}