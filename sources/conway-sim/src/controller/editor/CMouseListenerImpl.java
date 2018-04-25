package controller.editor;

/**
 * Implementation of {@link CMouseListener}.
 *
 */
public class CMouseListenerImpl implements CMouseListener {

    private final int row;
    private final int column;
    private final PatternEditor editor;

    /**
     * Constructor method for a new CMouseListenerImpl to observe the grid and its
     * components.
     * 
     * @param i
     *            the horizontal index of the Component to whom is added this
     *            listener
     * @param j
     *            the vertical index of the Component to whom is added this listener
     * @param editor
     *            current editor used to manage the grid
     */
    public CMouseListenerImpl(final int i, final int j, final PatternEditor editor) {
        this.row = i;
        this.column = j;
        this.editor = editor;
    }

    /**
     * This method checks if the user can edit the grid, in this case notifies the
     * editor and calls the methods to place a pattern or change the status of a
     * cell.
     */
    @Override
    public void mousePressedIsLeftWithoutControl() {
        if (this.editor.isEnabled()) {
            this.editor.setMouseBeingPressed(true);
            if (this.editor.isPlacingModeOn()) {
                this.editor.placeCurrentPattern(this.row, this.column);
            } else {
                this.editor.hit(this.row, this.column);
            }
        }
    }

    /**
     * This Method is not supported.
     */
    public void mousePressedIsLeftWithControl() {
    }

    /**
     * This method checks if the user can edit the grid and a pattern has been
     * chosen, in this case removes the pattern notifying the editor.
     */
    @Override
    public void mousePressedisRightWithControl() {
        if (this.editor.isEnabled() && this.editor.isPlacingModeOn()) {
            this.editor.removePatternToPlace();
        }
    }

    /**
     * This method checks if the user can edit the grid and a pattern has been
     * chosen, in this case rotates the pattern notifying the editor.
     */
    public void mousePressedisRightWithoutControl() {
        if (this.editor.isEnabled() && this.editor.isPlacingModeOn()) {
            this.editor.rotateCurrentPattern(1);
        }
    }

    /**
     * This method notifies the editor as the mouse left button is being pressed.
     */
    @Override
    public void mouseReleasedLeft() {
        this.editor.setMouseBeingPressed(false);
    }

    /**
     * This method is not supported.
     */
    @Override
    public void mouseReleasedRight() {
    }

    /**
     * This method checks if the user can edit the grid and the mouse entered a cell
     * while pressing a button, in that case calls the method to change the status
     * of the cell.
     */
    @Override
    public void mouseEnteredWhilePressingLeft() {
        if (this.editor.isEnabled() && this.editor.isMouseBeingPressed()) {
            this.editor.hit(this.row, this.column);
        }
    }

    /**
     * This method is not supported.
     */
    @Override
    public void mouseEnteredWhilePressingRight() {
    }

    /**
     * This method checks if the user can edit the grid and a pattern has been
     * chosen, in that case shows the pattern as it was on the grid.
     */
    @Override
    public void mouseEnteredWithoutPress() {
        if (this.editor.isEnabled() && this.editor.isPlacingModeOn()) {
            this.editor.showPreview(this.row, this.column);
        }
    }
}
