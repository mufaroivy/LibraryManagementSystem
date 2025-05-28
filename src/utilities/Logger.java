package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE_PATH = "error_log.txt"; // Path to your log file

    public static void logError(String message, Exception e) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();

            if (e != null) {
                writer.write("Exception: " + e.getClass().getName() + " - " + e.getMessage());
                writer.newLine();
                writer.write("Stack Trace: ");
                for (StackTraceElement element : e.getStackTrace()) {
                    writer.write("\t" + element.toString());
                    writer.newLine();
                }
            } else {
                writer.write("No exception information available.");
                writer.newLine();
            }

            writer.write("---------------------------------------------------");
            writer.newLine();
        } catch (IOException ioException) {
            // Log to console in case of a failure to write to the log file
            System.err.println("Failed to log error: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }
}