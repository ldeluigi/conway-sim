package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
 * 
 * 
 */
public class GridPanel extends JScrollPane {

    private static final long serialVersionUID = -1;
    private static final int INITIAL_SIZE = 20;
    private static final int INITIAL_BORDER_WIDTH = 1;
    private static final Color INITIAL_BORDER_COLOR = Color.darkGray;

    private final Dimension cellSize = new Dimension(INITIAL_SIZE, INITIAL_SIZE);
    private int borderWidth = INITIAL_BORDER_WIDTH;
    private final Color borderColor = INITIAL_BORDER_COLOR;
    private final JPanel grid;
    private final Matrix<JLabel> labelMatrix;
    private final boolean shouldGridStayVisible;
    /**
     * 
     * @param width of the matrix
     * @param height of the matrix
     */
    public GridPanel(final int width, final int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Arguments must be greater than 1.");
        }
        this.labelMatrix = new ListMatrix<>(width, height, () -> {
            final JLabel l = new JLabel("");
            l.setSize(cellSize);
            l.setPreferredSize(cellSize);
            l.setBackground(Color.white);
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
                this.labelMatrix.get(i, j).addMouseListener(new GridPanel.CellListener(i, j));
                setBorder(this.labelMatrix.get(i, j), i, j, this.borderColor, this.borderWidth);
                this.grid.add(this.labelMatrix.get(i, j), c);
            }
        }
        this.setDoubleBuffered(true);
        this.setViewportView(grid);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
        this.shouldGridStayVisible = !MenuSettings.areTransitionsInstant(); //cambiare con getter-setter
    }
    /**
     * Alters Cell size value.
     * @param byPixels to add
     */
    public void alterCellSize(final int byPixels) {
        SwingUtilities.invokeLater(() -> {
                this.grid.setVisible(false);
            if (this.cellSize.getWidth() + byPixels <= 0 || this.cellSize.getHeight() + byPixels <= 0) {
                throw new IllegalStateException("Final Dimensions are 0 or less.");
            }
            this.cellSize.setSize(this.cellSize.getWidth() + byPixels, this.cellSize.getHeight() + byPixels);
            this.labelMatrix.forEach(label -> {
                label.setSize(this.cellSize);
                label.setPreferredSize(this.cellSize);
            });
            this.getVerticalScrollBar().setUnitIncrement(this.cellSize.height);
                this.grid.setVisible(true);
            });
        }

    /**
     * Alters Border width value.
     * @param byPixels to add
     */
    public void alterBorderWidth(final int byPixels) {
        SwingUtilities.invokeLater(() -> {
                this.grid.setVisible(false);
            if (this.borderWidth + byPixels < 1) {
                throw new IllegalStateException("Final Border Width is 0 or less.");
            }
            this.borderWidth += byPixels;
            for (int i = 0; i < this.labelMatrix.getHeight(); i++) {
                for (int j = 0; j < this.labelMatrix.getWidth(); j++) {
                    setBorder(this.labelMatrix.get(i, j), i, j, this.borderColor, this.borderWidth);
                }
            }
            this.grid.setVisible(true);
        });
    }

    private void setBorder(final JLabel label, final int row, final int col, final Color c, final int borderWidth)  {
        if (row == 0) {
            if (col == 0) {
                // Top left corner, draw all sides
                label.setBorder(BorderFactory.createLineBorder(c, borderWidth));
            } else {
                // Top edge, draw all sides except left edge
                label.setBorder(BorderFactory.createMatteBorder(borderWidth, 0, borderWidth,
                        borderWidth, c));
            }
        } else {
            if (col == 0) {
                // Left-hand edge, draw all sides except top
                label.setBorder(BorderFactory.createMatteBorder(0, borderWidth, borderWidth,
                        borderWidth, c));
            } else {
                // Neither top edge nor left edge, skip both top and left lines
                label.setBorder(BorderFactory.createMatteBorder(0, 0, borderWidth, borderWidth,
                        c));
            }
        }
    }

    /**
     * A fr nvrogòwn  ng .
     * @param boolMatrix is the to.
     */
    public void paintCells(final Matrix<Boolean> boolMatrix) {
        displayColors(boolMatrix.map(b -> b ? Color.black : Color.white));
    }

    private void displayColors(final Matrix<Color> colorMatrix) {
        SwingUtilities.invokeLater(() -> {
            this.grid.setVisible(this.shouldGridStayVisible);
            IntStream.range(0, colorMatrix.getHeight()).forEach(line -> {
                IntStream.range(0, colorMatrix.getWidth()).forEach(column -> {
                    labelMatrix.get(line, column).setBackground(colorMatrix.get(line, column));
                });
            });
            this.grid.setVisible(true);
        });
    }

    /**
     * 
     */
    public void piazzamentoPattern() {

    }

    private final class CellListener implements MouseListener {

        private final int row, column;

        private CellListener(final int row, final int column) {
            this.row = row;
            this.column = column;
        }
        public void mouseReleased(final MouseEvent e) { }

        public void mousePressed(final MouseEvent e) { }

        public void mouseExited(final MouseEvent e) { }

        public void mouseEntered(final MouseEvent e) {
            System.out.println("Enter: " + row + ";" + column);
        }

        public void mouseClicked(final MouseEvent e) {
            System.out.println("Click: " + row + ";" + column);
        }
    }
}
