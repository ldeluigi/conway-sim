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
     * Method which manages a click on the left mouse button without pressing
     * control.
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
     * Method which manages a click on the right mouse button while pressing
     * control.
     */
    @Override
    public void mousePressedisRightWithControl() {
        if (this.editor.isEnabled() && this.editor.isPlacingModeOn()) {
            this.editor.removePatternToPlace();
        }
    }

    /**
     * Method which manages a click on the right mouse button without pressing
     * control.
     */
    public void mousePressedisRightWithoutControl() {
        if (this.editor.isEnabled() && this.editor.isPlacingModeOn()) {
            this.editor.rotateCurrentPattern(1);
        }
    }

    /**
     * Method which manages a left mouse button release.
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
     * Method which manages left mouse button being pressed while changing position
     * on the grid.
     */
    @Override
    public void mouseEnteredWhilePressingLeft() {
        if (this.editor.isEnabled() && this.editor.isMouseBeingPressed()) {
            this.editor.hit(this.row, this.column);
        }
    }

    /**
     * Method which manages right mouse button being pressed while changing position
     * on the grid.
     */
    @Override
    public void mouseEnteredWhilePressingRight() {
    }

    /**
     * Method which manages mouse motion while changing position on the grid.
     */
    @Override
    public void mouseEnteredWithoutPress() {
        if (this.editor.isEnabled() && this.editor.isPlacingModeOn()) {
            this.editor.showPreview(this.row, this.column);
        }
    }
}
