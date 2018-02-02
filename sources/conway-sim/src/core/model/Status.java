package core.model;

/**
 * Status describes the various types of cells and their current state.
 * 
 *
 */

public enum Status {
    /**
     * Dead means that the cell has not survived the generation 
     * and is not allowed to produce other living cells.
     */
    DEAD,
    /**
     * Alive means that the cell can produce other living cells.
     */
    ALIVE;
}
