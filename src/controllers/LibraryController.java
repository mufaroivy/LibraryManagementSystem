package controllers;

import models.Book;
import models.Customer;
import models.Transaction;
import services.BookService;
import services.CustomerService;
import services.TransactionService;
import services.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class LibraryController {
    private BookService bookService = new BookService();
    private CustomerService customerService = new CustomerService();
    private TransactionService transactionService = new TransactionService();
    private UserService userService = new UserService();

    public void launch() {
        showLoginDialog();
    }

    private void showLoginDialog() {
        JFrame loginFrame = new JFrame("Mufaro's Library Management System");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Center the components with padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding

        // Title label
        JLabel titleLabel = new JLabel("Login for Mufaro's Library", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        loginFrame.add(titleLabel, gbc);

        // Name label and field
        JLabel nameLabel = new JLabel("Staff Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginFrame.add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginFrame.add(nameField, gbc);

        // User ID label and field
        JLabel userIdLabel = new JLabel("User ID:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginFrame.add(userIdLabel, gbc);

        JTextField userIdField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginFrame.add(userIdField, gbc);

        // Register button (now first)
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();

            if (userService.registerUser(name)) {
                JOptionPane.showMessageDialog(loginFrame, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Registration failed! User ID might already exist.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3; // Position for Register button
        gbc.gridwidth = 1;
        loginFrame.add(registerButton, gbc);

        // Login button (now second)
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String userId = userIdField.getText().trim();

            if (userService.authenticateUser(name, userId)) {
                loginFrame.dispose();
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials! Please try again.");
            }
        });

        gbc.gridx = 1; // Position for Login button
        gbc.gridy = 3; // Same row as Register button
        loginFrame.add(loginButton, gbc);

        loginFrame.setLocationRelativeTo(null); // Center the frame on the screen
        loginFrame.setVisible(true);
    }

    private void showMainMenu() {
        JFrame frame = new JFrame("Ivy's Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Padding for the components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding

        // Title label
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        frame.add(titleLabel, gbc);

        // Books button
        JButton booksButton = new JButton("Books");
        booksButton.addActionListener(e -> showBooksMenu(frame));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(booksButton, gbc);

        // Customers button
        JButton customersButton = new JButton("Customers");
        customersButton.addActionListener(e -> showCustomersMenu(frame));
        gbc.gridy = 2;
        frame.add(customersButton, gbc);

        // Check Transactions button
        JButton checkTransactionsButton = new JButton("Check Transactions");
        checkTransactionsButton.addActionListener(e -> checkTransactions(frame));
        gbc.gridy = 3;
        frame.add(checkTransactionsButton, gbc);

        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    // Method to show the books menu
    private void showBooksMenu(JFrame parentFrame) {
        JFrame booksFrame = new JFrame("Books Menu");
        booksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        booksFrame.setSize(500, 400);
        booksFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        // Padding for the components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
    
        // Add buttons for book management
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> addBook(booksFrame));
        gbc.gridx = 0;
        gbc.gridy = 0;
        booksFrame.add(addBookButton, gbc);
    
        JButton borrowBookButton = new JButton("Borrow Book");
        borrowBookButton.addActionListener(e -> borrowBook(booksFrame));
        gbc.gridy = 1; // Move to next row
        booksFrame.add(borrowBookButton, gbc);
    
        JButton checkBooksButton = new JButton("Check Books");
        checkBooksButton.addActionListener(e -> checkBooks(booksFrame));
        gbc.gridy = 3; // Move to next row
        booksFrame.add(checkBooksButton, gbc);
    
        JButton deleteBookButton = new JButton("Delete Book");
        deleteBookButton.addActionListener(e -> {
            String bookISBN = JOptionPane.showInputDialog(booksFrame, "Enter the ISBN of the book to delete:");
            if (bookISBN != null) {
                bookService.deleteBook(bookISBN.trim());
            }
        });
        gbc.gridy = 4; // Move to next row
        booksFrame.add(deleteBookButton, gbc);

        JButton updateBookButton = new JButton("Update Book");
        updateBookButton.addActionListener(e -> {
            String bookISBN = JOptionPane.showInputDialog(booksFrame, "Enter the ISBN of the book to update:");
            if (bookISBN != null) {
                bookService.updateBook(bookISBN.trim());
            }
        });
        gbc.gridy = 5; // Move to next row
        booksFrame.add(updateBookButton, gbc);
    
        booksFrame.setLocationRelativeTo(parentFrame); // Center relative to parent
        booksFrame.setVisible(true);
    }
    // Method to show the customers menu
    private void showCustomersMenu(JFrame parentFrame) {
        JFrame customersFrame = new JFrame("Customers Menu");
        customersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        customersFrame.setSize(300, 200);
        customersFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Padding for the components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding

        // Add buttons for customer management
        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(e -> addCustomer(customersFrame));

        JButton deleteCustomerButton = new JButton("Delete Customer");
        deleteCustomerButton.addActionListener(e -> {
       // Call the method to delete a customer
       deleteCustomer(customersFrame); // Assuming you have this method in your LibraryController
});

       JButton updateCustomerButton = new JButton("Update Customer");
       updateCustomerButton.addActionListener(e -> {
       // Call the method to update a customer
       updateCustomer(customersFrame); // Assuming you have this method in your LibraryController
});

        // Layout the buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        customersFrame.add(addCustomerButton, gbc);

        gbc.gridy = 1;
        customersFrame.add(deleteCustomerButton, gbc);

        gbc.gridy = 2;
        customersFrame.add(updateCustomerButton, gbc);

        customersFrame.setLocationRelativeTo(parentFrame); // Center relative to parent
        customersFrame.setVisible(true);
    }

    private void addBook(JFrame frame) {
        String title = JOptionPane.showInputDialog(frame, "Enter Book Title:");
        String author = JOptionPane.showInputDialog(frame, "Enter Book Author:");
        String isbn = JOptionPane.showInputDialog(frame, "Enter Book ISBN:");
        String publisher = JOptionPane.showInputDialog(frame, "Enter Book Publisher:");
        String yearString = JOptionPane.showInputDialog(frame, "Enter Book Year:");
    
        if (title != null && author != null && isbn != null && publisher != null && yearString != null) {
            try {
                int year = Integer.parseInt(yearString); // Convert year input to an integer
    
                // Create the Book object with the new information
                Book book = new Book(isbn, title, author, publisher, year);
                bookService.addBook(book);
                JOptionPane.showMessageDialog(frame, "Book Added!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid year. Please enter a valid year.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
        }
    }

    private void addCustomer(JFrame frame) {
        String name = JOptionPane.showInputDialog(frame, "Enter Customer Name:");
    
        if (name != null && !name.trim().isEmpty()) {
            // Create customer with the provided name and auto-generate the ID
            Customer customer = new Customer(name.trim(), generateCustomerId()); // Assuming generateCustomerId() is a method that creates a unique ID
            customerService.addCustomer(customer);
            JOptionPane.showMessageDialog(frame, "Customer Added!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill in the name.");
        }
    }
    
    // Example method to generate a customer ID (you should implement this based on your requirements)
    private String generateCustomerId() {
        // Implement your logic to generate a unique customer ID
        return "CUST" + System.currentTimeMillis(); // Example: Using current time as part of the ID
    }

    private void deleteCustomer(JFrame frame) {
        String customerId = JOptionPane.showInputDialog(frame, "Enter Customer ID to delete:");
        if (customerId != null && !customerId.trim().isEmpty()) {
            boolean deleted = customerService.deleteCustomer(customerId.trim());
            JOptionPane.showMessageDialog(frame, deleted ? "Customer deleted successfully!" : "Customer ID not found.");
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a valid Customer ID.");
        }
    }

    private void updateCustomer(JFrame frame) {
        String customerId = JOptionPane.showInputDialog(frame, "Enter Customer ID to update:");
        if (customerId != null && !customerId.trim().isEmpty()) {
            String newName = JOptionPane.showInputDialog(frame, "Enter new name for Customer ID " + customerId.trim() + ":");
            if (newName != null && !newName.trim().isEmpty()) {
                boolean updated = customerService.updateCustomer(customerId.trim(), newName.trim());
                JOptionPane.showMessageDialog(frame, updated ? "Customer updated successfully!" : "Customer ID not found.");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid name.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a valid Customer ID.");
        }
    }
    
    // Method to add buttons for delete and update
    private void setupButtons(JFrame frame) {
        JButton deleteButton = new JButton("Delete Customer");
        deleteButton.addActionListener(e -> deleteCustomer(frame));
        frame.add(deleteButton);

        JButton updateButton = new JButton("Update Customer");
        updateButton.addActionListener(e -> updateCustomer(frame));
        frame.add(updateButton);
    }


    private void borrowBook(JFrame frame) {
        String customerId = JOptionPane.showInputDialog(frame, "Enter Customer ID:");
        String isbn = JOptionPane.showInputDialog(frame, "Enter Book ISBN:");
    
        // Check if the book exists in the books database
        Book book = bookService.getAllBooks().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst().orElse(null);
    
        if (book == null) {
            JOptionPane.showMessageDialog(frame, "Book not found in the library!");
            return;
        }
    
        // Check if the book is already borrowed
        if (isBookBorrowed(isbn)) {
            JOptionPane.showMessageDialog(frame, "Book is already borrowed!");
            return;
        }
    
        // Check if the customer exists
        Customer customer = customerService.getAllCustomers().stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst().orElse(null);
    
        if (customer != null) {
            Transaction transaction = new Transaction(customer, book, new Date());
            transactionService.borrowBook(transaction); // Record the transaction
            JOptionPane.showMessageDialog(frame, "Book Borrowed Successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Customer not found!");
        }
    }

    private void checkBooks(JFrame frame) {
        List<Book> allBooks = bookService.getAllBooks();
        StringBuilder message = new StringBuilder("Available Books:\n");
        boolean hasAvailableBooks = false;
    
        for (Book book : allBooks) {
            if (!isBookBorrowed(book.getIsbn())) {
                message.append(book.getTitle())
                       .append(" by ").append(book.getAuthor())
                       .append(", Publisher: ").append(book.getPublisher())
                       .append(", Year: ").append(book.getYear())
                       .append("\n");
                hasAvailableBooks = true;
            }
        }
    
        if (!hasAvailableBooks) {
            message.append("No available books.");
        }
    
        JOptionPane.showMessageDialog(frame, message.toString());
    }

    // Call deleteBook
public void onDeleteBookButtonClicked() {
    String bookISBN = JOptionPane.showInputDialog("Enter the ISBN of the book to delete:");
    if (bookISBN != null) {
        bookService.deleteBook(bookISBN.trim());
    }
}

// Call updateBook
public void onUpdateBookButtonClicked() {
    String bookISBN = JOptionPane.showInputDialog("Enter the ISBN of the book to update:");
    if (bookISBN != null) {
        bookService.updateBook(bookISBN.trim());
    }
}

    private void checkTransactions(JFrame frame) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        StringBuilder message = new StringBuilder("Transactions:\n");
        
        if (transactions.isEmpty()) {
            message.append("No transactions found.");
        } else {
            transactions.forEach(transaction -> message.append(transaction.toString()).append("\n"));
        }
        
        JOptionPane.showMessageDialog(frame, message.toString());
    }

    private boolean isBookBorrowed(String isbn) {
        return transactionService.getAllTransactions().stream()
                .anyMatch(t -> t.getBook().getIsbn().equals(isbn) && !t.isReturned());
    }
}