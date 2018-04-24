package controller.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Logger that outputs string to the selected LogOutput if available or to
 * {@link System#err}. The default LogOutput is a file output stream that
 * targets a file specified in properties (log.file.name)
 */
public final class Logger {

    private static Optional<Supplier<PrintStream>> customOut = Optional.empty();

    private Logger() {
    }

    /**
     * Logs the message to the LogOutput if possible or else to {@link System#err},
     * with the current time.
     * 
     * @param message
     *            the message to log
     */
    public static void logTime(final String message) {
        log(LocalDateTime.now() + " @ " + message);
    }

    /**
     * Logs the message to the LogOutput if possible or else to {@link System#err}.
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
     * Prints (formatted) the {@link Throwable} stack trace to the LogOutput if
     * possible or else to {@link System#err}.
     * 
     * @param e
     *            the throwable to log
     */
    public static void logThrowable(final Throwable e) {
        try (PrintStream out = out()) {
            e.printStackTrace(out);
        } catch (FileNotFoundException e1) {
            final PrintStream err = err();
            err.println("Stack trace:");
            e.printStackTrace(err);
        }
    }

    /**
     * For each log request, the printStreamSupplier must produce a non-null
     * PrintStream that will be used as LogOutput. If this method is never called,
     * if it's called after the first log request, or if null is produced by the
     * supplier, the default LogOutput will be used instead.
     * 
     * @param printStreamSupplier
     *            the supplier of {@link PrintStream} for LogOutput
     */
    public static synchronized void setLogOutput(final Supplier<PrintStream> printStreamSupplier) {
        customOut = Optional.ofNullable(printStreamSupplier);
    }

    private static PrintStream err() {
        System.err.println("Couldn't create a log file in " + System.getProperty("user.dir"));
        return System.err;
    }

    private static synchronized PrintStream out() throws FileNotFoundException {
        if (customOut.isPresent()) {
            final PrintStream ps = customOut.get().get();
            if (ps != null) {
                return ps;
            }
        }
        return new PrintStream(new FileOutputStream(ResourceLoader.loadString("log.file.name"), true), true);
    }
}
