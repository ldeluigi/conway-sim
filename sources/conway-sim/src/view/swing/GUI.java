package view.swing;

import javax.swing.JPanel;

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
    void setView(JPanel viewPanel);

    /**
     * Returns to the default view.
     */
    void backToMainMenu();

}
