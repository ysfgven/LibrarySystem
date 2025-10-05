import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class LogHelper {

    private static final String LOG_FOLDER = "logs";
    private static final String LOG_FILE = LOG_FOLDER + "/application.log";

    static {
        new java.io.File(LOG_FOLDER).mkdirs();
    }

    public static void logException(Exception ex) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(LocalDateTime.now() + " - Exception: " + ex.getClass().getName());
            pw.println("Message: " + ex.getMessage());
            for (StackTraceElement ste : ex.getStackTrace()) {
                pw.println("\tat " + ste);
            }
            pw.println();
        } catch (IOException ioe) {
            System.err.println(LocalDateTime.now() + " - Failed to write log: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

}
