package core.model;

/**
 * This class uses two strategies passed to constructor to determine if the cell should die or not based on the number of neighbors.
 *
 */
public interface CellEnvironment {

    /**
     * Check for cell birth.
     * @param neighbors of this cell
     * @return true if the cell should be born
     */
    boolean checkCellBorn(int neighbors);

    /**
     * Check for cell death.
     * @param neighbors of this cell
     * @return true if the cell should die
     */
    boolean checkCellDeath(int neighbors);
}
