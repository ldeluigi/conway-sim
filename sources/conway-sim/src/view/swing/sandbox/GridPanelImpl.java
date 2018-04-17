package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.menu.MenuSettings;

/**
 * GridPanel is the visual panel which displays a scrollable grid of mutating cells.
 * 
 */
public class GridPanelImpl extends JScrollPane implements GridPanel {

    private static final long serialVersionUID = -1;
    private static final int INITIAL_BORDER_WIDTH = 1;
    private static final Color INITIAL_BORDER_COLOR = Color.darkGray;
    private static final int MIN_CELL_SIZE_RATIO = 2;
    private static final int MAX_CELL_SIZE_RATIO = 4;
    private static final int MIN_CELL_SIZE = 5;

    private final Dimension cellSize;
    private final int borderWidth = INITIAL_BORDER_WIDTH;
    private final Color borderColor = INITIAL_BORDER_COLOR;
    private final JPanel grid;
    private final boolean shouldGridStayVisible;
    private final int maxCellSize;
    private final int minCellSize;
    private final GridBagConstraints c = new GridBagConstraints();

    private Matrix<JComponent> labelMatrix;

    /**
     * Is the constructor method for a new GridPanel.
     * 
     * @param width
     *            of the matrix
     * @param height
     *            of the matrix
     * @param startingCellSize
     *            the initial dimension for the square cell
     */
    public GridPanelImpl(final int width, final int height, final int startingCellSize) {
        if (width < 1 || height < 1 || startingCellSize < 1) {
            throw new IllegalArgumentException("Arguments must be greater than 1.");
        }

        this.maxCellSize = startingCellSize * MAX_CELL_SIZE_RATIO;
        this.minCellSize = Math.max(startingCellSize / MIN_CELL_SIZE_RATIO, MIN_CELL_SIZE);
        this.cellSize = new Dimension(startingCellSize, startingCellSize);
        this.labelMatrix = new ListMatrix<>(width, height,
                () -> new JCell(this.cellSize, Color.WHITE));
        this.grid = new JPanel(new GridBagLayout());
        this.grid.setOpaque(false);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        this.claspLabels();
        this.setDoubleBuffered(true);
        final JPanel gridWrapper = new JPanel();
        gridWrapper.setOpaque(false);
        gridWrapper.add(grid);
        this.setViewportView(gridWrapper);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
        this.getHorizontalScrollBar().setUnitIncrement(this.cellSize.width);
        this.shouldGridStayVisible = !MenuSettings.areTransitionsInstant();
        this.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    if (cellSize.getWidth() < this.maxCellSize) {
                        alterCellSize(1);
                    }
                } else {
                    if (cellSize.getWidth() > this.minCellSize) {
                        alterCellSize(-1);
                    }
                }
            }
        });
        this.getViewport().setOpaque(false);
        this.setOpaque(false);
    }

    /**
     * Alters Cell size value. Non-thread safe.
     * 
     * @param byPixels
     *            to add
     */
    public void alterCellSize(final int byPixels) {
        if (this.cellSize.getWidth() + byPixels <= 0 || this.cellSize.getHeight() + byPixels <= 0) {
            throw new IllegalStateException("Final Dimensions are 0 or less.");
        }
        this.grid.setVisible(false);
        this.cellSize.setSize(this.cellSize.getWidth() + byPixels,
                this.cellSize.getHeight() + byPixels);
        this.labelMatrix.stream().forEach(label -> {
            label.setSize(this.cellSize);
            label.setPreferredSize(this.cellSize);
        });
        this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
        this.grid.setVisible(true);
    }

    /**
     * 
     */
    @Override
    public void notifyToUser(final String s) { // TODO stringa?
        JOptionPane.showMessageDialog(this, s, "Error choosing pattern",
                JOptionPane.WARNING_MESSAGE);
    }

    private void setBorder(final JComponent jComponent, final int row, final int col, final Color c,
            final int borderWidth) {
        if (row == 0) { // already existing implementation
            if (col == 0) {
                jComponent.setBorder(BorderFactory.createLineBorder(c, borderWidth));
            } else {
                jComponent.setBorder(BorderFactory.createMatteBorder(borderWidth, 0, borderWidth,
                        borderWidth, c));
            }
        } else {
            if (col == 0) {
                jComponent.setBorder(BorderFactory.createMatteBorder(0, borderWidth, borderWidth,
                        borderWidth, c));
            } else {
                jComponent.setBorder(
                        BorderFactory.createMatteBorder(0, 0, borderWidth, borderWidth, c));
            }
        }
    }

    /**
     * Is the method to invoke in order to color up the components of the grid.
     * 
     * @param colorMatrix
     *            is the matrix containing the colors to paint.
     * @param startRow
     *            is the vertical index explaining where the colorMatrix should start being applied.
     * @param startCol
     *            is the horizontal index where the colorMatrix should start being applied.
     */
    public void paintGrid(final int startRow, final int startCol, final Matrix<Color> colorMatrix) {
        if (this.labelMatrix.getHeight() < colorMatrix.getHeight()
                || this.labelMatrix.getWidth() < colorMatrix.getWidth()) {
            throw new IllegalArgumentException("Dimensions not corresponding.");
        }
        this.displayColors(startRow, startCol, colorMatrix);
    }

    /**
     * Is the method to invoke when a single cell changes instead of repainting the whole grid.
     * 
     * @param row
     *            is the vertical index of the cell
     * @param column
     *            is the horizontal index of the cell
     * @param col
     *            is the color to be set as background for the given cell at (row,column) position
     */
    public void displaySingleCell(final int row, final int column, final Color col) {
        if (row >= 0 && column >= 0) {
            SwingUtilities.invokeLater(() -> {
                this.labelMatrix.get(row, column).setBackground(col);
            });
        } else {
            throw new IllegalArgumentException("Used not consistent parameter(s)");
        }
    }

    /**
     * Is the method to invoke in order to add an observer to the components of the grid.
     * 
     * @param listenerDispencer
     *            is the BiFunction dispensing the listener.
     */
    public void addListenerToGrid(
            final BiFunction<Integer, Integer, MouseListener> listenerDispencer) {
        for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
            for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                if (this.labelMatrix.get(i, j).getMouseListeners().length == 0) {
                    this.labelMatrix.get(i, j).addMouseListener(listenerDispencer.apply(i, j));
                }
            }
        }
    }

    /**
     * Is the method to invoke in order to discover the number of horizontal cells.
     * 
     * @return the width of the grid.
     */
    public int getGridWidth() {
        return this.labelMatrix.getWidth();
    }

    /**
     * Is the method to invoke in order to discover the number of vertical cells.
     * 
     * @return the height of the grid.
     */
    public int getGridHeight() {
        return this.labelMatrix.getHeight();
    }

    /**
     * Is the method to invoke in order to resize the grid inside the frame.
     * 
     * @param horizontal
     *            is the new number of columns
     * @param vertical
     *            is the new number of rows
     */
    public void changeGrid(final int horizontal, final int vertical) {
        if (SwingUtilities.isEventDispatchThread()) {
            resizeGrid(horizontal, vertical);
        } else {
            SwingUtilities.invokeLater(() -> resizeGrid(horizontal, vertical));
        }
    }

    private void displayColors(final int startRow, final int startColumn,
            final Matrix<Color> colorMatrix) {
        if (colorMatrix != null && startRow >= 0 && startColumn >= 0
                && this.labelMatrix.getHeight() >= colorMatrix.getHeight() + startRow
                && this.labelMatrix.getWidth() >= colorMatrix.getWidth() + startColumn) {
            SwingUtilities.invokeLater(() -> {
                this.grid.setVisible(this.shouldGridStayVisible);
                IntStream.range(0, colorMatrix.getHeight()).forEach(line -> {
                    IntStream.range(0, colorMatrix.getWidth()).forEach(column -> {
                        this.labelMatrix.get(line + startRow, column + startColumn)
                                .setBackground(colorMatrix.get(line, column));
                    });
                });
                this.grid.setVisible(true);
            });
        } else {
            throw new IllegalArgumentException("Used not consistent parameter(s).");
        }
    }

    private void resizeGrid(final int horizontal, final int vertical) {
        this.grid.setVisible(false);
        this.grid.removeAll();
        if (this.labelMatrix.getWidth() < horizontal) {
            if (this.labelMatrix.getHeight() < vertical) {
                this.labelMatrix = Matrices.mergeXY(
                        new ListMatrix<>(horizontal, vertical,
                                () -> new JCell(this.cellSize, Color.WHITE)),
                        0, 0, this.labelMatrix);
            } else {
                this.labelMatrix = Matrices.cut(this.labelMatrix, 0, vertical - 1, 0,
                        this.labelMatrix.getWidth() - 1);
                this.labelMatrix = Matrices.mergeXY(
                        new ListMatrix<>(horizontal, vertical,
                                () -> new JCell(this.cellSize, Color.WHITE)),
                        0, 0, this.labelMatrix);
            }
        } else {
            if (this.labelMatrix.getHeight() < vertical) {
                this.labelMatrix = Matrices.cut(this.labelMatrix, 0,
                        this.labelMatrix.getHeight() - 1, 0, horizontal - 1);
                this.labelMatrix = Matrices.mergeXY(
                        new ListMatrix<>(horizontal, vertical,
                                () -> new JCell(this.cellSize, Color.WHITE)),
                        0, 0, this.labelMatrix);
            } else {
                this.labelMatrix = Matrices.cut(this.labelMatrix, 0, vertical - 1, 0,
                        horizontal - 1);
            }
        }
        this.claspLabels();
        this.grid.setVisible(true);
    }

    private void claspLabels() {
        for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
            for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                c.gridx = j;
                c.gridy = i;
                GridPanelImpl.this.setBorder(this.labelMatrix.get(i, j), i, j, this.borderColor,
                        this.borderWidth);
                this.grid.add(this.labelMatrix.get(i, j), c);
            }
        }
    }
}
