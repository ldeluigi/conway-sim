package core.model;

/**
 * This interface describes a cell of the Conway's Game of Life.
 */
public interface Cell {

    /**
     * A method that sets the next status of this cell.
     * 
     * @param nextStatus
     *            is the status of the cell to be assumed
     */
    void setStatus(Status nextStatus);

    /**
     * A method that gets the current status of this cell.
     * 
     * @return the current status of the cell
     */
    Status getStatus();

    /**
     * A method that return an exact copy of itself.
     * 
     * @return a copy of itself
     */
    Cell copy();

    /**
     * This method returns an integer that represents the type of this cell.
     * Implementing classes that are supposed to have a special representation
     * should return a unique code that identifies its cells. Otherwise, they can
     * return an already existing code to replicate an already set representation.
     * 
     * @return an integer based on this cell behavior, that distinguishes, for
     *         example, its class or type
     */
    int code();
}
