package core.campaign;

import core.model.Cell;
import core.model.SimpleCell;
import core.model.Status;

/**
 * Implementation of {@link Cell} which makes the user win the game when
 * involved in the solution of the level.
 */
public class GameWinningCell extends SimpleCell {

    /**
     * This is the code returned by {@link SimpleCell#code}.
     */
    public static final int GAME_WINNING_CODE = 4;

    /**
     * Constructor method for a game winning cell.
     * 
     * @param state
     *            is the Status of the new cell
     */
    public GameWinningCell(final Status state) {
        super(state);
    }

    /**
     * Is the method which gives a exact copy of the cell.
     */
    @Override
    public Cell copy() {
        return new GameWinningCell(this.getStatus());
    }

    /**
     * Describes Cell status with a string.
     */
    @Override
    public String toString() {
        return "GameWinningCell: " + getStatus().toString();
    }

    /**
     * Is the method which gives the code for this kind of cells.
     */
    @Override
    public int code() {
        return GameWinningCell.GAME_WINNING_CODE;
    }

    /**
     * Equals checks if the two cells have the same {@link Status} and if they are
     * both {@link GameWinningCell}.
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
        final GameWinningCell other = (GameWinningCell) obj;
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
