package services;

import utilities.Logger; // Import your Logger class
import java.util.Random;

public class InputValidator {

    public static boolean isValidBookTitle(String title) {
        boolean valid = title != null && !title.trim().isEmpty(); // Title must not be empty
        if (!valid) {
            Logger.logError("Invalid book title provided: " + title, null); // Log invalid title
        }
        return valid;
    }

    public static boolean isValidAuthor(String author) {
        boolean valid = author != null && author.trim().matches("[a-zA-Z\\s]+"); // Only letters and spaces
        if (!valid) {
            Logger.logError("Invalid author name provided: " + author, null); // Log invalid author
        }
        return valid;
    }

    public static boolean isValidISBN(String isbn) {
        boolean valid = isbn != null && isbn.matches("\\d{3}-\\d{10}"); // Must match the format 978-xxxxxxxxxx
        if (!valid) {
            Logger.logError("Invalid ISBN provided: " + isbn, new Exception("Invalid ISBN format")); // Log invalid ISBN with an exception
        }
        return valid;
    }

    public static boolean isValidCustomerName(String name) {
        boolean valid = name != null && name.trim().matches("[a-zA-Z\\s]+"); // Only letters and spaces
        if (!valid) {
            Logger.logError("Invalid customer name provided: " + name, null); // Log invalid customer name
        }
        return valid;
    }

    public static String generateCustomerID() {
        Random random = new Random();
        return String.format("%05d", random.nextInt(100000)); // Generate a random 5-digit number
    }

    public static String generateUserID() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000)); // Generate a random 3-digit number
    }
}