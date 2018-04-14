package start;

import view.swing.MainGUI;

/**
 * Starts the application.
 */
public final class ApplicationStart {

    private ApplicationStart() {
    }

    /**
     * Calls the constructor of {@link MainGUI} which starts the application.
     * 
     * @param args
     *            from command line, ignored
     */
    public static void main(final String[] args) {
        new MainGUI();
    }

}
