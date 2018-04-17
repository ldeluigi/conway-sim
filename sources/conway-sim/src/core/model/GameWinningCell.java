package core.model;

/**
 * Implementation of {@link Cell} which makes the user win the game when
 * involved in the development of the level.
 *
 */
public class GameWinningCell implements Cell {

    /**
     * This is the code returned by {@link SimpleCell#code}.
     */
    public static final int GAME_WINNING_CODE = 4;

    private Status currentStatus;
    private Runnable born;
    private Runnable death;

    /**
     * Constructor method for a game winning cell.
     * 
     * @param state
     *            is the Status of the new cell
     * @param born
     *            is the Runnable to be used when the Status is changed from dead to
     *            alive
     * @param death
     *            is the runnable to be used when the Status is changed from alive
     *            to dead
     */
    public GameWinningCell(final Status state, final Runnable born, final Runnable death) {
        this.currentStatus = state;
        this.born = born;
        this.death = death;
    }

    /**
     * Is the method which changes the current status of the Cell, this event has
     * some consequences depending on the level.
     * 
     * @param nextStatus
     *            is the new status to be taken
     */
    @Override
    public void setStatus(final Status nextStatus) {
        this.currentStatus = nextStatus;
        if (nextStatus != this.currentStatus) {
            if (nextStatus == Status.DEAD) {
                this.death.run();
            } else {
                this.born.run();
            }
        }
    }

    /**
     * Is the method which gives the current status of the cell.
     */
    @Override
    public Status getStatus() {
        return this.currentStatus;
    }

    /**
     * Is the method which gives a complete copy of the cell.
     */
    @Override
    public Cell copy() {
        return new GameWinningCell(this.currentStatus, born, death);
    }

    /**
     * Is the method which gives the code for this kind of cells.
     */
    @Override
    public int code() {
        return GameWinningCell.GAME_WINNING_CODE;
    }

}
