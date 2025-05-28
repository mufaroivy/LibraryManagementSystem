import controllers.LibraryController;
import utilities.Logger; // Import the Logger class
import javax.swing.JOptionPane; // For displaying error messages

public class Main {
    public static void main(String[] args) {
        try {
            LibraryController controller = new LibraryController();
            controller.launch(); // Launch the application
        } catch (Exception e) {
            Logger.logError("Unexpected error in main method.", e); // Log the error
            JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please check the log file."); // Inform the user
        }
    }
}