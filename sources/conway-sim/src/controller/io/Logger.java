package controller.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * Logger that outputs string to a file if available or to {@link System#err}.
 */
public final class Logger {

    private static PrintStream out;

    static {
        try {
            out = new PrintStream(new FileOutputStream(ResourceLoader.loadString("log.file.name")), true);
        } catch (FileNotFoundException e) {
            out = System.err;
            out.println("Couldn't create a log file in " + System.getProperty("user.dir"));
            out.println("Stack trace:");
            e.printStackTrace(out);
        }
    }

    private Logger() {
    }

    /**
     * Logs the message to a file if possible or else to {@link System#err}, with
     * the current time.
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
        out.println(message);
    }

    /**
<<<<<<< HEAD
     * Prints (formatted) the {@link Throwable} stack trace to a file if possible or
     * else to {@link System#err}.
=======
     * Prints (formatted) the {@link Throwable} stack trace to a file if possible or else to
     * {@link System#err}.
>>>>>>> 09767c1c3bc2cfef3dd0838fb497b5eb1c215eed
     * 
     * @param e
     *            the throwable to log
     */
    public static void logThrowable(final Throwable e) {
<<<<<<< HEAD
        e.printStackTrace(out);
=======
        try (PrintStream out = out()) {
            e.printStackTrace(out);
        } catch (FileNotFoundException e1) {
            final PrintStream err = err();
            err.println("Stack trace:");
            e.printStackTrace(err);
        }
    }

    private static PrintStream err() {
        System.err.println("Couldn't create a log file in " + System.getProperty("user.dir"));
        return System.err;
    }

    private static PrintStream out() throws FileNotFoundException {
        return new PrintStream(
                new FileOutputStream(ResourceLoader.loadString("log.file.name"), true), true);
>>>>>>> 09767c1c3bc2cfef3dd0838fb497b5eb1c215eed
    }
}
