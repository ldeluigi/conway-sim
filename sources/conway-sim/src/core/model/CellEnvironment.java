package core.model;

/**
 * This interface must be implemented by classes which use two methods to
 * determine if the cell should die or be born based on the number of neighbors.
 */
public interface CellEnvironment {

	/**
	 * Check for cell birth.
	 * 
	 * @param neighbors
	 *            of this cell
	 * @return true if the cell should be born
	 */
	boolean checkCellBorn(int neighbors);

	/**
	 * Check for cell death.
	 * 
	 * @param neighbors
	 *            of this cell
	 * @return true if the cell should die
	 */
	boolean checkCellDeath(int neighbors);
}
