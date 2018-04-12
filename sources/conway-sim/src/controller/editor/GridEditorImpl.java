package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.io.ResourceLoader;

import core.model.SimpleCell;
import core.model.Cell;
import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

/**
 * GridEditorImpl is the editor for the grid and the pattern manager depending on which interface is used.
 *
 */
public class GridEditorImpl implements GridEditor, PatternEditor {

    private final GridPanel gameGrid;
    private Optional<Matrix<Status>> pattern;
    private Matrix<Status> currentStatus;
    private boolean placingState;
    private Environment env;
    private boolean mouseBeingPressed;
    private int lastPreviewRow;
    private int lastPreviewColumn;
    private static final BiFunction<Status, Color, Color> STATUSTOCOLOR = (s, c) -> s.equals(Status.ALIVE) ? c : Color.WHITE;
    private static final Function<Status, Color> ALIVETOBLACK = s -> STATUSTOCOLOR.apply(s, Color.BLACK);
    private static final Function<Status, Color> ALIVETOGRAY = s -> STATUSTOCOLOR.apply(s, Color.GRAY);
    private static final Function<Cell, Color> CELLTOCOLOR = c -> ALIVETOBLACK.apply(c.getStatus());
    private static final String MESSAGE = "Cannot modify the matrix out of 'Placing' mode or without choosing a pattern";

    /**
     * Constructor method for a new Editor.
     * @param grid is the panel containing the grid to manage
     */
    public GridEditorImpl(final GridPanel grid) {
        this.gameGrid = grid;
        this.placingState = true;
        this.gameGrid.addListenerToGrid((i, j) -> new CellListener(i, j));
        this.pattern = Optional.empty();
        this.env = EnvironmentFactory.standardRules(this.gameGrid.getGridWidth(), this.gameGrid.getGridHeight());
        this.currentStatus = new ListMatrix<>(this.gameGrid.getGridWidth(), this.gameGrid.getGridHeight(), () -> Status.DEAD);
    }

    /**
     * Is the method which draws the generation on the grid.
     * @param gen is the {@link Generation} which should be displayed
     */
    @Override
    public void draw(final Generation gen) {
        this.gameGrid.paintGrid(0, 0, gen.getCellMatrix().map(CELLTOCOLOR));
    }

    /**
     * Is the method which places the current chosen pattern in the selected place.
     * @param row is the vertical index of the cell where the user has clicked
     * @param col is the horizontal index of the cell where the user has clicked
     */
    @Override
    public void hit(final int row, final int col) {
        if (!this.placingState) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.currentStatus.set(row, col, this.currentStatus.get(row, col).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
        this.gameGrid.displaySingleCell(row, col, ALIVETOBLACK.apply(this.currentStatus.get(row, col)));
    }

    /**
     * Is the method which gives back the current generation displayed.
     * @return the generation displayed.
     */
    @Override
    public Generation getGeneration() {
        return GenerationFactory.from(this.currentStatus.map(s -> new SimpleCell(s)), this.env);
    }

    /**
     * Is the method which displays the future pattern together with the matrix already existing.
     * @param row is the vertical index of the cell where the user is pointing
     * @param col is the horizontal index of the cell where the user is pointing
     */
    @Override
    public void showPreview(final int row, final int col) {
        int newcolumn = col;
        int newrow = row;
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        if (newcolumn < this.pattern.get().getWidth() / 2) {
            newcolumn = this.pattern.get().getWidth() / 2;
        } else if (newcolumn + (this.pattern.get().getWidth() / 2) > this.gameGrid.getGridWidth()) {
            newcolumn = this.gameGrid.getGridWidth() - this.pattern.get().getWidth() / 2;
        }
        if (newrow < this.pattern.get().getHeight() / 2) {
            newrow = this.pattern.get().getHeight() / 2;
        } else if (newrow + (this.pattern.get().getHeight() / 2) > this.gameGrid.getGridHeight()) {
            newrow = this.gameGrid.getGridHeight() - this.pattern.get().getHeight() / 2;
        }
        if ((this.gameGrid.getGridWidth() - newcolumn) >= this.pattern.get().getWidth() && (this.gameGrid.getGridHeight() - newrow) >= this.pattern.get().getHeight()) {
            System.out.println("Chiamata di Merge(" + newrow + ", " + newcolumn + ")");
            this.gameGrid.paintGrid(0, 0, Matrices.mergeXY(
                    this.currentStatus.map(ALIVETOBLACK), newrow - this.pattern.get().getHeight() / 2, newcolumn - this.pattern.get().getWidth() / 2,
                    this.pattern.get().map(ALIVETOGRAY)));
            this.lastPreviewRow = newrow;
            this.lastPreviewColumn = newcolumn;
        }
    }

    /**
     * Is the method which sets the given pattern as the one to be placed.
     * @param statusMatrix is the matrix containing the pattern
     */
    @Override
    public void addPatternToPlace(final Matrix<Status> statusMatrix) {
        if (statusMatrix.getHeight() <= this.gameGrid.getGridHeight() && statusMatrix.getWidth() <= this.gameGrid.getGridWidth()) {
            this.pattern = Optional.of(Objects.requireNonNull(statusMatrix));
        } else {
            JOptionPane.showMessageDialog(this.gameGrid, ResourceLoader.loadString("grideditor.pattern.error"), "Error choosing pattern", JOptionPane.WARNING_MESSAGE);
            this.removePatternToPlace();
        }
    }

    /**
     * Is the method which merges together the existing matrix and the pattern.
     * @param row is the index describing the lastPreviewRow where to add the first pattern label
     * @param col is the index of the column where to add the first pattern label
     */
    @Override
    public void placeCurrentPattern(final int row, final int col) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        int newcolumn = col;
        int newrow = row;
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        if (newcolumn < this.pattern.get().getWidth() / 2) {
            newcolumn = this.pattern.get().getWidth() / 2;
        } else if (newcolumn + (this.pattern.get().getWidth() / 2) > this.gameGrid.getGridWidth()) {
            newcolumn = this.gameGrid.getGridWidth() - this.pattern.get().getWidth() / 2;
        }
        if (newrow < this.pattern.get().getHeight() / 2) {
            newrow = this.pattern.get().getHeight() / 2;
        } else if (newrow + (this.pattern.get().getHeight() / 2) > this.gameGrid.getGridHeight()) {
            newrow = this.gameGrid.getGridHeight() - this.pattern.get().getHeight() / 2;
        }
        if ((this.gameGrid.getGridWidth() - newcolumn) >= this.pattern.get().getWidth() && (this.gameGrid.getGridHeight() - newrow) >= this.pattern.get().getHeight()) {
            this.currentStatus = Matrices.mergeXY(this.currentStatus, newrow - this.pattern.get().getHeight() / 2, newcolumn - this.pattern.get().getWidth() / 2, this.pattern.get());
            this.applyChanges();
            this.removePatternToPlace();
        } else if (!(newrow == this.lastPreviewRow && newcolumn == this.lastPreviewColumn)) {
            this.placeCurrentPattern(this.lastPreviewRow, this.lastPreviewColumn);
        }
    }

    /**
     * Is the method to invoke to know if a pattern is set and can be placed.
     * @return a boolean describing the presence (or absence) of the pattern
     */
    @Override
    public boolean isPlacingModeOn() {
        return this.pattern.isPresent();
    }

    /**
     * Is the method to invoke in order to rotate the pattern.
     * @param hits is the number of click from mouse
     */
    @Override
    public void rotateCurrentPattern(final int hits) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.pattern.get().rotateClockwise(hits);
        this.showPreview(this.lastPreviewRow, this.lastPreviewColumn);
    }

    /**
     * Is the method to remove the current pattern that was chosen.
     */
    @Override
    public void removePatternToPlace() {
        this.pattern = Optional.empty();
    }

    /**
     * Is the method showing if the placing mode is available.
     * @return the boolean describing the current setting
     */
    @Override
    public boolean isEnabled() {
        return this.placingState;
    }

    /**
     * Is the method to set enable (or disable) the placing mode.
     * @param enabled is the boolean describing the next setting
     */
    @Override
    public void setEnabled(final Boolean enabled) {
        this.placingState = enabled;
        if (enabled) {
            this.applyChanges();
        }
    }

    /**
     * Is the method which shows a full white grid as every cell was dead or a new grid was just created.
     */
    @Override
    public void clean() {
        this.currentStatus = new ListMatrix<>(this.gameGrid.getGridWidth(), this.gameGrid.getGridHeight(), () -> Status.DEAD);
        this.applyChanges();
    }

    /**
     * Is the method which changes both dimensions of the grid currently used and shown.
     * @param horizontal is the length of the future grid in number of cells
     * @param vertical is the height of the future grid in number of cells
     */
    @Override
    public void changeSizes(final int horizontal, final int vertical) {
        if (horizontal != this.currentStatus.getWidth() || vertical != this.currentStatus.getHeight()) {
            if (this.currentStatus.getWidth() < horizontal) {
                if (this.currentStatus.getHeight() < vertical) {
                    this.currentStatus = Matrices.mergeXY(new ListMatrix<>(horizontal, vertical, () -> Status.DEAD), 0, 0, this.currentStatus);
                } else {
                    this.currentStatus = Matrices.cut(this.currentStatus, 0, vertical - 1, 0, this.currentStatus.getWidth() - 1);
                    this.currentStatus = Matrices.mergeXY(new ListMatrix<>(horizontal, vertical, () -> Status.DEAD), 0, 0, this.currentStatus);
                }
            } else {
                if (this.currentStatus.getHeight() < vertical) {
                    this.currentStatus = Matrices.cut(this.currentStatus, 0, this.currentStatus.getHeight() - 1, 0, horizontal - 1);
                    this.currentStatus = Matrices.mergeXY(new ListMatrix<>(horizontal, vertical, () -> Status.DEAD), 0, 0, this.currentStatus);
                } else {
                    this.currentStatus = Matrices.cut(this.currentStatus, 0, vertical - 1, 0, horizontal - 1);
                }
            }
            this.gameGrid.changeGrid(horizontal, vertical);
            this.env = EnvironmentFactory.standardRules(horizontal, vertical);
            this.gameGrid.addListenerToGrid((i, j) -> new CellListener(i, j));
        }
    }

    private void applyChanges() {
        this.gameGrid.paintGrid(0, 0, this.currentStatus.map(ALIVETOBLACK));
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
            if (SwingUtilities.isLeftMouseButton(e) && GridEditorImpl.this.isEnabled()) {
                GridEditorImpl.this.mouseBeingPressed = true;
                if (GridEditorImpl.this.isPlacingModeOn()) {
                    GridEditorImpl.this.placeCurrentPattern(this.row, this.column);
                } else {
                    GridEditorImpl.this.hit(this.row, this.column);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && GridEditorImpl.this.isEnabled() && GridEditorImpl.this.isPlacingModeOn()) {
                if (e.isControlDown()) {
                    GridEditorImpl.this.removePatternToPlace();
                } else {
                    GridEditorImpl.this.rotateCurrentPattern(1);
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
                GridEditorImpl.this.mouseBeingPressed = false;
            }
        }

        /**
         * Is the method which notifies when the user's cursor enters a cell of the grid. 
         * @param e the event generated as result of the interaction
         */
        @Override
        public void mouseEntered(final MouseEvent e) {
            if (GridEditorImpl.this.isEnabled()) {
                if (GridEditorImpl.this.isPlacingModeOn()) {
                    GridEditorImpl.this.showPreview(this.row, this.column);
                } else if (GridEditorImpl.this.mouseBeingPressed && SwingUtilities.isLeftMouseButton(e)) {
                    GridEditorImpl.this.hit(this.row, this.column);
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
