package controller.editor;

import java.util.Objects;
import java.util.Optional;

import controller.io.ResourceLoader;

import core.model.SimpleCell;
import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.Colors;
import view.swing.GridPanel;

/**
 * GridEditorImpl is the editor for the grid and the pattern manager; it allows
 * to perform changes to the view of the grid.
 *
 */
public class GridEditorImpl implements PatternEditor {

    private static final String MESSAGE = "Cannot modify the matrix out of 'Placing' mode or without choosing a pattern";

    private final GridPanel gameGrid;

    private boolean mouseBeingPressed;
    private int lastPreviewRow;
    private int lastPreviewColumn;
    private Environment env;
    private Optional<Matrix<Status>> pattern;
    private Matrix<Status> currentStatus;
    private Boolean enabled;

    /**
     * Constructor method for a new Editor.
     * 
     * @param grid
     *            is the panel containing the grid to manage
     */
    public GridEditorImpl(final GridPanel grid) {
        this.gameGrid = grid;
        this.addActionListenerToGridPanel();
        this.pattern = Optional.empty();
        this.env = EnvironmentFactory.standardRules(this.getGameGrid().getGridWidth(),
                this.getGameGrid().getGridHeight());
        this.setCurrentStatus(new ListMatrix<>(this.getGameGrid().getGridWidth(), this.getGameGrid().getGridHeight(),
                () -> Status.DEAD));
    }

    /**
     * @return true if a pattern is being placed
     */
    protected boolean patternIsPresent() {
        return this.pattern.isPresent();
    }

    /**
     * @return current pattern if present or else throws
     *         {@link NoSuchElementException}
     */
    protected Matrix<Status> getPattern() {
        return this.pattern.get();
    }

    /**
     * Is the method which draws the generation on the grid.
     * 
     * @param gen
     *            is the {@link Generation} which should be displayed
     */
    @Override
    public void draw(final Generation gen) {
        this.getGameGrid().paintGrid(0, 0, Colors.colorDefaultCellMatrix(gen.getCellMatrix()));
    }

    /**
     * Is the method which changes the current status of the selected cell.
     * 
     * @param row
     *            is the vertical index of the cell where the user has clicked
     * @param column
     *            is the horizontal index of the cell where the user has clicked
     */
    @Override
    public void hit(final int row, final int column) {
        if (!this.isEnabled()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.getCurrentStatus().set(row, column,
                this.getCurrentStatus().get(row, column).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
        this.getGameGrid().displaySingleCell(row, column,
                Colors.colorDefaultCell(this.getCurrentStatus().get(row, column)));
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
     * Is the method which displays the pattern together with the matrix already
     * existing. The cursor of the mouse will guide the center of the pattern all
     * over the grid (if it can be fitted).
     * 
     * @param row
     *            is the vertical index of the cell where the user is pointing
     * @param column
     *            is the horizontal index of the cell where the user is pointing
     */
    @Override
    public void showPreview(final int row, final int column) {
        if (!this.isEnabled() || !this.patternIsPresent()) {
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
            this.getGameGrid().paintGrid(0, 0, Matrices.mergeXY(Colors.colorDefaultMatrix(this.getCurrentStatus()),
                    newRow, newColumn, Colors.colorPattern(this.pattern.get())));
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
            this.removePatternWithoutRedraw();
        }
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
        if (!this.isEnabled() || !this.patternIsPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        final int[] indexes = this.centerIndexes(row, column);
        final int newRow = indexes[0];
        final int newColumn = indexes[1];
        if ((this.getGameGrid().getGridWidth() - newColumn) >= this.getPattern().getWidth()
                && (this.getGameGrid().getGridHeight() - newRow) >= this.getPattern().getHeight()) {
            this.setCurrentStatus(Matrices.mergeXY(this.getCurrentStatus(), newRow, newColumn, this.getPattern()));
            this.applyChanges();
            this.removePatternWithoutRedraw();
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
        if (!this.isEnabled() || !this.patternIsPresent()) {
            throw new IllegalStateException(GridEditorImpl.MESSAGE);
        }
        this.getPattern().rotateClockwise(hits);
        this.showPreview(this.lastPreviewRow, this.lastPreviewColumn);
    }

    /**
     * Is the method to remove the current pattern that was chosen and also repaints
     * the grid.
     */
    @Override
    public void removePatternToPlace() {
        this.pattern = Optional.empty();
        this.applyChanges();
    }

    /**
     * Is the method showing if the placing mode is available.
     * 
     * @return the boolean describing the current setting
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Is the method to set enable (or disable) the placing mode.
     * 
     * @param enabled
     *            is the boolean describing the next setting
     */
    @Override
    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
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
            this.getGameGrid().addListenerToGrid((i, j) -> new CMouseListenerImpl(i, j, this));
        }
    }

    /**
     * Is the method to get to know if a mouse button is being pressed.
     * 
     * @return the mouseBeingPressed
     */
    public boolean isMouseBeingPressed() {
        return this.mouseBeingPressed;
    }

    /**
     * Is the method to invoke to let the editor know if a mouse button is being
     * pressed.
     * 
     * @param pressed
     *            is the boolean describing if the user is keeping a mouse button
     *            pressed
     */
    public void setMouseBeingPressed(final boolean pressed) {
        this.mouseBeingPressed = pressed;
    }

    /**
     * 
     * @param row
     *            start row
     * @param column
     *            start column
     * @return return an array with new row in first position and new column in
     *         second position
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
        this.getGameGrid().paintGrid(0, 0, Colors.colorDefaultMatrix(this.getCurrentStatus()));
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
     * listener.
     * 
     * where Listener extends MouseListener
     */
    private void addActionListenerToGridPanel() {
        this.getGameGrid().addListenerToGrid((i, j) -> new CMouseListenerImpl(i, j, this));
    }

    /**
     * Is the method which removes the current pattern selected from the book.
     */
    private void removePatternWithoutRedraw() {
        this.pattern = Optional.empty();
    }
}
