package controller.editor;

/**
 * A listener for each interaction occurred while using properly the grid.
 * 
 */
public interface CMouseListener {

    /**
     * Method which manages a click on the left mouse button without pressing
     * control.
     */
    void mousePressedIsLeftWithoutControl();

    /**
     * Method which manages a click on the left mouse button while pressing control.
     */
    void mousePressedIsLeftWithControl();

    /**
     * Method which manages a click on the right mouse button while pressing
     * control.
     */
    void mousePressedisRightWithControl();

    /**
     * Method which manages a click on the right mouse button without pressing
     * control.
     */
    void mousePressedisRightWithoutControl();

    /**
     * Method which manages a left mouse button release.
     */
    void mouseReleasedLeft();

    /**
     * Method which manages a right mouse button release.
     */
    void mouseReleasedRight();

    /**
     * Method which manages left mouse button being pressed while changing position
     * on the grid.
     */
    void mouseEnteredWhilePressingLeft();

    /**
     * Method which manages right mouse button being pressed while changing position
     * on the grid.
     */
    void mouseEnteredWhilePressingRight();

    /**
     * Method which manages mouse entering without pressing right or left.
     */
    void mouseEnteredWithoutPress();
}
