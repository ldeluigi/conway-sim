package view.swing.sandbox;
import javax.swing.JButton;
import controller.editor.PatternEditor;
/**
 * 
 *
 */
public interface Sandbox {
    /**
     * Applies size changes to the grid.
     */
    void resetGrid();

    /**
     * Enables "clear" button to be clicked by the user.
     * @param flag
     *            a boolean flag
     */
    void setButtonClearEnabled(boolean flag);

    /**
     * Enables "apply" button to be clicked by the user.
     * @param flag
     *            a boolean flag
     */
    void setButtonApplyEnabled(boolean flag);

    /**
     * Enables clear button to be clicked by the user.
     * @return the book button
     */
    JButton getButtonBook();

    /**
     * Gets the editor used to handle the grid and returns it.
     * @return the gridEtitor
     */
    PatternEditor getGridEditor();

    /**
     * Refreshes generation panel.
     */
    void refreshView();
}
