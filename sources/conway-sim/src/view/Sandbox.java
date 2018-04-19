package view;

import javax.swing.JButton;

import controller.editor.PatternEditor;

/**
 * These are the method that were necessary to the implementation of a panel that contained all the main functions in the swing package.
 * More precisely this panel allowed to edit a grid, show generation computing and control of it, grid resize, clear and book integration.
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
