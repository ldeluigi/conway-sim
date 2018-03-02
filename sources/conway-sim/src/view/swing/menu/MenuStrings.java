package view.swing.menu;


/**
 * Static class to get strings. Can support translations.
 *
 */
public final class MenuStrings {

    private MenuStrings() { }

    /**
     * Main Frame title.
     * @return the title of this application.
     */
    public static String getApplicationTitle() {
        return "Conway's Game of Life";
    }

    /**
     * Returns a string representing the version of the source.
     * @return the version
     */
    public static String getVersion() {
        return "0.0.7 (Alpha)";
    }

    /**
     * Info about this project.
     * @return the info
     */
    public static String getInfo() {
        return "LDLM-Project, All Rights Reserved";
    }

    /**
     * Sandbox button Hover hint.
     * @return hint on sandbox button
     */
    public static String getHoverSandboxButton() {
        return "Start Sandbox Mode";
    }

    /**
     * Sandbox button text.
     * @return text to be displayed
     */
    public static String sandboxButtonText() {
        return "Sandbox";
    }

    /**
     * Exit button text.
     * @return text to be displayed
     */
    public static String exitButtonText() {
        return "Exit";
    }

    /**
     * Returns text to be displayed on loading screen.
     * @return the text
     */
    public static String getLoadingText() {
        return "Loading...";
    }
}
