package view.swing.sandbox;

import view.Sandbox;

/**
 * This is the interface for sandbox classes that allow resize of the grid.
 *
 */
public interface ResizableSandbox extends Sandbox {

    /**
     * Enables "apply" button to be clicked by the user.
     * 
     * @param flag
     *            true if the button should be enabled
     */
    void setButtonApplyEnabled(boolean flag);

}
