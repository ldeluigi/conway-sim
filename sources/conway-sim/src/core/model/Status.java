package core.model;

/**
 * Status describes the current state of a {@link Cell}.
 * 
 */
public enum Status {
    /**
     * Dead means that the cell has not survived the generation 
     * and is not allowed to produce other living cells.
     */
    DEAD,
    /**
     * Alive means that the cell can produce other living cells or die, according to the rules specified by its environment.
     */
    ALIVE;
}
