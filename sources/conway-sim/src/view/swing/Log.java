package view.swing;

/**
 * 
 * A simple log class for message.
 */
public final class Log {

    private Log() { }

    /**
     * 
     * @param text the text to be showed
     */
    public static void logMessage(final String text) {
        System.err.println("log: " + text);
    }

}
