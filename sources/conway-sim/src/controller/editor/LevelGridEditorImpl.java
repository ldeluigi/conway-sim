package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ColorModel;
import java.util.function.BiFunction;

import javax.swing.SwingUtilities;
import javax.swing.text.StyleConstants.ColorConstants;

import core.campaign.CellType;
import core.campaign.Editable;
import core.campaign.Level;
import core.campaign.LevelImplTest;
import core.model.Status;
import view.swing.sandbox.GridPanel;

/**
 * 
 */
public class LevelGridEditorImpl extends GridEditorImpl {

    private final Level currentLevel;
    private static final BiFunction<Status, CellType, Color> LEVELCOLOR = (status, type) -> {
        if (type.equals(CellType.NORMAL)) {
            return status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
        } else if (type.equals(CellType.GOLDEN)) {
            return Color.YELLOW;
        } else if (type.equals(CellType.ALIVE_WALL)) {
            return new Color(153, 76, 0);
        } else if (type.equals(CellType.DEAD_WALL)) {
            return Color.GREEN;
        }
        return Color.RED;
    };

    /**
     * 
     * @param grid the initial grid
     * @param level the level to be loaded
     */
    public LevelGridEditorImpl(final GridPanel grid, final Level level) {
        super(grid);
        grid.changeGrid(level.getEnvironmentMatrix().getWidth(), level.getEnvironmentMatrix().getHeight());
        grid.addListenerToGrid((i, j) -> new CellListener(i, j));
        this.currentLevel = new LevelImplTest();
    }

    @Override
    public void hit(final int row, final int col) {
        if (!this.placingState) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        if (this.currentLevel.getEditableMatrix().get(row, col).equals(Editable.EDITABLE)) {
            this.currentStatus.set(row, col, this.currentStatus.get(row, col).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
            this.getGameGrid().displaySingleCell(row, col, LEVELCOLOR.apply(this.currentStatus.get(row, col), this.currentLevel.getCellTypeMatrix().get(row, col)));
        }
    }

    class CellListener implements MouseListener {

        private final int row;
        private final int column;

        /**
         * Is the constructor method which creates a new Listener.
         * @param i is the vertical index of the cell.
         * @param j is the horizontal index of the cell.
         */
        CellListener(final int i, final int j) {
            this.row = i;
            this.column = j;
        }

        /**
         * This method is not supported.
         */
        @Override
        public void mouseClicked(final MouseEvent e) {
        }

        /**
         * Is the method which notifies where and how the user interacted with the grid.
         * @param e the event generated as result of the interaction
         */
        @Override
        public void mousePressed(final MouseEvent e) {
        }

        /**
         * Is the method which notifies when mouse's left button is released.
         * @param e the event generated as result of the interaction with the grid
         */
        @Override
        public void mouseReleased(final MouseEvent e) {
        }

        /**
         * Is the method which notifies when the user's cursor enters a cell of the grid. 
         * @param e the event generated as result of the interaction
         */
        @Override
        public void mouseEntered(final MouseEvent e) {
        }

        /**
         * This method is not supported.
         */
        @Override
        public void mouseExited(final MouseEvent e) {
        }
    }
}
