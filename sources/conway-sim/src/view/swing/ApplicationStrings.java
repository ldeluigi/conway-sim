package view.swing;

/**
 * Static class to get strings. Can support translations.
 *
 */
public final class ApplicationStrings {

    private ApplicationStrings() { }

    /**
     * Main Frame title.
     * @return the title of this application.
     */
    public static String getApplicationTitle() {
        return "Conway's Game of Life";
    }

    public static String getVersion() {
        return "0.0.5 (Alpha)";
    }

    public static String getInfo() {
        return "LDLM-Project, All Rights Reserved";
    }
}
