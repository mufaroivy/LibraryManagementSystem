package services;

import models.Book;
import models.Customer;
import models.Transaction;
import constants.CSVFilePaths;
import utilities.Logger;

import javax.swing.JOptionPane;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionService {
    // Update the date format to match CSV data structure
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    public void borrowBook(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.TRANSACTIONS_CSV, true))) {
            writer.write(transaction.toCsvString()); // Ensure proper formatting
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Book borrowed successfully!"); // Notify user
        } catch (IOException e) {
            Logger.logError("Error writing to transactions file while borrowing book: " + transaction.toString(), e); // Log the error
            JOptionPane.showMessageDialog(null, "An error occurred while borrowing the book."); // Notify user
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSVFilePaths.TRANSACTIONS_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Ensure there are enough fields to create a Transaction
                if (data.length < 4) {
                    Logger.logError("Insufficient data in line: " + line, null);
                    continue; // Skip this line
                }

                // Extract fields and handle potential exceptions
                try {
                    String customerId = data[0].trim();
                    String bookISBN = data[1].trim();
                    String bookTitle = data.length > 2 ? data[2].trim() : "Unknown"; // Fallback to "Unknown"
                    String bookAuthor = data.length > 3 ? data[3].trim() : "Unknown"; // Fallback to "Unknown"
                    String publisher = data.length > 4 ? data[4].trim() : "Unknown"; // Fallback to "Unknown"
                    int year = data.length > 5 ? Integer.parseInt(data[5].trim()) : 0; // Fallback to 0
                    Date borrowDate = dateFormat.parse(data[6].trim()); // Borrow date in the 7th position

                    // Handle return date if provided
                    String returnDateStr = data.length > 7 ? data[7].trim() : null;

                    // Validate customerId and bookISBN
                    if (customerId.isEmpty() || bookISBN.isEmpty()) {
                        Logger.logError("Customer ID or Book ISBN is empty for line: " + line, null);
                        continue; // Skip this line
                    }

                    // Create Customer object
                    Customer customer = new Customer(customerId, "Unknown"); // Placeholder name

                    // Create Book object with additional fields
                    Book book = new Book(bookISBN, bookTitle, bookAuthor, publisher, year);

                    // Create Transaction object
                    Transaction transaction = new Transaction(customer, book, borrowDate);

                    // Handle return date if provided
                    if (returnDateStr != null && !"Not Returned".equals(returnDateStr)) {
                        try {
                            Date returnDate = dateFormat.parse(returnDateStr);
                            transaction.setReturnDate(returnDate);
                        } catch (ParseException e) {
                            Logger.logError("Error parsing return date for line: " + line, e);
                        }
                    }

                    transactions.add(transaction);
                } catch (ParseException e) {
                    Logger.logError("Error parsing date for line: " + line, e);
                } catch (NumberFormatException e) {
                    Logger.logError("Error parsing year for line: " + line, e);
                } catch (Exception e) {
                    Logger.logError("Error processing line: " + line, e);
                }
            }
        } catch (IOException e) {
            Logger.logError("Error reading transactions file: " + e.getMessage(), e);
        }
        return transactions; // Return the list of transactions
    }

    public void updateTransaction(Transaction transaction) {
        List<Transaction> transactions = getAllTransactions();
        boolean updated = false;

        // Update the transaction in the list if it exists
        for (int i = 0; i < transactions.size(); i++) {
            // Adjusted criteria for matching transactions
            if (transactions.get(i).getUser().getCustomerId().equals(transaction.getUser().getCustomerId()) &&
                transactions.get(i).getBook().getIsbn().equals(transaction.getBook().getIsbn()) &&
                transactions.get(i).getBorrowDate().equals(transaction.getBorrowDate())) {
                transactions.set(i, transaction);
                updated = true;
                break;
            }
        }

        // Write updated transactions back to the CSV only if an update occurred
        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.TRANSACTIONS_CSV))) {
                for (Transaction t : transactions) {
                    writer.write(t.toCsvString()); // Ensure proper CSV format
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Transaction updated successfully!"); // Notify user
            } catch (IOException e) {
                Logger.logError("Error writing updated transactions file: " + e.getMessage(), e); // Log write error
                JOptionPane.showMessageDialog(null, "An error occurred while updating the transaction."); // Notify user
            }
        } else {
            JOptionPane.showMessageDialog(null, "Transaction not found."); // Notify user if transaction does not exist
        }
    }

    // Method to display transactions in a user-friendly format
    public void displayTransactions() {
        List<Transaction> transactions = getAllTransactions();
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No transactions found."); // Notify user
            return;
        }

        StringBuilder transactionList = new StringBuilder();
        for (Transaction transaction : transactions) {
            transactionList.append(transaction.toString()).append("\n"); // Assuming Transaction has a meaningful toString() method
        }

        JOptionPane.showMessageDialog(null, transactionList.toString(), "Transactions", JOptionPane.INFORMATION_MESSAGE);
    }
}