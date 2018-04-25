package core.campaign;

import core.model.Cell;
import core.model.SimpleCell;
import core.model.Status;

/**
 * Implementation of {@link Cell} which is always alive or dead and can't be
 * changed.
 */
public class NeverChangingCell extends SimpleCell {

    /**
     * This is the code returned by {@link NeverChangingCell#code}.
     */
    public static final int NEVER_CHANGING_CODE = 3;

    /**
     * Constructor method for a new immutable Cell.
     * 
     * @param s
     *            the status to be used
     */
    public NeverChangingCell(final Status s) {
        super(s);
    }

    /**
     * Is the method to change the status of this cell. Won't work as this change is
     * not supported.
     * 
     * @param nextStatus
     */
    @Override
    public void setStatus(final Status nextStatus) {
    }

    /**
     * Is the method which gives an exact copy of this cell.
     */
    @Override
    public Cell copy() {
        return new NeverChangingCell(this.getStatus());
    }

    /**
     * Describes Cell status with a string.
     */
    @Override
    public String toString() {
        return "NeverChangingCell: " + getStatus().toString();
    }

    /**
     * Is the method which gives the code of this kind of cells.
     */
    @Override
    public int code() {
        return NeverChangingCell.NEVER_CHANGING_CODE;
    }

    /**
     * Equals checks if the two cells have the same {@link Status} and if they are
     * both {@link NeverChangingCell}.
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
        final NeverChangingCell other = (NeverChangingCell) obj;
        return this.getStatus().equals(other.getStatus());
    }

    /**
     * It's the same as {@link Status}.hashCode().
     */
    @Override
    public int hashCode() {
        return code() * 3 + super.hashCode();
    }
}
