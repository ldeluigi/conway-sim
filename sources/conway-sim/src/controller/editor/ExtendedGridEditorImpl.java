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
        if (cutReady) {
            return Matrices.cut(this.getCurrentStatus(), lowX, hightX, lowY, hightY);
        }
        throw new IllegalStateException();
    }

    /**
     * If selectMode is enabled it controls the selection and the cut of the
     * pattern, otherwise {@inheritDoc}.
     */
    @Override
    public void hit(final int row, final int column) {
        if (selectMode) {
            if (firstCoordinateIsPresent) {
                final int saveRow = lastRow;
                final int saveCol = lastCol;
                setFirstCoordinate(row, column);
                showSelect(row, column, saveRow, saveCol);
                firstCoordinateIsPresent = false;
            } else {
                setFirstCoordinate(row, column);
                firstCoordinateIsPresent = true;
                cutReady = false;
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
        if (selectMode) {
            this.applyChanges();
            firstCoordinateIsPresent = false;
            selectMode = false;
            cutReady = false;
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
        lastCol = col;
        lastRow = row;
    }

    private void showSelect(final int newRow, final int newCol, final int lastRow, final int lastCol) {
        if (firstCoordinateIsPresent) {
            this.applyChanges();
            final int sizeCol = Math.abs(lastCol - newCol);
            final int sizeRow = Math.abs(lastRow - newRow);
            if (newRow < lastRow && newCol < lastCol) {
                lowX = lastRow - sizeRow;
                lowY = lastCol - sizeCol;
                hightX = lastRow;
                hightY = lastCol;
            } else if (newRow < lastRow && newCol > lastCol) {
                lowY = lastCol;
                lowX = lastRow - sizeRow;
                hightY = lastCol + sizeCol;
                hightX = lastRow;
            } else if (newRow > lastRow && newCol < lastCol) {
                lowY = lastCol - sizeCol;
                lowX = lastRow;
                hightY = lastCol;
                hightX = lastRow + sizeRow;
            } else {
                lowY = lastCol;
                lowX = lastRow;
                hightY = lastCol + sizeCol;
                hightX = lastRow + sizeRow;
            }
            for (int x = lowY; x <= hightY; x++) {
                this.getCurrentStatus().set(lowX, x, this.getCurrentStatus().get(lowX, x));
                this.getGameGrid().displaySingleCell(lowX, x, Colors.selectMode(this.getCurrentStatus().get(lowX, x)));
                this.getCurrentStatus().set(hightX, x, this.getCurrentStatus().get(hightX, x));
                this.getGameGrid().displaySingleCell(hightX, x,
                        Colors.selectMode(this.getCurrentStatus().get(hightX, x)));
            }
            for (int x = lowX; x <= hightX; x++) {
                this.getCurrentStatus().set(x, lowY, this.getCurrentStatus().get(x, lowY));
                this.getGameGrid().displaySingleCell(x, lowY, Colors.selectMode(this.getCurrentStatus().get(x, lowY)));
                this.getCurrentStatus().set(x, hightY, this.getCurrentStatus().get(x, hightY));
                this.getGameGrid().displaySingleCell(x, hightY,
                        Colors.selectMode(this.getCurrentStatus().get(x, hightY)));
            }
            cutReady = sizeRow > 2 && sizeCol > 2;
        }
    }
}
