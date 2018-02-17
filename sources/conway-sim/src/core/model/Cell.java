package core.model;

/**
 * This interface describes a cell of the Conway's Game of Life.
 */
public interface Cell {

    /**
     * A method that sets the next status of this cell.
     * @param nextstatus is the status of the cell to be assumed 
     */
    void setStatus(Status nextstatus);

    /**
     * A method that gets the current status of this cell.
     * @return the current status of the cell
     */
    Status getStatus();
}
