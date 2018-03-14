package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.SwingUtilities;

import core.model.Cell;
import core.model.CellImpl;
import core.model.Environment;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

/**
 * 
 *
 */
public class GridEditorImpl implements GridEditor, PatternEditor {

    private final GridPanel gameGrid;
    private Optional<Matrix<Status>> pattern = Optional.ofNullable(null);
    private Matrix<Status> currentStatus;
    private Environment env;
    private boolean placingState;
    private static final String MESSAGE = "Cannot modify the matrix out of 'Placing' mode or without choosing a pattern";

    /**
     * 
     * @param grid
     */
    public GridEditorImpl(final GridPanel grid) {
        this.gameGrid = grid;
        this.placingState = false;
        this.gameGrid.addListenerToGrid((i, j) -> new CellListener(i, j));
    }

    /**
     * 
     */
    @Override
    public void draw(final Generation gen) {
        this.gameGrid.paintGrid(gen.getAliveMatrix().map(b -> b ? Color.BLACK : Color.WHITE));
    }

    /**
     * 
     */
    public void getBackUpMatrix() {
        this.currentStatus = this.gameGrid.getColorMatrix().map(c -> c.equals(Color.BLACK) ? Status.ALIVE : Status.DEAD);
    }

    /**
     * 
     */
    public void setPlacingState() {
        this.placingState = true;
        this.getBackUpMatrix();
    }

    /**
     * 
     * @param e
     */
    public void setEnvironment(Environment e) {
        this.env = e;
    }

    /**
     * 
     */
    @Override
    public void hit(final int row, final int column) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        } 
        this.currentStatus.set(row, column, this.currentStatus.get(row, column).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
        this.gameGrid.paintGrid(this.currentStatus.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
    }

    /**
     * 
     */
    @Override
    public Generation getGeneration() {
        return GenerationFactory.from(this.currentStatus.map(s -> new CellImpl(s)) , this.env);
    }

    /**
     * 
     */
    @Override
    public void showPreview(final int row, final int column) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.gameGrid.paintGrid(Matrices.mergeXY(this.currentStatus, row, column, this.pattern.get()).map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.BLACK));
    }

    /**
     * 
     */
    @Override
    public void addPatternToPlace(final Matrix<Status> statusMatrix) {
        this.pattern = Optional.ofNullable(statusMatrix);
    }

    /**
     * 
     */
    @Override
    public void placeCurrentPattern(final int row, final  int column) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.currentStatus = Matrices.mergeXY(this.currentStatus, row, column, this.pattern.get());
        this.gameGrid.paintGrid(this.currentStatus.map(s -> s.equals(Status.DEAD) ? Color.WHITE : Color.BLACK));
    }

    /**
     * 
     */
    @Override
    public boolean isPlacingModeOn() {
        return this.placingState;
    }

    /**
     * 
     */
    @Override
    public void rotateCurrentPattern(final int hits) {
        if (!this.placingState || !this.pattern.isPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.pattern.get().rotateClockwise(hits);
    }

    /**
     * 
     */
    @Override
    public void removePatternToPlace() {
        this.pattern = Optional.ofNullable(null);
    }

    class CellListener implements MouseListener {

        private final int row;
        private final int column;

        /**
         * 
         * @param i
         * @param j
         */
        CellListener(final int i, final int j) {
            this.row = i;
            this.column = j;
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                hit(row, column);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                rotateCurrentPattern(e.getClickCount());
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
            showPreview(row, column);
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }

    }


}


