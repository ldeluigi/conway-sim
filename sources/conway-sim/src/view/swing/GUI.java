package view.swing;

import javax.swing.JComponent;

/**
 * Interface for generic GUI implementations.
 */
public interface GUI {

    /**
     * Gets current frame height.
     * @return height
     */
    int getCurrentHeight();

    /**
     * Gets current frame width.
     * @return width
     */
    int getCurrentWidth();

    /**
     * Closes the application.
     */
    void close();

    /**
     * Makes the given panel the main content pane of the view.
     * @param viewPanel to be displayed as main view.
     */
    void setView(JComponent viewPanel);

    /**
     * Returns to the default view.
     */
    void backToMainMenu();

    /**
     * @return screen height in pixels
     */
    int getScreenHeight();

    /**
     * @return screen width in pixels
     */
    int getScreenWidth();

}
