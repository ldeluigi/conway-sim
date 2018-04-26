package controller.editor;

import core.model.Status;
import core.utils.Matrices;
import core.utils.Matrix;
import view.Colors;
import view.swing.GridPanel;

/**
 * This class extends {@link GridEditorImpl} to allow the selection and the cut
 * of a pattern of the grid.
 */
public class ExtendedGridEditorImpl extends GridEditorImpl implements ExtendedGridEditor {

    private boolean selectMode;
    private boolean firstCoordinateIsPresent;
    private boolean cutReady;

    private int lowX;
    private int lowY;
    private int hightX;
    private int hightY;
    private int lastRow;
    private int lastCol;

    /**
     * 
     * @param grid
     *            is the initial grid.
     */
    public ExtendedGridEditorImpl(final GridPanel grid) {
        super(grid);
    }

    /**
     * Important: call it only if cutReady is true, otherwise throw new
     * {@link IllegalStateException}.
     */
    @Override
    public Matrix<Status> cutMatrix() {
        if (this.cutReady) {
            return Matrices.cut(this.getCurrentStatus(), this.lowX, this.hightX, this.lowY, this.hightY);
        }
        throw new IllegalStateException();
    }

    /**
     * If selectMode is enabled it controls the selection and the cut of the
     * pattern, otherwise {@inheritDoc}.
     */
    @Override
    public void hit(final int row, final int column) {
        if (this.selectMode) {
            if (this.firstCoordinateIsPresent) {
                final int saveRow = this.lastRow;
                final int saveCol = this.lastCol;
                setFirstCoordinate(row, column);
                showSelect(row, column, saveRow, saveCol);
                this.firstCoordinateIsPresent = false;
            } else {
                setFirstCoordinate(row, column);
                this.firstCoordinateIsPresent = true;
                this.cutReady = false;
            }
        } else {
            super.hit(row, column);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectMode(final boolean flag) {
        this.selectMode = flag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelSelectMode() {
        if (this.selectMode) {
            this.applyChanges();
            this.firstCoordinateIsPresent = false;
            this.selectMode = false;
            this.cutReady = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCutReady() {
        return this.cutReady;
    }

    private void setFirstCoordinate(final int row, final int col) { // when click
        this.applyChanges();
        this.getGameGrid().displaySingleCell(row, col, Colors.selectMode(this.getCurrentStatus().get(row, col)));
        this.lastCol = col;
        this.lastRow = row;
    }

    private void showSelect(final int newRow, final int newCol, final int lastRow, final int lastCol) {
        if (firstCoordinateIsPresent) {
            this.applyChanges();
            final int sizeCol = Math.abs(lastCol - newCol);
            final int sizeRow = Math.abs(lastRow - newRow);
            if (newRow < lastRow && newCol < lastCol) {
                this.lowX = lastRow - sizeRow;
                this.lowY = lastCol - sizeCol;
                this.hightX = lastRow;
                this.hightY = lastCol;
            } else if (newRow < lastRow && newCol > lastCol) {
                this.lowY = lastCol;
                this.lowX = lastRow - sizeRow;
                this.hightY = lastCol + sizeCol;
                this.hightX = lastRow;
            } else if (newRow > lastRow && newCol < lastCol) {
                this.lowY = lastCol - sizeCol;
                this.lowX = lastRow;
                this.hightY = lastCol;
                this.hightX = lastRow + sizeRow;
            } else {
                this.lowY = lastCol;
                this.lowX = lastRow;
                this.hightY = lastCol + sizeCol;
                this.hightX = lastRow + sizeRow;
            }
            for (int x = this.lowY; x <= this.hightY; x++) {
                this.getCurrentStatus().set(this.lowX, x, this.getCurrentStatus().get(this.lowX, x));
                this.getGameGrid().displaySingleCell(this.lowX, x, Colors.selectMode(this.getCurrentStatus().get(this.lowX, x)));
                this.getCurrentStatus().set(this.hightX, x, this.getCurrentStatus().get(this.hightX, x));
                this.getGameGrid().displaySingleCell(this.hightX, x,
                        Colors.selectMode(this.getCurrentStatus().get(this.hightX, x)));
            }
            for (int x = this.lowX; x <= this.hightX; x++) {
                this.getCurrentStatus().set(x, this.lowY, this.getCurrentStatus().get(x, this.lowY));
                this.getGameGrid().displaySingleCell(x, this.lowY, Colors.selectMode(this.getCurrentStatus().get(x, this.lowY)));
                this.getCurrentStatus().set(x, this.hightY, this.getCurrentStatus().get(x, this.hightY));
                this.getGameGrid().displaySingleCell(x, this.hightY,
                        Colors.selectMode(this.getCurrentStatus().get(x, this.hightY)));
            }
            this.cutReady = sizeRow > 2 && sizeCol > 2;
        }
    }
}
