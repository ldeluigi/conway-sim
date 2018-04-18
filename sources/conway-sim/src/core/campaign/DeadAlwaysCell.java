package core.campaign;

import core.model.Cell;
import core.model.SimpleCell;
import core.model.Status;

/**
 * Implementation of {@link Cell} which is always dead and can't be brought to
 * life.
 */
public class DeadAlwaysCell extends SimpleCell {

    /**
     * This is the code returned by {@link SimpleCell#code}.
     */
    public static final int DEAD_ALWAYS_CODE = 2;

    /**
     * Constructor method for a new dead Cell. No parameters needed as it won't
     * change.
     */
    public DeadAlwaysCell() {
    	super(Status.DEAD);
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
     * Is the method which gives the current Status of this cell.
     */
    @Override
    public Status getStatus() {
    	return super.getStatus();
    }

    /**
     * Is the method which gives an exact copy of this cell.
     */
    @Override
    public Cell copy() {
        return new DeadAlwaysCell();
    }

    /**
     * Is the method which gives the code of this kind of cells.
     */
    @Override
    public int code() {
        return DeadAlwaysCell.DEAD_ALWAYS_CODE;
    }

}
