package core.model;

/**
 * Implementation of the interface Cell.
 */
public class SimpleCell implements Cell {
    /**
     * This is the code returned by {@link SimpleCell#code}.
     */
    public static final int STANDARD_CELL_CODE = 1;

    private Status current;

    /**
     * Constructor for a new Cell.
     * 
     * @param firstStatus
     *            is the status to be assumed the first time
     */
    public SimpleCell(final Status firstStatus) {
        this.current = firstStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(final Status nextstatus) {
        this.current = nextstatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return this.current;
    }

    /**
     * Returns a new {@link SimpleCell} with the same status.
     */
    @Override
    public Cell copy() {
        return new SimpleCell(this.getStatus());
    }

    /**
     * Describes Cell status with a string.
     */
    @Override
    public String toString() {
        return "SimpleCell: " + current.toString();
    }

    /**
     * It's the same as {@link Status}.hashCode().
     */
    @Override
    public int hashCode() {
        return (current == null) ? 0 : current.hashCode();
    }

    /**
     * Equals checks if the two cells have the same {@link Status} and if they are
     * both {@link SimpleCell}.
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
        final SimpleCell other = (SimpleCell) obj;
        return this.getStatus().equals(other.getStatus());
    }

    /**
     * Returns {@link SimpleCell#STANDARD_CELL_CODE}.
     */
    @Override
    public int code() {
        return SimpleCell.STANDARD_CELL_CODE;
    }
}
