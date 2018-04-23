package controller.editor;

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
import view.swing.GridPanel;

/**
 * Editor extended for level mode.
 */
public final class LevelGridEditorImpl extends GridEditorImpl {

    private final Level currentLevel;
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
                cellMatrix.set(row, col, this.currentLevel.getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)
                        // NORMAL CELL
                        ? new SimpleCell(this.getCurrentStatus().get(row, col))
                        : this.currentLevel.getCellTypeMatrix().get(row, col).equals(CellType.GOLDEN)
                                // GOLDEN CELL
                                ? new GameWinningCell(this.getCurrentStatus().get(row, col))
                                : this.currentLevel.getCellTypeMatrix().get(row, col).equals(CellType.WALL)
                                        // WALL CELL
                                        ? new NeverChangingCell(this.getCurrentStatus().get(row, col))
                                        // DEFAULT CELL
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
            this.getGameGrid().displaySingleCell(row, col,
                    Colors.cellColor(this.currentLevel.getEditableMatrix().get(row, col),
                            this.currentLevel.getCellTypeMatrix().get(row, col),
                            this.getCurrentStatus().get(row, col)));
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
        this.getGameGrid().paintGrid(0, 0,
                Colors.colorMatrix(this.getCurrentStatus(), this.currentLevel.getCellTypeMatrix(),
                        this.currentLevel.getEditableMatrix(), this.currentLevel.getEnvironmentMatrix()));
    }

    /**
     * Is the method which draws the generation on the grid.
     * 
     * @param gen
     *            is the {@link Generation} which should be displayed
     */
    @Override
    public void draw(final Generation gen) {
        this.getGameGrid().paintGrid(0, 0, Colors.colorEditableMatrix(gen.getCellMatrix().map(cell -> cell.getStatus()),
                this.currentLevel.getCellTypeMatrix(), this.currentLevel.getEnvironmentMatrix()));

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
            this.paintPreview(row, column);
        } else if (this.lastIsPresent && patternIsPlacable(lastRowPlacable, lastColPlacable)) {
            this.paintPreview(this.lastRowPlacable, this.lastColPlacable);
        }
    }

    private void paintPreview(final int row, final int column) {
        final int[] vet = this.centerIndexes(row, column);
        final int newRow = vet[0];
        final int newCol = vet[1];
        this.getGameGrid().paintGrid(0, 0,
                Matrices.mergeXY(
                        Colors.colorMatrix(getCurrentStatus(), this.currentLevel.getCellTypeMatrix(),
                                this.currentLevel.getEditableMatrix(), this.currentLevel.getEnvironmentMatrix()),
                        newRow, newCol, Colors.statusToGray(this.getPattern())));
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
        } else if (this.lastIsPresent && patternIsPlacable(lastRowPlacable, lastColPlacable)) {
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
