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
        this.gameGrid.paintGrid(this.currentStatus.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
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
        this.gameGrid.paintGrid(this.currentStatus.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
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
    public void showPreview(final int row, final int column) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.gameGrid.paintGrid(Matrices
                                        .mergeXY(this.currentStatus
                                        .map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.BLACK), row, column, this.pattern
                                        .get().map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.LIGHT_GRAY)));
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
     * @param row is the index describing the row where to add the first pattern label.
     * @param column is the index of the column where to add the first pattern label.
     */
    @Override
    public void placeCurrentPattern(final int row, final  int column) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.currentStatus = Matrices.mergeXY(this.currentStatus, row, column, this.pattern.get());
        this.gameGrid.paintGrid(this.currentStatus.map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.BLACK));
        this.removePatternToPlace();
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
            if (SwingUtilities.isLeftMouseButton(e) && isEnabled()) {
                if (isPlacingModeOn()) {
                    placeCurrentPattern(this.row, this.column);
                } else {
                    hit(this.row, this.column);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && isEnabled() && isPlacingModeOn()) {
                rotateCurrentPattern(e.getClickCount());
                showPreview(this.row, this.column);
            }
        }

        @Override
        public void mousePressed(final MouseEvent e) {
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            if (isPlacingModeOn() && isEnabled()) {
                showPreview(row, column);
            }
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }

    }


}
