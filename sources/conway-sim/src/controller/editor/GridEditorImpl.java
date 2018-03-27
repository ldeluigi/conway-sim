package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Optional;

import javax.swing.SwingUtilities;

import core.model.SimpleCell;
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
    private final Environment env;
    private boolean mouseBeingPressed;
    private int lastPreviewRow;
    private int lastPreviewColumn;
    private static final String MESSAGE = "Cannot modify the matrix out of 'Placing' mode or without choosing a pattern";

    /**
     * Constructor method for a new Editor.
     * @param grid is the panel containing the grid to manage.
     */
    public GridEditorImpl(final GridPanel grid) {
        this.gameGrid = grid;
        this.placingState = true;
        this.gameGrid.addListenerToGrid((i, j) -> new CellListener(i, j));
        this.pattern = Optional.empty();
        this.env = EnvironmentFactory.standardRules(this.gameGrid.getColorMatrix().getWidth(), this.gameGrid.getColorMatrix().getHeight());
        this.currentStatus = new ListMatrix<>(this.gameGrid.getGridWidth(), this.gameGrid.getGridHeight(), () -> Status.DEAD);
        this.applyChanges();
    }

    /**
     * Is the method which draws the generation on the grid.
     */
    @Override
    public void draw(final Generation gen) {
        this.gameGrid.paintGrid(gen.getAliveMatrix().map(b -> b ? Color.BLACK : Color.WHITE));
    }

    /**
     * Is the method which places the current chosen pattern in the selected place.
     */
    @Override
    public void hit(final int row, final int column) {
        if (!this.placingState) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        } 
        this.currentStatus.set(row, column, this.currentStatus.get(row, column).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
        this.applyChanges();
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
     */
    @Override
    public void showPreview(final int row, final int column) { //TODO centrare il mouse
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        if ((this.gameGrid.getGridWidth() - column) >= this.pattern.get().getWidth()
                && (this.gameGrid.getGridHeight() - row) >= this.pattern.get().getHeight()) { //TODO rivedere i controlli con il mouse centrato
            this.gameGrid.paintGrid(Matrices.mergeXY(
                    this.currentStatus.map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.BLACK), row, column,
                    this.pattern.get().map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.LIGHT_GRAY)));
            this.lastPreviewRow = row;
            this.lastPreviewColumn = column;
        }
    }

    /**
     * Is the method which sets the given pattern as the one to be placed.
     * @param statusMatrix is the matrix containing the pattern.
     */
    @Override
    public void addPatternToPlace(final Matrix<Status> statusMatrix) {
        this.pattern = Optional.of(Objects.requireNonNull(statusMatrix));
    }

    /**
     * Is the method which merges together the existing matrix and the pattern.
     * @param row is the index describing the lastPreviewRow where to add the first pattern label.
     * @param column is the index of the column where to add the first pattern label.
     */
    @Override
    public void placeCurrentPattern(final int row, final  int column) { //TODO centrare il mouse
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        if ((this.gameGrid.getGridWidth() - column) >= this.pattern.get().getWidth()
                && (this.gameGrid.getGridHeight() - row) >= this.pattern.get().getHeight()) { //TODO rivedere i controlli con il mouse centrato
        this.currentStatus = Matrices.mergeXY(this.currentStatus, row, column, this.pattern.get());
        this.applyChanges();
        this.removePatternToPlace();
        } else if (!(row == this.lastPreviewRow && column == this.lastPreviewColumn)) {
            placeCurrentPattern(this.lastPreviewRow, this.lastPreviewColumn);
        }
    }

    /**
     * Is the method to invoke to know if a pattern is set and can be placed.
     * @return a boolean describing the presence (or absence) of the pattern. 
     */
    @Override
    public boolean isPlacingModeOn() {
        return this.pattern.isPresent();
    }

    /**
     * Is the method to invoke in order to rotate the pattern.
     * @param hits is the number of click from mouse.
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
        this.applyChanges();
    }

    /**
     * Is the method showing if the placing mode is available.
     * @return the boolean describing the current setting.
     */
    @Override
    public boolean isEnabled() {
        return this.placingState;
    }

    /**
     * Is the method to set enable (or disable) the placing mode.
     * @param enabled is the boolean describing the next setting.
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
     * 
     */
    @Override
    public void changeSizes(int horizontal, int vertical) {
        // TODO Auto-generated method stub
    }

    private void applyChanges() {
        this.gameGrid.paintGrid(this.currentStatus.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
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

        @Override
        public void mouseClicked(final MouseEvent e) {
        }

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

        @Override
        public void mouseReleased(final MouseEvent e) {
            GridEditorImpl.this.mouseBeingPressed = false;
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            if (GridEditorImpl.this.isEnabled()) {
                if (GridEditorImpl.this.isPlacingModeOn()) {
                    GridEditorImpl.this.showPreview(row, column);
                } else if (GridEditorImpl.this.mouseBeingPressed && SwingUtilities.isLeftMouseButton(e)) {
                    GridEditorImpl.this.hit(this.row, this.column);
                }
            }
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }

    }
}
