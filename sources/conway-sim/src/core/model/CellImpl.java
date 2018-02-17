package core.model;

/**
 * Implementation of the interface Cell.
 * 
 *
 */

public class CellImpl implements Cell {

    private Status current;

    /**
     * Constructor for a new Cell.
     * @param firstStatus is the status to be assumed the first time
     */
    public CellImpl(final Status firstStatus) {
        this.current = firstStatus;
    }
    /**
     * setStatus is the method to invoke in order to change the current status of the cell.
     * @param nextstatus is the new status to be assumed. 
     */

    public void setStatus(final Status nextstatus) {
        this.current = nextstatus;
    }

    /**
     * getStatus is the method to get the curent status of the cell.
     * @return the current status
     */
    public Status getStatus() {
      return this.current;
    }

}
