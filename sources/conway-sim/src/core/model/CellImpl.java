package core.model;

/**
 * Implementation of the interface Cell.
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
    @Override
    public void setStatus(final Status nextstatus) {
        this.current = nextstatus;
    }

    /**
     * getStatus is the method to get the current status of the cell.
     * @return the current status
     */
    @Override
    public Status getStatus() {
      return this.current;
    }

    /**
     * Returns a new {@link CellImpl} with the same status.
     */
    @Override
    public Cell copy() {
        return new CellImpl(this.getStatus());
    }

    /**
     * Describes Cell status with a string.
     */
    @Override
    public String toString() {
        return current.toString();
    }

    /**
     * It's the same as {@link Status}.hashCode().
     */
    @Override
    public int hashCode() {
        return (current == null) ? 0 : current.hashCode();
    }

    /**
     * Equals checks if the two cells have the same {@link Status}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CellImpl other = (CellImpl) obj;
        return current.equals(other.current);
    }
}
