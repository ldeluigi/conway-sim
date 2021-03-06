package controller.editor;

import core.model.Generation;

/**
 * This interface represents a simple editor for a grid of Conway's Game of
 * Life.
 */
public interface SimpleGridEditor {

    /**
     * Displays the given generation on the grid.
     * 
     * @param gen
     *            the {@link Generation}
     */
    void draw(Generation gen);

    /**
     * Applies a {@link Status} change in the given position.
     * 
     * @param row
     *            the row
     * @param column
     *            the column
     * @throws IllegalSateException
     *             if not in placing mode
     */
    void hit(int row, int column);

    /**
     * Returns the first generation of the computation.
     * 
     * @return the {@link Generation} to compute
     */
    Generation getGeneration();

    /**
     * Set true if the editor is able to operate modifications on the grid.
     * 
     * @param enabled
     *            true if the editor allows editing operations
     */
    void setEnabled(Boolean enabled);

    /**
     * Returns true if the editor allows editing operations.
     * 
     * @return true if the editor allows editing operations
     */
    boolean isEnabled();

    /**
     * Resets the status grid to its initial state.
     */
    void clean();

    /**
     * Resizes the status grid to the given dimensions.
     * 
     * @param horizontal
     *            the number of columns
     * @param vertical
     *            the number of rows
     */
    void changeSizes(int horizontal, int vertical);
}
