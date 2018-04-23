package controller.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * Logger that outputs string to a file if available or to {@link System#err}.
 */
public final class Logger {

    private Logger() {
    }

    /**
     * Logs the message to a file if possible or else to {@link System#err}, with the current time.
     * 
     * @param message
     *            the message to log
     */
    public static void logTime(final String message) {
        log(LocalDateTime.now() + " @ " + message);
    }

    /**
     * Logs the message to a file if possible or else to {@link System#err}.
     * 
     * @param message
     *            the message to log
     */
    public static void log(final String message) {
        try (PrintStream out = out()) {
            out.println(message);
        } catch (FileNotFoundException e) {
            err().println(message);
        }
    }
 
    /**
     * Prints (formatted) the {@link Throwable} stack trace to a file if possible or else to {@link System#err}.
     * 
     * @param e
     *            the throwable to log
     */
    public static void logThrowable(final Throwable e) {
        try (PrintStream out = out()) {
            e.printStackTrace(out);
        } catch (FileNotFoundException e1) {
            e.printStackTrace(err());
        }
    }

    private static PrintStream err() {
        System.err.println("Couldn't create a log file in " + System.getProperty("user.dir"));
        System.err.println("Stack trace:");
        return System.err;
    }

    private static PrintStream out() throws FileNotFoundException {
        return new PrintStream(new FileOutputStream(ResourceLoader.loadString("log.file.name"), true),
                    true);
    }
}
