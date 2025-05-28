package constants;

public final class CSVFilePaths {
    // Prevent instantiation
    private CSVFilePaths() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated.");
    }

    public static final String BOOKS_CSV = "resources/database/books.csv";
    public static final String CUSTOMERS_CSV = "resources/database/customers.csv";
    public static final String TRANSACTIONS_CSV = "resources/database/transactions.csv";
    public static final String USERS_CSV = "resources/database/users.csv";
}