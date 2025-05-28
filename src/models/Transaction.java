package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private Customer user; 
    private Book book;
    private Date borrowDate;
    private Date returnDate;
    private boolean isReturned;

    // Constructor
    public Transaction(Customer user, Book book, Date borrowDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.isReturned = false; // Initially set to false
    }

    // Getters
    public Customer getUser() { 
        return user; 
    }

    public Book getBook() { 
        return book; 
    }

    public Date getBorrowDate() { 
        return borrowDate; 
    }

    public Date getReturnDate() { 
        return returnDate; 
    }

    public boolean isReturned() { 
        return isReturned; 
    }

    // Set the return date and update the returned status
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
        this.isReturned = true; // Update status to returned
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "user=" + user.getCustomerId() +
                ", book=" + book.getIsbn() +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + (isReturned ? returnDate : "Not Returned") +
                '}';
    }

    public String toCsvString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        return user.getCustomerId() + "," + 
               book.getIsbn() + "," + 
               dateFormat.format(borrowDate) + "," + 
               (returnDate != null ? dateFormat.format(returnDate) : "Not Returned");
    }
}