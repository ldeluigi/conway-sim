package controller.editor;

import java.awt.Color;
import java.util.function.Function;

import core.campaign.CellType;
import core.campaign.Editable;
import core.campaign.GameWinningCell;
import core.campaign.Level;
import core.campaign.NeverChangingCell;
import core.model.Cell;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.SimpleCell;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.Colors;
import view.swing.sandbox.GridPanel;

/**
 * Editor extended for level mode.
 */
public final class LevelGridEditorImpl extends GridEditorImpl {

    private static final Function<Status, Color> ALIVETOGRAY = s -> s.equals(Status.ALIVE) ? Color.LIGHT_GRAY : Color.WHITE;
    private Level currentLevel;
    private boolean lastIsPresent;
    private int lastRowPlacable;
    private int lastColPlacable;

    /**
     * 
     * @param grid
     *            the initial grid
     * @param level
     *            the level to be loaded
     */
    public LevelGridEditorImpl(final GridPanel grid, final Level level) {
        super(grid);
        setLevel(level);
    }

    /**
     * @param level
     *            to set as current
     */
    public void setLevel(final Level level) {
        this.currentLevel = level;
        this.getGameGrid().changeGrid(this.currentLevel.getEnvironmentMatrix().getWidth(),
                this.currentLevel.getEnvironmentMatrix().getHeight());
        this.setCurrentStatus(this.currentLevel.getInitialStateMatrix().map(e -> e));
        this.clean();
    }

    /**
     * Is the method which gives back the current generation displayed.
     * 
     * @return the generation displayed.
     */
    @Override
    public Generation getGeneration() {
        final Matrix<Cell> cellMatrix = new ListMatrix<>(this.currentLevel.getEnvironmentMatrix().getWidth(),
                this.currentLevel.getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.currentLevel.getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.currentLevel.getEnvironmentMatrix().getWidth(); col++) {
                cellMatrix.set(row, col,
                        this.currentLevel.getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)
                                ? new SimpleCell(this.getCurrentStatus().get(row, col))
                                : this.currentLevel.getCellTypeMatrix().get(row, col).equals(CellType.GOLDEN)
                                        ? new GameWinningCell(this.getCurrentStatus().get(row, col))
                                        : this.currentLevel.getCellTypeMatrix().get(row, col).equals(CellType.WALL)
                                                ? new NeverChangingCell(this.getCurrentStatus().get(row, col))
                                                : new SimpleCell(this.getCurrentStatus().get(row, col)));
            }
        }
        return GenerationFactory.from(cellMatrix, this.currentLevel.getEnvironmentMatrix());
    }

    /**
     * 
     */
    @Override
    public void hit(final int row, final int col) {
        if (!this.isEnabled()) {
            throw new IllegalStateException();
        }
        if (this.currentLevel.getEditableMatrix().get(row, col).equals(Editable.EDITABLE)) {
            this.getCurrentStatus().set(row, col,
                    this.getCurrentStatus().get(row, col).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
            this.getGameGrid().displaySingleCell(row, col, calculateColorEditMode(row, col));
        }
    }

    /**
     * Is the method which shows a full white grid as every cell was dead or a new
     * grid was just created.
     */
    @Override
    public void clean() {
        this.setCurrentStatus(Matrices.copyOf(currentLevel.getInitialStateMatrix()));
        this.applyChanges();
    }

    /**
     * 
     */
    @Override
    protected void applyChanges() {
        final Matrix<Color> matrixColor = new ListMatrix<>(this.currentLevel.getEnvironmentMatrix().getWidth(),
                this.currentLevel.getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.currentLevel.getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.currentLevel.getEnvironmentMatrix().getWidth(); col++) {
                matrixColor.set(row, col, calculateColorEditMode(row, col));
            }
        }
        this.getGameGrid().paintGrid(0, 0, matrixColor);
    }

    /**
     * Is the method which draws the generation on the grid.
     * 
     * @param gen
     *            is the {@link Generation} which should be displayed
     */
    @Override
    public void draw(final Generation gen) {
        final Matrix<Color> matrixColor = new ListMatrix<>(this.currentLevel.getEnvironmentMatrix().getWidth(),
                this.currentLevel.getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.currentLevel.getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.currentLevel.getEnvironmentMatrix().getWidth(); col++) {
                matrixColor.set(row, col,
                        calculateColorRunningMode(row, col, gen.getCellMatrix().map(s -> s.getStatus())));
            }
        }
        this.getGameGrid().paintGrid(0, 0, matrixColor);
    }

    private Color calculateColorEditMode(final int row, final int col) {
        return Colors.cellColor(this.currentLevel.getEditableMatrix().get(row, col),
                this.currentLevel.getCellTypeMatrix().get(row, col), this.getCurrentStatus().get(row, col));
    }

    private Color calculateColorRunningMode(final int row, final int col, final Matrix<Status> status) {
        return Colors.cellColor(Editable.EDITABLE, this.currentLevel.getCellTypeMatrix().get(row, col),
                status.get(row, col));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void colorPreview(final int row, final int col) {
        final Matrix<Color> matrixColor = new ListMatrix<>(this.currentLevel.getEnvironmentMatrix().getWidth(),
                this.currentLevel.getEnvironmentMatrix().getHeight(), () -> null);
        for (int x = 0; x < this.currentLevel.getEnvironmentMatrix().getHeight(); x++) {
            for (int y = 0; y < this.currentLevel.getEnvironmentMatrix().getWidth(); y++) {
                matrixColor.set(x, y, calculateColorEditMode(x, y));
            }
        }
        this.getGameGrid().paintGrid(0, 0, Matrices.mergeXY(matrixColor, row,
                col, this.getPattern().map(ALIVETOGRAY)));
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
        if (!this.isEnabled() || !this.patternIsPresent()) {
            throw new IllegalStateException();
        }
        if (patternIsPlacable(row, column)) {
            super.showPreview(row, column);
        } else if (this.lastIsPresent) {
            super.showPreview(this.lastRowPlacable, this.lastColPlacable);
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
            throw new IllegalStateException();
        }
        if (patternIsPlacable(row, column)) {
            super.placeCurrentPattern(row, column);
        } else if (this.lastIsPresent) {
            super.placeCurrentPattern(this.lastRowPlacable, this.lastColPlacable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePatternToPlace() {
        super.removePatternToPlace();
        this.lastIsPresent = false;
    }

    private boolean patternIsPlacable(final int row, final int column) {
        final int[] vet = this.centerIndexes(row, column);
        final int newRow = vet[0];
        final int newCol = vet[1];
        if (newRow + this.getPattern().getHeight() > this.currentLevel.getEnvironmentMatrix().getHeight()
                || newCol + this.getPattern().getWidth() > this.currentLevel.getEnvironmentMatrix().getWidth()) {
            return false;
        }
        for (int x = newRow; x < newRow + this.getPattern().getHeight(); x++) {
            for (int y = newCol; y < newCol + this.getPattern().getWidth(); y++) {
                if (this.currentLevel.getEditableMatrix().get(x, y).equals(Editable.UNEDITABLE)) {
                    return false;
                }
            }
        }
        this.lastIsPresent = true;
        this.lastRowPlacable = row;
        this.lastColPlacable = column;
        return true;
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
        throw new UnsupportedOperationException();
    }
}
