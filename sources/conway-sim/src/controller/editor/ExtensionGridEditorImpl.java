package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Function;

import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

/**
 * 
 */
public class ExtensionGridEditorImpl extends GridEditorImpl implements ExtensionGridEditor {

    private static final Function<Status, Color> SELECT = s -> s.equals(Status.DEAD) ? Color.ORANGE : Color.RED;
    private boolean cutMode = false;
    private boolean selectMode = false;
    private int lowX = 0;
    private int lowY = 0;
    private int hightX = 0;
    private int hightY = 0;
    private int lastRow = -1;
    private int lastCol = -1;
    private boolean cutReady;
    private boolean oldPlacingState;

    /**
     * 
     * @param grid the initial grid.
     */
    public ExtensionGridEditorImpl(final GridPanel grid) {
        super(grid);
        gameGrid.addListenerToGrid((i, j) -> new ExtensionGridEditorImpl.CellListener(i, j));
    }

    /**
     * 
     */
    @Override
    public void changeSizes(final int horizontal, final int vertical) {
        super.changeSizes(horizontal, vertical);
        gameGrid.addListenerToGrid((i, j) -> new ExtensionGridEditorImpl.CellListener(i, j));
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
            oldPlacingState = placingState;
            placingState = false;
            selectMode = true;
        } else if (selectMode && cutReady && flag) {
            placingState = oldPlacingState;
            selectMode = false;
        } else if (!flag) {
            cutReady = false;
            placingState = oldPlacingState;
            selectMode = false;
            applyChanges();
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
            this.gameGrid.displaySingleCell(row, col, SELECT.apply(this.currentStatus.get(row, col)));
            if (lastRow > this.currentStatus.getWidth() || lastCol > this.currentStatus.getHeight()) {
                lastRow = -1;
                lastCol = -1;
            }
            lastCol = col;
            lastRow = row;
        }
    }

    //TODO refactor!!! important!!!
    private void showSelect(final int newRow, final int newCol) {
        if (lastCol > 0 && lastRow > 0) {
            this.applyChanges();
            int size = Math.min(Math.abs(lastCol - newCol), Math.abs(lastRow - newRow));
            if (newRow < lastRow && newCol < lastCol) {
                size = -size;
                lowX = lastRow + size;
                lowY = lastCol + size;
                hightX = lastRow;
                hightY = lastCol;
                for (int x = lastCol + size; x <= lastCol; x++) {
                    this.currentStatus.set(lastRow, x, this.currentStatus.get(lastRow, x));
                    this.gameGrid.displaySingleCell(lastRow, x, SELECT.apply(this.currentStatus.get(lastRow, x)));
                    this.currentStatus.set(lastRow + size, x, this.currentStatus.get(lastRow + size, x));
                    this.gameGrid.displaySingleCell(lastRow + size, x, SELECT.apply(this.currentStatus.get(lastRow + size, x)));
                }
                for (int x = lastRow + size; x <= lastRow; x++) {
                    this.currentStatus.set(x, lastCol, this.currentStatus.get(x, lastCol));
                    this.gameGrid.displaySingleCell(x, lastCol, SELECT.apply(this.currentStatus.get(x, lastCol)));
                    this.currentStatus.set(x, lastCol + size, this.currentStatus.get(x, lastCol + size));
                    this.gameGrid.displaySingleCell(x, lastCol + size, SELECT.apply(this.currentStatus.get(x, lastCol + size)));
                }
                size = -size;
            } else if (newRow < lastRow && newCol > lastCol) {
                lowY = lastCol;
                lowX = lastRow - size;
                hightY = lastCol + size;
                hightX = lastRow;
                for (int x = lastCol; x <= lastCol + size; x++) {
                    this.currentStatus.set(lastRow, x, this.currentStatus.get(lastRow, x));
                    this.gameGrid.displaySingleCell(lastRow, x, SELECT.apply(this.currentStatus.get(lastRow, x)));
                    this.currentStatus.set(lastRow - size, x, this.currentStatus.get(lastRow - size, x));
                    this.gameGrid.displaySingleCell(lastRow - size, x, SELECT.apply(this.currentStatus.get(lastRow - size, x)));
                }
                for (int x = lastRow - size; x <= lastRow; x++) {
                    this.currentStatus.set(x, lastCol, this.currentStatus.get(x, lastCol));
                    this.gameGrid.displaySingleCell(x, lastCol, SELECT.apply(this.currentStatus.get(x, lastCol)));
                    this.currentStatus.set(x, lastCol + size, this.currentStatus.get(x, lastCol + size));
                    this.gameGrid.displaySingleCell(x, lastCol + size, SELECT.apply(this.currentStatus.get(x, lastCol + size)));
                }
            } else if (newRow > lastRow && newCol < lastCol) {
                lowY = lastCol - size;
                lowX = lastRow;
                hightY = lastCol;
                hightX = lastRow + size;
                for (int x = lastCol - size; x <= lastCol; x++) {
                    this.currentStatus.set(lastRow, x, this.currentStatus.get(lastRow, x));
                    this.gameGrid.displaySingleCell(lastRow, x, SELECT.apply(this.currentStatus.get(lastRow, x)));
                    this.currentStatus.set(lastRow + size, x, this.currentStatus.get(lastRow + size, x));
                    this.gameGrid.displaySingleCell(lastRow + size, x, SELECT.apply(this.currentStatus.get(lastRow + size, x)));
                }
                for (int x = lastRow; x <= lastRow + size; x++) {
                    this.currentStatus.set(x, lastCol, this.currentStatus.get(x, lastCol));
                    this.gameGrid.displaySingleCell(x, lastCol, SELECT.apply(this.currentStatus.get(x, lastCol)));
                    this.currentStatus.set(x, lastCol - size, this.currentStatus.get(x, lastCol - size));
                    this.gameGrid.displaySingleCell(x, lastCol - size, SELECT.apply(this.currentStatus.get(x, lastCol - size)));
                }
            } else {
                lowY = lastCol;
                lowX = lastRow;
                hightY = lastCol + size;
                hightX = lastRow + size;
                for (int x = lastCol; x <= lastCol + size; x++) {
                    this.currentStatus.set(lastRow, x, this.currentStatus.get(lastRow, x));
                    this.gameGrid.displaySingleCell(lastRow, x, SELECT.apply(this.currentStatus.get(lastRow, x)));
                    this.currentStatus.set(lastRow + size, x, this.currentStatus.get(lastRow + size, x));
                    this.gameGrid.displaySingleCell(lastRow + size, x, SELECT.apply(this.currentStatus.get(lastRow + size, x)));
                }
                for (int x = lastRow; x <= lastRow + size; x++) {
                    this.currentStatus.set(x, lastCol, this.currentStatus.get(x, lastCol));
                    this.gameGrid.displaySingleCell(x, lastCol, SELECT.apply(this.currentStatus.get(x, lastCol)));
                    this.currentStatus.set(x, lastCol + size, this.currentStatus.get(x, lastCol + size));
                    this.gameGrid.displaySingleCell(x, lastCol + size, SELECT.apply(this.currentStatus.get(x, lastCol + size)));
                }
            }
            cutReady = size > 2 ? true : false;
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
            if (cutMode) {
                if (lowX == lowY && lowX == 0) {
                    lowY = row;
                    lowX = column;
                } else if (hightX == hightY && hightX == 0) {
                    hightY = row;
                    hightX = column;
                } else {
                    cutMode = false;
                }
            }
        }

        /**
         * Is the method which notifies where and how the user interacted with the grid.
         * @param e the event generated as result of the interaction
         */
        @Override
        public void mousePressed(final MouseEvent e) {
            if (selectMode) {
                select(row, column);
            }
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
            if (selectMode) {
                ExtensionGridEditorImpl.this.showSelect(row, column);
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