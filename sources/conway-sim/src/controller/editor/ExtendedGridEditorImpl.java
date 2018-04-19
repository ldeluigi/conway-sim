package controller.editor;

import java.awt.Color;
import java.util.function.Function;

import core.model.Status;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

/**
 * 
 */
public class ExtendedGridEditorImpl extends GridEditorImpl implements ExtendedGridEditor {

    private static final Function<Status, Color> SELECT = s -> s.equals(Status.DEAD) ? Color.ORANGE : Color.RED;
    private boolean selectMode;
    private boolean firstCoordinatePresent;
    private int lowX = 0;
    private int lowY = 0;
    private int hightX = 0;
    private int hightY = 0;
    private int lastRow;
    private int lastCol;
    private boolean cutReady;

    /**
     * 
     * @param grid the initial grid.
     */
    public ExtendedGridEditorImpl(final GridPanel grid) {
        super(grid);
    }

    /**
     * 
     */
    @Override
    public void changeSizes(final int horizontal, final int vertical) {
        super.changeSizes(horizontal, vertical);
    }

    /**
     * Important, call only if cutReady is true, else throw new {@link IllegalStateException}.
     */
    @Override
    public Matrix<Status> cutMatrix() {
        if (cutReady) {
            return Matrices.cut(this.getCurrentStatus(), lowX, hightX, lowY, hightY);
        }
        throw new IllegalStateException();
    }

    /**
     * 
     */
    @Override
    public void hit(final int row, final int column) {
        if (selectMode) {
            if (firstCoordinatePresent) {
                final int saveRow = lastRow;
                final int saveCol = lastCol;
                setFirstCoordinate(row, column);
                showSelect(row, column, saveRow, saveCol);
                firstCoordinatePresent = false;
            } else {
                setFirstCoordinate(row, column);
                firstCoordinatePresent = true;
                cutReady = false;
            }
        } else {
            super.hit(row, column);
        }
    }

    /**
     * Set selectMode with enable/disable.
     * If selectMode is enable, u can't change status cell, but u can see what is ready for the cut.
     */
    @Override
    public void selectMode(final boolean flag) {
        this.selectMode = flag;
    }

    /**
     * Exit from select mode.
     */
    @Override
    public void cancelSelectMode() {
        if (selectMode) {
            this.applyChanges();
            firstCoordinatePresent = false;
            selectMode = false;
            cutReady = false;
        }
    }

    /**
     * 
     */
    @Override
    public boolean isCutReady() {
        return this.cutReady;
    }

    private void setFirstCoordinate(final int row, final int col) { //when click
        this.applyChanges();
        this.getGameGrid().displaySingleCell(row, col, SELECT.apply(this.getCurrentStatus().get(row, col)));
        lastCol = col;
        lastRow = row;
    }

    private void showSelect(final int newRow, final int newCol, final int lastRow, final int lastCol) {
        if (firstCoordinatePresent) {
            this.applyChanges();
            int sizeCol = Math.abs(lastCol - newCol);
            int sizeRow =  Math.abs(lastRow - newRow);
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
                this.getGameGrid().displaySingleCell(lowX, x, SELECT.apply(this.getCurrentStatus().get(lowX, x)));
                this.getCurrentStatus().set(hightX, x, this.getCurrentStatus().get(hightX, x));
                this.getGameGrid().displaySingleCell(hightX, x, SELECT.apply(this.getCurrentStatus().get(hightX, x)));
            }
            for (int x = lowX; x <= hightX; x++) {
                this.getCurrentStatus().set(x, lowY, this.getCurrentStatus().get(x, lowY));
                this.getGameGrid().displaySingleCell(x, lowY, SELECT.apply(this.getCurrentStatus().get(x, lowY)));
                this.getCurrentStatus().set(x, hightY, this.getCurrentStatus().get(x, hightY));
                this.getGameGrid().displaySingleCell(x, hightY, SELECT.apply(this.getCurrentStatus().get(x, hightY)));
            }
            cutReady = sizeRow > 2 && sizeCol > 2;
        }
    }
}
