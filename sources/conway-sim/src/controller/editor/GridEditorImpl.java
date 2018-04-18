package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

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
 * GridEditorImpl is the editor for the grid and the pattern manager depending
 * on which interface is used; it allows to perform changes to the view of the
 * grid.
 *
 */
public class GridEditorImpl implements SimpleGridEditor, PatternEditor {

    private static final BiFunction<Status, Color, Color> STATUSTOCOLOR = (s, c) -> s.equals(Status.ALIVE) ? c
            : Color.WHITE;
    private static final Function<Status, Color> ALIVETOBLACK = s -> STATUSTOCOLOR.apply(s, Color.BLACK);
    private static final Function<Status, Color> ALIVETOGRAY = s -> STATUSTOCOLOR.apply(s, Color.GRAY);
    private static final Function<Cell, Color> CELLTOCOLOR = c -> ALIVETOBLACK.apply(c.getStatus());
    private static final String MESSAGE = "Cannot modify the matrix out of 'Placing' mode or without choosing a pattern";

    private final GridPanel gameGrid;

    private boolean placingState;
    private boolean mouseBeingPressed;
    private int lastPreviewRow;
    private int lastPreviewColumn;
    private Environment env;
    private Optional<Matrix<Status>> pattern;
    private Matrix<Status> currentStatus;

    /**
     * Constructor method for a new Editor.
     * 
     * @param grid
     *            is the panel containing the grid to manage
     */
    public GridEditorImpl(final GridPanel grid) {
        this.gameGrid = grid;
        this.placingState = true;
        this.addActionListenerToGridPanel();
        this.pattern = Optional.empty();
        this.env = EnvironmentFactory.standardRules(this.getGameGrid().getGridWidth(),
                this.getGameGrid().getGridHeight());
        this.setCurrentStatus(new ListMatrix<>(this.getGameGrid().getGridWidth(), this.getGameGrid().getGridHeight(),
                () -> Status.DEAD));
    }

    /**
     * Is the method which draws the generation on the grid.
     * 
     * @param gen
     *            is the {@link Generation} which should be displayed
     */
    @Override
    public void draw(final Generation gen) {
        this.getGameGrid().paintGrid(0, 0, gen.getCellMatrix().map(CELLTOCOLOR));
    }

    /**
     * Is the method which places the current chosen pattern in the selected place.
     * 
     * @param row
     *            is the vertical index of the cell where the user has clicked
     * @param column
     *            is the horizontal index of the cell where the user has clicked
     */
    @Override
    public void hit(final int row, final int column) {
        if (!this.placingState) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.getCurrentStatus().set(row, column,
                this.getCurrentStatus().get(row, column).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
        this.getGameGrid().displaySingleCell(row, column, ALIVETOBLACK.apply(this.getCurrentStatus().get(row, column)));
    }

    /**
     * Is the method which gives back the current generation displayed.
     * 
     * @return the generation displayed.
     */
    @Override
    public Generation getGeneration() {
        return GenerationFactory.from(this.getCurrentStatus().map(s -> new SimpleCell(s)), this.env);
    }

    /**
     * Is the method which displays the future pattern together with the matrix
     * already existing. The cursor of the mouse will guide the center of the
     * pattern all over the grid (if it can be fitted).
     * 
     * @param row
     *            is the vertical index of the cell where the user is pointing
     * @param column
     *            is the horizontal index of the cell where the user is pointing
     */
    @Override
    public void showPreview(final int row, final int column) {
        if (!this.placingState || !this.patternIsPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        final int newRow;
        final int newColumn;
        if (this.lastPreviewColumn != column || this.lastPreviewRow != row) {
            final int[] indexes = this.centerIndexes(row, column);
            newRow = indexes[0];
            newColumn = indexes[1];
        } else {
            newRow = row;
            newColumn = column;
        }
        if ((this.getGameGrid().getGridWidth() - newColumn) >= this.pattern.get().getWidth()
                && (this.getGameGrid().getGridHeight() - newRow) >= this.pattern.get().getHeight()) {
            this.getGameGrid().paintGrid(0, 0, Matrices.mergeXY(this.getCurrentStatus().map(ALIVETOBLACK), newRow,
                    newColumn, this.pattern.get().map(ALIVETOGRAY)));
            this.lastPreviewRow = newRow;
            this.lastPreviewColumn = newColumn;
        }
    }

    /**
     * Is the method which sets the given pattern as the one to be placed.
     * 
     * @param statusMatrix
     *            is the matrix containing the pattern
     */
    @Override
    public void addPatternToPlace(final Matrix<Status> statusMatrix) {
        if (statusMatrix.getHeight() <= this.getGameGrid().getGridHeight()
                && statusMatrix.getWidth() <= this.getGameGrid().getGridWidth()) {
            this.pattern = Optional.of(Objects.requireNonNull(statusMatrix));
        } else {
            this.getGameGrid().notifyToUser(ResourceLoader.loadString("grideditor.pattern.error"));
            this.removePatternToPlace();
        }
    }

    /**
     * 
     * @return if the pattern is present
     */
    public boolean patternIsPresent() {
        return this.pattern.isPresent();
    }

    /**
     * @return current pattern if present
     *                  else null
     */
    public Matrix<Status> getPattern() {
        return this.pattern.get();
    }

    /**
     * Is the method which merges together the existing matrix and the pattern.
     * 
     * @param row
     *            is the index describing the lastPreviewRow where to add the first
     *            pattern label
     * @param column
     *            is the index of the lastPreviewColumn where to add the first
     *            pattern label
     */
    @Override
    public void placeCurrentPattern(final int row, final int column) {
        if (!this.placingState || !this.patternIsPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        final int[] indexes = this.centerIndexes(row, column);
        final int newRow = indexes[0];
        final int newColumn = indexes[1];
        if ((this.getGameGrid().getGridWidth() - newColumn) >= this.getPattern().getWidth()
                && (this.getGameGrid().getGridHeight() - newRow) >= this.getPattern().getHeight()) {
            this.setCurrentStatus(Matrices.mergeXY(this.getCurrentStatus(), newRow, newColumn, this.getPattern()));
            this.applyChanges();
            this.removePatternToPlace();
        } else if (!(newRow == this.lastPreviewRow && newColumn == this.lastPreviewColumn)) {
            this.placeCurrentPattern(this.lastPreviewRow, this.lastPreviewColumn);
        }
    }

    /**
     * Is the method to invoke to know if a pattern is set and can be placed.
     * 
     * @return a boolean describing the presence (or absence) of the pattern
     */
    @Override
    public boolean isPlacingModeOn() {
        return this.pattern.isPresent();
    }

    /**
     * Is the method to invoke in order to rotate the pattern.
     * 
     * @param hits
     *            is the number of click(s) from the mouse
     */
    @Override
    public void rotateCurrentPattern(final int hits) {
        if (!this.placingState || !this.patternIsPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.getPattern().rotateClockwise(hits);
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
     * 
     * @return the boolean describing the current setting
     */
    @Override
    public boolean isEnabled() {
        return this.placingState;
    }

    /**
     * Is the method to set enable (or disable) the placing mode.
     * 
     * @param enabled
     *            is the boolean describing the next setting
     */
    @Override
    public void setEnabled(final Boolean enabled) {
        this.placingState = enabled;
        if (enabled) {
            this.applyChanges();
        }
    }

    /**
     * Is the method which shows a full white grid as every cell was dead or a new
     * grid was just created.
     */
    @Override
    public void clean() {
        this.currentStatus = new ListMatrix<>(this.getGameGrid().getGridWidth(), this.getGameGrid().getGridHeight(),
                () -> Status.DEAD);
        this.applyChanges();
    }

    /**
     * Is the method which changes both dimensions of the grid currently used and
     * shown.
     * 
     * @param horizontal
     *            is the length of the future grid in number of cells
     * @param vertical
     *            is the height of the future grid in number of cells
     */
    @Override
    public void changeSizes(final int horizontal, final int vertical) {
        if (horizontal != this.getCurrentStatus().getWidth() || vertical != this.getCurrentStatus().getHeight()) {
            if (this.getCurrentStatus().getWidth() < horizontal) {
                if (this.getCurrentStatus().getHeight() < vertical) {
                    this.setCurrentStatus(Matrices.mergeXY(new ListMatrix<>(horizontal, vertical, () -> Status.DEAD), 0,
                            0, this.getCurrentStatus()));
                } else {
                    this.setCurrentStatus(Matrices.cut(this.getCurrentStatus(), 0, vertical - 1, 0,
                            this.getCurrentStatus().getWidth() - 1));
                    this.setCurrentStatus(Matrices.mergeXY(new ListMatrix<>(horizontal, vertical, () -> Status.DEAD), 0,
                            0, this.getCurrentStatus()));
                }
            } else {
                if (this.getCurrentStatus().getHeight() < vertical) {
                    this.setCurrentStatus(Matrices.cut(this.getCurrentStatus(), 0,
                            this.getCurrentStatus().getHeight() - 1, 0, horizontal - 1));
                    this.setCurrentStatus(Matrices.mergeXY(new ListMatrix<>(horizontal, vertical, () -> Status.DEAD), 0,
                            0, this.getCurrentStatus()));
                } else {
                    this.setCurrentStatus(Matrices.cut(this.getCurrentStatus(), 0, vertical - 1, 0, horizontal - 1));
                }
            }
            this.getGameGrid().changeGrid(horizontal, vertical);
            this.env = EnvironmentFactory.standardRules(horizontal, vertical);
            this.getGameGrid().addListenerToGrid((i, j) -> new CellListener(i, j));
        }
    }

    /**
     * 
     * @param row start row
     * @param column start column
     * @return return an array with new row in first position and new column in second position 
     */
    protected int[] centerIndexes(final int row, final int column) {
        int[] newIndex = { row - this.pattern.get().getHeight() / 2, column - this.pattern.get().getWidth() / 2 };

        if (newIndex[0] < 0) {
            newIndex[0] = 0;
        }
        if (newIndex[0] > this.getGameGrid().getGridHeight() - this.pattern.get().getHeight()) {
            newIndex[0] = this.getGameGrid().getGridHeight() - this.pattern.get().getHeight();
        }
        if (newIndex[1] < 0) {
            newIndex[1] = 0;
        }
        if (newIndex[1] > this.getGameGrid().getGridWidth() - this.pattern.get().getWidth()) {
            newIndex[1] = this.getGameGrid().getGridWidth() - this.pattern.get().getWidth();
        }
        return newIndex;
    }

    /**
     * 
     */
    protected void applyChanges() {
        this.getGameGrid().paintGrid(0, 0, this.getCurrentStatus().map(ALIVETOBLACK));
    }

    /**
     * 
     * @return the current status matrix
     */
    protected Matrix<Status> getCurrentStatus() {
        return this.currentStatus;
    }

    /**
     * 
     * @param newStatus
     *            set the new matrix status
     */
    protected void setCurrentStatus(final Matrix<Status> newStatus) {
        this.currentStatus = newStatus;
    }

    /**
     * 
     * @return the current GridPanel
     */
    protected GridPanel getGameGrid() {
        return gameGrid;
    }

    /**
     * Add the actionListener to the game grid. Override this method to change the
     * listener
     * 
     * this.getGameGrid().addListenerToGrid((x, y) -> new Listener(x, y);
     *
     * where Listener extends MouseListener
     */
    protected void addActionListenerToGridPanel() {
        this.getGameGrid().addListenerToGrid((i, j) -> new CellListener(i, j));
    }

    class CellListener implements MouseListener {

        private final int row;
        private final int column;

        /**
         * Is the constructor method which creates a new Listener.
         * 
         * @param i
         *            is the vertical index of the cell.
         * @param j
         *            is the horizontal index of the cell.
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
         * 
         * @param e
         *            the event generated as result of the interaction
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
            } else if (SwingUtilities.isRightMouseButton(e) && GridEditorImpl.this.isEnabled()
                    && GridEditorImpl.this.isPlacingModeOn()) {
                if (e.isControlDown()) {
                    GridEditorImpl.this.removePatternToPlace();
                    GridEditorImpl.this.applyChanges();
                } else {
                    GridEditorImpl.this.rotateCurrentPattern(1);
                }
            }
        }

        /**
         * Is the method which notifies when mouse's left button is released.
         * 
         * @param e
         *            the event generated as result of the interaction with the grid
         */
        @Override
        public void mouseReleased(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                GridEditorImpl.this.mouseBeingPressed = false;
            }
        }

        /**
         * Is the method which notifies when the user's cursor enters a cell of the
         * grid.
         * 
         * @param e
         *            the event generated as result of the interaction
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
