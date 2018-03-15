package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Optional;

import javax.swing.SwingUtilities;

import core.model.CellImpl;
import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Status;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

/**
 * 
 *
 */
public class GridEditorImpl implements GridEditor, PatternEditor {

    private final GridPanel gameGrid;
    private Optional<Matrix<Status>> pattern;
    private Matrix<Status> currentStatus;
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
        this.pattern = Optional.empty();
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
    /*public void getBackUpMatrix() {
        this.currentStatus = this.gameGrid.getColorMatrix().map(c -> c.equals(Color.BLACK) ? Status.ALIVE : Status.DEAD);
    }*/

    /**
     * 
     * @param e
     */
    /*public void setEnvironment(final Environment e) {
        this.env = e;
    }*/

    /**
     * 
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
     * 
     */
    @Override
    public Generation getGeneration() {
        return GenerationFactory.from(this.currentStatus.map(s -> new CellImpl(s)) , EnvironmentFactory.standardRules(this.currentStatus.getWidth(), this.currentStatus.getHeight()));
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
        this.pattern = Optional.of(Objects.requireNonNull(statusMatrix));
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
        this.removePatternToPlace();
    }

    /**
     * 
     */
    @Override
    public boolean isPlacingModeOn() {
        return this.pattern.isPresent();
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
        this.pattern = Optional.empty();
    }
    
    @Override
	public boolean isEnabled() {
		return this.placingState;
	}
    
    /**
     * 
     */
    @Override
	public void setEnabled(final Boolean enabled) {
		this.placingState = enabled;
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
            if (SwingUtilities.isLeftMouseButton(e) && isEnabled()) {
                hit(row, column); //is placing mode on
            } else if (SwingUtilities.isRightMouseButton(e) && isEnabled() && isPlacingModeOn()) {
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


