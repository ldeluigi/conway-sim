package core.model;

/**
 *
 * 
 * 
 */
public interface Cell {

    /**
     * 
     * @param nextstatus is the status of the cell to be assumed 
     */
    void setStatus(Status nextstatus);

    /**
     * 
     * @return the current status of the cell
     */
    Status getStatus();
}
