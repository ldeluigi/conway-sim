package controller.editor;

import core.model.Status;
import core.utils.Matrix;

/**
 * Represents a {@link GridEditor} able to place, show preview and rotate patterns of Conway's Game of Life.
 */
public interface PatternEditor extends GridEditor {

    /**
     * Shows a preview of the current pattern in the given position.
     * @param row the row
     * @param column the column
     * @throws {@link IllegalStateException} if there is no pattern that waits to be placed
     */
    void showPreview(int row, int column);

    /**
     * Starts the placement of the given pattern into the grid.
     * @param statusMatrix that describes the {@link Status} of the cells
     */
    void addPatternToPlace(Matrix<Status> statusMatrix);

    /**
     * Ends the placement of the current pattern in the given position, permanently.
     * @param row the row
     * @param column the column
     */
    void placeCurrentPattern(int row, int column);

    /**
     * Returns true if there is a pattern that is waiting to be placed.
     * @return true only if a pattern is waiting
     */
    boolean isPlacing();

    /**
     * Rotates 90 degrees clockwise the pattern.
     * @throws IllegalStateException if there is no pattern that waits to be placed
     */
    void rotateCurrentPattern();

    /**
     * Removes, if possible, the current pattern that is waiting to be placed.
     */
    void removePatternToPlace();
}
