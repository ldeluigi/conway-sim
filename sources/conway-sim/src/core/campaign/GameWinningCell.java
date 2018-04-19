package core.campaign;

import core.model.Cell;
import core.model.SimpleCell;
import core.model.Status;

/**
 * Implementation of {@link Cell} which makes the user win the game when involved in the development
 * of the level.
 *
 */
public class GameWinningCell extends SimpleCell {

    /**
     * This is the code returned by {@link SimpleCell#code}.
     */
    public static final int GAME_WINNING_CODE = 4;

    private final Runnable born;
    private final Runnable death;

    /**
     * Constructor method for a game winning cell.
     * 
     * @param state
     *            is the Status of the new cell
     * @param born
     *            is the Runnable to be used when the Status is changed from dead to alive
     * @param death
     *            is the runnable to be used when the Status is changed from alive to dead
     */
    public GameWinningCell(final Status state, final Runnable born, final Runnable death) {
        super(state);
        this.born = born;
        this.death = death;
    }

    /**
     * Is the method which changes the current status of the Cell, this event has some consequences
     * depending on the level.
     * 
     * @param nextStatus
     *            is the new status to be taken
     */
    @Override
    public void setStatus(final Status nextStatus) {
        if (nextStatus != this.getStatus()) {
            super.setStatus(nextStatus);
            if (nextStatus == Status.DEAD) {
                this.death.run();
            } else {
                this.born.run();
            }
        }
    }

    /**
     * Is the method which gives a complete copy of the cell.
     */
    @Override
    public Cell copy() {
        return new GameWinningCell(this.getStatus(), born, death);
    }

    /**
     * Is the method which gives the code for this kind of cells.
     */
    @Override
    public int code() {
        return GameWinningCell.GAME_WINNING_CODE;
    }

}
