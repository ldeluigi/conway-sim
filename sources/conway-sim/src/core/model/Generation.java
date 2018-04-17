package core.model;

import core.utils.Matrix;

/**
 * An interface for a cell generation.
 *
 */
public interface Generation {

	/**
	 * Returns the generations width.
	 * 
	 * @return width of the generation
	 */
	int getWidth();

	/**
	 * Returns the generations height.
	 * 
	 * @return height of the generation
	 */
	int getHeight();

	/**
	 * Returns the environment where the generation lives.
	 * 
	 * @return generation environment
	 */
	Environment getEnviroment();

	/**
	 * Returns the {@link Matrix<Cell>} of the generation.
	 * 
	 * @return cell matrix
	 */
	Matrix<Cell> getCellMatrix();
}
