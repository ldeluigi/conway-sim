package core.model;

/**
 * This is the main environment for the generation.
 * 
 */

public interface Environment {

	/**
	 * Gets the height of {@link CellEnvironment} matrix.
	 * 
	 * @return the current height of the environment matrix
	 */
	int getHeight();

	/**
	 * Gets the height of {@link CellEnvironment} matrix.
	 * 
	 * @return the current height of the environment matrix
	 */
	int getWidth();

	/**
	 * Returns the single cell environment from a position.
	 * 
	 * @param x
	 *            is the row
	 * @param y
	 *            is the column
	 * @return the {@link CellEnvironment} in that position
	 */
	CellEnvironment getCellEnvironment(int x, int y);
}
