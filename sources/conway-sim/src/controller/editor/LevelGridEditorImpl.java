package controller.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core.campaign.CellType;
import core.campaign.Editable;
import core.campaign.Level;
import view.swing.sandbox.GridPanel;

/**
 * 
 */
public class LevelGridEditorImpl extends GridEditorImpl {

    private final Level currentLevel;

    /**
     * 
     * @param grid the initial grid
     * @param level the level to be loaded
     */
    public LevelGridEditorImpl(final GridPanel grid, final Level level) {
        super(grid);
        //disable the old placingState and set enable on the new one
        grid.changeGrid(level.getEnvironmentMatrix().getWidth(), level.getEnvironmentMatrix().getHeight());
        grid.addListenerToGrid((i, j) -> new CellListener(i, j));
        this.currentLevel = level;
    }

    class CellListener implements MouseListener {

        private final int row;
        private final int column;
        private Editable editable;
        private CellType cellType;

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
