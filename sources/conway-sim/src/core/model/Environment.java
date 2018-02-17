package core.model;

/**
 * This is the main environment for the generation.
 * 
 *
 */

public interface Environment {

    /**
     * Gets the height of {@link CellEnvironment} matrix.
     * @return the current height of the environment matrix
     */
    int getHeight();

    /**
     * Gets the height of {@link CellEnvironment} matrix.
     * @return the current height of the environment matrix
     */
    int getWidth();


    /**
     * 
     * @param x is the row where the {@link CellEnvironment} should be set
     * @param y is the column where the {@link CellEnvironment} should be set
     * @param env the {@link CellEnvironment} to be used
     */
    void setEnvironment(int x, int y, CellEnvironment env);

    /**
     * Returns the single cell environment from a position.
     * @param x is the row
     * @param y is the column
     * @return the {@link CellEnvironment} in that position
     */
    CellEnvironment getCellEnvironment(int x, int y);
}
