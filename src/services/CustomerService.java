package services;

import models.Customer;
import constants.CSVFilePaths;
import services.InputValidator;
import utilities.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane; 

public class CustomerService {

    public void addCustomer(Customer customer) {
        // Validate customer name
        if (!InputValidator.isValidCustomerName(customer.getName())) {
            JOptionPane.showMessageDialog(null, "Invalid customer name. Please enter words only.");
            return;
        }

        // Generate a unique customer ID
        String customerId = InputValidator.generateCustomerID();
        
        // Create a new Customer object with the generated ID
        Customer newCustomer = new Customer(customer.getName(), customerId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.CUSTOMERS_CSV, true))) {
            writer.write(newCustomer.toCsvString());
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Customer added successfully! Customer ID: " + customerId);
        } catch (IOException e) {
            Logger.logError("Failed to add customer: " + customer.getName(), e);
            JOptionPane.showMessageDialog(null, "An error occurred while adding the customer.");
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSVFilePaths.CUSTOMERS_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Check if data has the expected number of fields
                if (data.length == 2) { // Ensure we have exactly two parts
                    String customerId = data[0].trim(); // First column is ID
                    String name = data[1].trim(); // Second column is name
                    customers.add(new Customer(name, customerId)); // Create Customer object
                } else {
                    System.err.println("Skipping invalid line: " + line); // Log invalid lines
                }
            }
        } catch (IOException e) {
            Logger.logError("Failed to read customer list.", e);
        }
        return customers;
    }

    // Method to delete a customer by ID
    public boolean deleteCustomer(String customerId) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSVFilePaths.CUSTOMERS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[0].equals(customerId)) { // Assuming customerId is the first entry in the CSV
                    lines.add(line); // Keep the line if it's not the one to delete
                } else {
                    found = true; // Found the customer to delete
                }
            }
        } catch (IOException e) {
            Logger.logError("Failed to delete customer with ID: " + customerId, e);
            return false;
        }

        // Write the updated list back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSVFilePaths.CUSTOMERS_CSV))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            Logger.logError("Failed to write updated customer list.", e);
        }

        return found; // Return whether the customer was found and deleted
    }

    // Method to update a customer's name by ID
    public boolean updateCustomer(String customerId, String newName) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSVFilePaths.CUSTOMERS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(customerId)) { // Assuming customerId is the first entry in the CSV
                    parts[1] = newName; // Update the name (assuming it's the second entry)
                    line = String.join(",", parts); // Join parts back into a single line
                    found = true; // Found the customer to update
                }
                lines.add(line);
            }
        } catch (IOException e) {
            Logger.logError("Failed to update customer with ID: " + customerId, e);
            return false;
        }

        // Write the updated list back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSVFilePaths.CUSTOMERS_CSV))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            Logger.logError("Failed to write updated customer list.", e);
        }

        return found; // Return whether the customer was found and updated
    }
}