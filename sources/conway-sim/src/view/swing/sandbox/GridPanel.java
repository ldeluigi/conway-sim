package view.swing.sandbox;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.function.BiFunction;

import javax.swing.JScrollPane;

import core.utils.Matrix;

/**
 * Abstract class that can represent matrices as grids.
 */
public interface GridPanel {


    /**
     * Alters Cell size value. Non-thread safe.
     * 
     * @param byPixels
     *            to add
     */
    void alterCellSize(int byPixels);

    /**
     * Is the method to invoke in order to color up the components of the grid.
     * 
     * @param colorMatrix
     *            is the matrix containing the colors to paint.
     * @param startRow
     *            is the vertical index explaining where the colorMatrix should
     *            start being applied.
     * @param startCol
     *            is the horizontal index where the colorMatrix should start being
     *            applied.
     */
    void paintGrid(int startRow, int startCol, Matrix<Color> colorMatrix);

    /**
     * Is the method to invoke when a single cell changes instead of repainting the
     * whole grid.
     * 
     * @param row
     *            is the vertical index of the cell
     * @param column
     *            is the horizontal index of the cell
     * @param col
     *            is the color to be set as background for the given cell at
     *            (row,column) position
     */
    void displaySingleCell(int row, int column, Color col);

    /**
     * Is the method to invoke in order to add an observer to the components of the
     * grid.
     * 
     * @param listenerDispencer
     *            is the BiFunction dispensing the listener.
     */
    void addListenerToGrid(BiFunction<Integer, Integer, MouseListener> listenerDispencer);

    /**
     * Is the method to invoke in order to discover the number of horizontal cells.
     * 
     * @return the width of the grid.
     */
    int getGridWidth();

    /**
     * Is the method to invoke in order to discover the number of vertical cells.
     * 
     * @return the height of the grid.
     */
    int getGridHeight();

    /**
     * Is the method to invoke in order to resize the grid inside the frame.
     * 
     * @param horizontal
     *            is the new number of columns
     * @param vertical
     *            is the new number of rows
     */
    void changeGrid(int horizontal, int vertical);

    /**
     * 
     * @param s
     */
    void notifyToUser(String s);
}
