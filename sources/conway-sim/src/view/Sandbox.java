package view;

import javax.swing.JButton;

import controller.editor.PatternEditor;

/**
 * 
 *
 */
public interface Sandbox extends UpdateScheduler {
    /**
     * Applies size changes to the grid.
     */
    void resetGrid();

    /**
     * Enables "clear" button to be clicked by the user.
     * 
     * @param flag
     *            a boolean flag
     */
    void setButtonClearEnabled(boolean flag);

    /**
     * Enables clear button to be clicked by the user.
     * 
     * @return the book button
     */
    JButton getButtonBook();

    /**
     * Gets the editor used to handle the grid and returns it.
     * 
     * @return the gridEtitor
     */
    PatternEditor getGridEditor();

    /**
     * Refreshes generation panel.
     */
    void refreshView();
}
