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
     * Is the method which gives the code for this kind of cells.
     */
    @Override
    public int code() {
        return GameWinningCell.GAME_WINNING_CODE;
    }

}
