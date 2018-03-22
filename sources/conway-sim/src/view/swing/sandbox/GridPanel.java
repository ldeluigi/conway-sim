package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import core.utils.ListMatrix;
import core.utils.Matrix;
import view.swing.menu.MenuSettings;


/**
 * GridPanel is the visual panel which displays a scrollable grid of mutating cells.
 * 
 */
public class GridPanel extends JScrollPane {

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
    private final Matrix<JLabel> labelMatrix;
    private final boolean shouldGridStayVisible;
    private final int maxCellSize;
    private final int minCellSize;
    private final int gridHeight;
    private final int gridWidth;

    /**
     * Is the constructor method for a new GridPanel.
     * @param width of the matrix
     * @param height of the matrix
     * @param startingCellSize the initial dimension for the square cell
     */
    public GridPanel(final int width, final int height, final int startingCellSize) {
        if (width < 1 || height < 1 || startingCellSize < 1) {
            throw new IllegalArgumentException("Arguments must be greater than 1.");
        }
        this.gridWidth = width;
        this.gridHeight = height;
        this.maxCellSize = startingCellSize * MAX_CELL_SIZE_RATIO;
        this.minCellSize = Math.max(startingCellSize / MIN_CELL_SIZE_RATIO, MIN_CELL_SIZE);
        this.cellSize = new Dimension(startingCellSize, startingCellSize);
        this.labelMatrix = new ListMatrix<>(width, height, () -> {
            final JLabel l = new JLabel("");
            l.setSize(cellSize);
            l.setPreferredSize(cellSize);
            l.setBackground(Color.WHITE);
            l.setOpaque(true);
            return l;
        });
        this.grid = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
            for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                c.gridx = j;
                c.gridy = i;
                setBorder(this.labelMatrix.get(i, j), i, j, this.borderColor, this.borderWidth);
                this.grid.add(this.labelMatrix.get(i, j), c);
            }
        }
        this.setDoubleBuffered(true);
        final JPanel gridWrapper = new JPanel();
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
            this.cellSize.setSize(this.cellSize.getWidth() + byPixels, this.cellSize.getHeight() + byPixels);
            this.labelMatrix.stream().forEach(label -> {
                label.setSize(this.cellSize);
                label.setPreferredSize(this.cellSize);
            });
            this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
            this.grid.setVisible(true);
    }

    private void setBorder(final JLabel label, final int row, final int col, final Color c, final int borderWidth)  {
        if (row == 0) { //already existing implementation
            if (col == 0) {
                label.setBorder(BorderFactory.createLineBorder(c, borderWidth));
            } else {
                label.setBorder(BorderFactory.createMatteBorder(borderWidth, 0, borderWidth,
                        borderWidth, c));
            }
        } else {
            if (col == 0) {
                label.setBorder(BorderFactory.createMatteBorder(0, borderWidth, borderWidth,
                        borderWidth, c));
            } else {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, borderWidth,
                        c));
            }
        }
    }

    /**
     * Is the method to invoke in order to color up the components of the grid.
     * @param colorMatrix is the matrix containing the colors to paint.
     */
    public void paintGrid(final Matrix<Color> colorMatrix) {
        if (this.labelMatrix.getHeight() != colorMatrix.getHeight() || this.labelMatrix.getWidth() != colorMatrix.getWidth()) {
            throw new IllegalArgumentException("Dimensions not corresponding.");
        }
            displayColors(colorMatrix, 0, 0);
    }

    private void displayColors(final Matrix<Color> colorMatrix, final int startRow, final int startColumn) {
        if (colorMatrix != null && startRow >= 0 && startColumn >= 0) {
            SwingUtilities.invokeLater(() -> {
                this.grid.setVisible(this.shouldGridStayVisible);
                IntStream.range(startRow, colorMatrix.getHeight()).forEach(line -> {
                    IntStream.range(startColumn, colorMatrix.getWidth()).forEach(column -> {
                        labelMatrix.get(line, column).setBackground(colorMatrix.get(line, column));
                    });
                });
                this.grid.setVisible(true);
            });
        } else {
            throw new IllegalArgumentException("Used not consistent parameter(s)");
        }
    }

    /**
     * Is the method which gives a color matrix showing the current grid.
     * @return the color matrix as overview of the current state of the grid.
     */
    public Matrix<Color> getColorMatrix() {
        return this.labelMatrix.map(l -> l.getBackground());
    }

    /**
     * Is the method to invoke in order to add an observer to the components of the grid.
     * @param listenerDispencer is the BiFunction dispensing the listener.
     */
    public void addListenerToGrid(final BiFunction<Integer, Integer, MouseListener> listenerDispencer) {
        for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
            for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                this.labelMatrix.get(i, j).addMouseListener(listenerDispencer.apply(i, j));
            }
        }
    }

    /**
     * Is the method to invoke in order to discover the number of horizontal cells.
     * @return the width of the grid.
     */
    public int getGridWidth() {
        return this.gridWidth;
    }

    /**
     * Is the method to invoke in order to discover the number of vertical cells.
     * @return the height of the grid.
     */
    public int getGridHeight() {
        return this.gridHeight;
    }
}
