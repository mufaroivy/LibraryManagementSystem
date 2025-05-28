package models;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int year;

    // Constructor
    public Book(String isbn, String title, String author, String publisher, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }

    // Setters
    public void setTitle(String newValue) {
        this.title = newValue;
    }

    public void setAuthor(String newValue) {
        this.author = newValue;
    }

    public void setIsbn(String newValue) {
        this.isbn = newValue;
    }

    public void setPublisher(String newValue) {
        this.publisher = newValue;
    }

    public void setYear(int newValue) {
        this.year = newValue;
    }

    @Override
    public String toString() {
        return String.format("Title: %s, Author: %s, ISBN: %s, Publisher: %s, Year: %d", 
                             title, author, isbn, publisher, year);
    }

    // Method to convert Book object to CSV format
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%d", isbn, title, author, publisher, year);
    }
}