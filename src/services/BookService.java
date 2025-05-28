package services;

import models.Book;
import constants.CSVFilePaths;
import services.InputValidator; 
import utilities.Logger; // Import Logger class
import javax.swing.JOptionPane; // For user interaction
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private List<Book> availableBooks;

    public BookService() {
        this.availableBooks = new ArrayList<>();
        loadBooksFromCSV();
    }

    public void addBook(Book book) {
        // Validate book details before adding
        if (!InputValidator.isValidBookTitle(book.getTitle())) {
            JOptionPane.showMessageDialog(null, "Invalid book title. Please enter a valid title.");
            return;
        }
        if (!InputValidator.isValidAuthor(book.getAuthor())) {
            JOptionPane.showMessageDialog(null, "Invalid author name. Please enter words only.");
            return;
        }
        if (!InputValidator.isValidISBN(book.getIsbn())) {
            JOptionPane.showMessageDialog(null, "Invalid ISBN. It must be in the format 978-xxxxxxxxxx.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.BOOKS_CSV, true))) {
            writer.write(book.toCsvString()); // Use toCsvString to include all fields
            writer.newLine();
            availableBooks.add(book); // Add to in-memory list
            JOptionPane.showMessageDialog(null, "Book added successfully!"); // Confirmation message
        } catch (IOException e) {
            Logger.logError("Failed to add book: " + book.getTitle(), e); // Log the error
            JOptionPane.showMessageDialog(null, "An error occurred while adding the book.");
        }
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(availableBooks); // Return a copy to avoid external modification
    }

    private void loadBooksFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSVFilePaths.BOOKS_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Check if data has the expected number of fields
                if (data.length >= 5) { // Updated to check for publisher and year
                    availableBooks.add(new Book(data[0].trim(), data[1].trim(), data[2].trim(), 
                                                 data[3].trim(), Integer.parseInt(data[4].trim()))); // isbn, title, author, publisher, year
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            Logger.logError("Failed to load books from CSV.", e); // Log the error
        }
    }

    public void borrowBook(String isbn) {
        availableBooks.removeIf(book -> book.getIsbn().equals(isbn)); // Remove from available books
    }

    public void returnBook(Book book) {
        availableBooks.add(book); // Add back to available books
    }

    // Method to delete a book by its ISBN
    public void deleteBook(String bookISBN) {
        List<Book> updatedBooks = new ArrayList<>();
        boolean bookFound = false;

        // Check if the book exists
        for (Book book : availableBooks) {
            if (book.getIsbn().equals(bookISBN)) {
                bookFound = true; // Book found, do not add it to the updated list
            } else {
                updatedBooks.add(book); // Keep other books
            }
        }

        if (!bookFound) {
            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " not found!"); // Notify user
            return;
        }

        // Write updated book list back to the CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.BOOKS_CSV))) {
            for (Book book : updatedBooks) {
                writer.write(book.toCsvString()); // Convert Book object to CSV format
                writer.newLine();
            }
            availableBooks = updatedBooks; // Update in-memory list
            JOptionPane.showMessageDialog(null, "Book deleted successfully!"); // Notify user
        } catch (IOException e) {
            Logger.logError("Failed to delete book with ISBN: " + bookISBN, e); // Log the error
            JOptionPane.showMessageDialog(null, "An error occurred while deleting the book.");
        }
    }

    // Method to update a book's details
    public void updateBook(String bookISBN) {
        Book bookToUpdate = null;
    
        // Find the book to update
        for (Book book : availableBooks) {
            if (book.getIsbn().equals(bookISBN)) {
                bookToUpdate = book;
                break;
            }
        }
    
        if (bookToUpdate == null) {
            JOptionPane.showMessageDialog(null, "Book with ISBN " + bookISBN + " not found!"); // Notify user
            return;
        }
    
        String[] options = {"Title", "Author", "ISBN", "Publisher", "Year"};
        String choice = (String) JOptionPane.showInputDialog(null, "Select field to update:",
                "Update Book", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
        if (choice == null) return; // Cancelled
    
        String newValue = JOptionPane.showInputDialog("Enter new value:");
        if (newValue == null || newValue.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "New value cannot be empty!"); // Notify user
            return;
        }
    
        switch (choice) {
            case "Title":
                bookToUpdate.setTitle(newValue); // Update title
                break;
            case "Author":
                bookToUpdate.setAuthor(newValue); // Update author
                break;
            case "ISBN":
                // Validate new ISBN format (you can add more specific validation based on your requirements)
                if (!isValidIsbn(newValue)) {
                    JOptionPane.showMessageDialog(null, "Invalid ISBN format. Please enter a valid ISBN.");
                    return;
                }
                bookToUpdate.setIsbn(newValue); // Update ISBN
                break;
            case "Publisher":
                bookToUpdate.setPublisher(newValue); // Update publisher
                break;
            case "Year":
                try {
                    int year = Integer.parseInt(newValue); // Convert to int
                    if (year < 0) {
                        throw new NumberFormatException(); // Prevent negative years
                    }
                    bookToUpdate.setYear(year); // Update year
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid year. Please enter a valid year.");
                    return;
                }
                break;
        }
    
        // Write updated book list back to the CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSVFilePaths.BOOKS_CSV))) {
            for (Book book : availableBooks) {
                writer.write(book.toCsvString()); // Convert Book object to CSV format
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Book updated successfully!"); // Notify user
        } catch (IOException e) {
            Logger.logError("Failed to update book with ISBN: " + bookISBN, e); // Log the error
            JOptionPane.showMessageDialog(null, "An error occurred while updating the book.");
        }
    }
    
    // Helper method to validate ISBN format (example implementation)
    private boolean isValidIsbn(String isbn) {
        // Basic validation: check if the ISBN is not empty and has a reasonable length
        return isbn != null && (isbn.length() == 10 || isbn.length() == 13);
    }
}