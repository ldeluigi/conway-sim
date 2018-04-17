package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import core.model.Status;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

/**
 * 
 */
public class ExtendedGridEditorImpl extends GridEditorImpl implements ExtendedGridEditor {

    private static final Function<Status, Color> STANDARDCOLOR = s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
    private static final Function<Status, Color> SELECTMODE = s -> s.equals(Status.DEAD) ? Color.ORANGE : Color.RED;
    private Matrix<Status> currentStatus;
    private GridPanel gameGrid;
    private boolean selectMode;
    private boolean cutReady;
    private Optional<Boolean> oldPlacingState;
    private boolean mouseBeingPressed;

    //Coordinates for the gridSave
    private int lowX = 0;
    private int lowY = 0;
    private int hightX = 0;
    private int hightY = 0;
    private int lastRow = -1;
    private int lastCol = -1;

    /**
     * 
     * @param grid the initial grid.
     */
    public ExtendedGridEditorImpl(final GridPanel grid) {
        super(grid);
        this.gameGrid = grid;
        getGameGrid().addListenerToGrid((i, j) -> new ExtendedGridEditorImpl.CellListener(i, j));
    }

    /**
     * 
     */
    @Override
    public void changeSizes(final int horizontal, final int vertical) {
        super.changeSizes(horizontal, vertical);
        getGameGrid().addListenerToGrid((i, j) -> new ExtendedGridEditorImpl.CellListener(i, j));
    }

    /**
     * Important, call only if cutReady is true, else throw new {@link IllegalStateException}.
     */
    @Override
    public Matrix<Status> cutMatrix() {
        if (cutReady) {
            return Matrices.cut(this.currentStatus, lowX, hightX, lowY, hightY);
        }
        throw new IllegalStateException();
    }

    /**
     * Return true if the cut is ready.
     */
    @Override
    public void selectMode(final boolean flag) {
        if (!selectMode && flag) {
            oldPlacingState = Optional.of(this.isEnabled());
            this.setEnabled(false);
            selectMode = true;
        } else if (selectMode && cutReady && flag) {
            this.setEnabled(oldPlacingState.orElse(false));
            selectMode = false;
        }
    }

    /**
     * 
     */
    @Override
    public void cancelSelectMode() {
        if (selectMode) {
            selectMode = false;
            this.setEnabled(oldPlacingState.orElse(false));
            cutReady = false;
        }
    }

    @Override
    public boolean isCutReady() {
        return this.cutReady;
    }

    private void select(final int row, final int col) { //when click
        if (lastCol != -1 && lastRow != -1) {
            lastCol = -1;
            lastRow = -1;
        } else {
            cutReady = false;
            this.applyChanges();
            this.currentStatus.set(row, col, this.currentStatus.get(row, col));
            this.getGameGrid().displaySingleCell(row, col, SELECTMODE.apply(this.currentStatus.get(row, col)));
            if (lastRow > this.currentStatus.getWidth() || lastCol > this.currentStatus.getHeight()) {
                lastRow = -1;
                lastCol = -1;
            }
            lastCol = col;
            lastRow = row;
        }
    }

    private void showSelect(final int newRow, final int newCol) {
        if (lastCol > 0 && lastRow > 0) {
            this.applyChanges();
            int size = Math.min(Math.abs(lastCol - newCol), Math.abs(lastRow - newRow));
            if (newRow < lastRow && newCol < lastCol) {
                lowX = lastRow - size;
                lowY = lastCol - size;
                hightX = lastRow;
                hightY = lastCol;
            } else if (newRow < lastRow && newCol > lastCol) {
                lowY = lastCol;
                lowX = lastRow - size;
                hightY = lastCol + size;
                hightX = lastRow;
            } else if (newRow > lastRow && newCol < lastCol) {
                lowY = lastCol - size;
                lowX = lastRow;
                hightY = lastCol;
                hightX = lastRow + size;
            } else {
                lowY = lastCol;
                lowX = lastRow;
                hightY = lastCol + size;
                hightX = lastRow + size;
            }
            for (int x = lowY; x <= hightY; x++) {
                this.currentStatus.set(lowX, x, this.currentStatus.get(lowX, x));
                this.getGameGrid().displaySingleCell(lowX, x, SELECTMODE.apply(this.currentStatus.get(lowX, x)));
                this.currentStatus.set(hightX, x, this.currentStatus.get(hightX, x));
                this.getGameGrid().displaySingleCell(hightX, x, SELECTMODE.apply(this.currentStatus.get(hightX, x)));
            }
            for (int x = lowX; x <= hightX; x++) {
                this.currentStatus.set(x, lowY, this.currentStatus.get(x, lowY));
                this.getGameGrid().displaySingleCell(x, lowY, SELECTMODE.apply(this.currentStatus.get(x, lowY)));
                this.currentStatus.set(x, hightY, this.currentStatus.get(x, hightY));
                this.getGameGrid().displaySingleCell(x, hightY, SELECTMODE.apply(this.currentStatus.get(x, hightY)));
            }
            cutReady = size > 2 ? true : false;
        }
    }

    /**
     * 
     */
    @Override
    protected Matrix<Status> getCurrentStatus() {
        return this.currentStatus;
    }

    /**
     * 
     */
    @Override
    protected void setCurrentStatus(final Matrix<Status> newStatus) {
        this.currentStatus = newStatus;
    }

    /**
     * 
     */
    @Override
    protected GridPanel getGameGrid() {
        return this.gameGrid;
    }

    /**
     * 
     */
    @Override
    protected void addActionListenerToGridPanel() {
        this.getGameGrid().addListenerToGrid((i, j) -> new CellListener(i, j));
    }

    private void applyChanges() {
        this.getGameGrid().paintGrid(0, 0, this.getCurrentStatus().map(STANDARDCOLOR));
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
            if (selectMode) {
                select(row, column);
            } else if (SwingUtilities.isLeftMouseButton(e) && ExtendedGridEditorImpl.this.isEnabled()) {
                ExtendedGridEditorImpl.this.mouseBeingPressed = true;
                if (ExtendedGridEditorImpl.this.isPlacingModeOn()) {
                    ExtendedGridEditorImpl.this.placeCurrentPattern(this.row, this.column);
                } else {
                    ExtendedGridEditorImpl.this.hit(this.row, this.column);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && ExtendedGridEditorImpl.this.isEnabled()
                    && ExtendedGridEditorImpl.this.isPlacingModeOn()) {
                if (e.isControlDown()) {
                    ExtendedGridEditorImpl.this.removePatternToPlace();
                    ExtendedGridEditorImpl.this.applyChanges();
                } else {
                    ExtendedGridEditorImpl.this.rotateCurrentPattern(1);
                }
            }
        }

        /**
         * Is the method which notifies when mouse's left button is released.
         * @param e the event generated as result of the interaction with the grid
         */
        @Override
        public void mouseReleased(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                ExtendedGridEditorImpl.this.mouseBeingPressed = false;
            }
        }

        /**
         * Is the method which notifies when the user's cursor enters a cell of the grid. 
         * @param e the event generated as result of the interaction
         */
        @Override
        public void mouseEntered(final MouseEvent e) {
            if (selectMode) {
                ExtendedGridEditorImpl.this.showSelect(row, column);
            } else if (ExtendedGridEditorImpl.this.isEnabled()) {
                if (ExtendedGridEditorImpl.this.isPlacingModeOn()) {
                    ExtendedGridEditorImpl.this.showPreview(this.row, this.column);
                } else if (ExtendedGridEditorImpl.this.mouseBeingPressed && SwingUtilities.isLeftMouseButton(e)) {
                    ExtendedGridEditorImpl.this.hit(this.row, this.column);
                }
            }
        }

        /**
         * This method is not supported.
         */
        @Override
        public void mouseExited(final MouseEvent e) {
        }
    }
}
