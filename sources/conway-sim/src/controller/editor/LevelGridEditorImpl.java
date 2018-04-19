package controller.editor;

import java.awt.Color;
import java.util.Optional;

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
import view.swing.Colors;
import view.swing.sandbox.GridPanel;

/**
 * Editor extended for level mode.
 */
public final class LevelGridEditorImpl extends GridEditorImpl {

    private Optional<Level> currentLevel;
    private Matrix<Status> currentStatus;

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

//    /**
//     * 
//     * @param gridp
//     *            a
//     */
//    public LevelGridEditorImpl(final GridPanel gridp) {
//        super(gridp);
//        setLevel(new Level() {
//
//            @Override
//            public Matrix<Status> getInitialStateMatrix() {
//                return new LazyMatrix<>(1, 1, Status.DEAD);
//            }
//
//            @Override
//            public Environment getEnvironmentMatrix() {
//                return EnvironmentFactory.standardRules(1, 1);
//            }
//
//            @Override
//            public Matrix<Editable> getEditableMatrix() {
//                return new LazyMatrix<>(1, 1, Editable.EDITABLE);
//            }
//
//            @Override
//            public Matrix<CellType> getCellTypeMatrix() {
//                return new LazyMatrix<>(1, 1, CellType.NORMAL);
//            }
//
//            @Override
//            public List<String> availablePatterns() {
//                return Collections.emptyList();
//            }
//        });
//    }

    /**
     * //TODO controllo che il livello sia stato settato prima di usare tutti i metodi della classe
     * tranne questo.
     * 
     * @param level
     *            to set as current
     */
    public void setLevel(final Level level) {
        this.currentLevel = Optional.of(level);
        this.getGameGrid().changeGrid(this.currentLevel.get().getEnvironmentMatrix().getWidth(),
                this.currentLevel.get().getEnvironmentMatrix().getHeight());
        this.currentStatus = this.currentLevel.get().getInitialStateMatrix().map(e -> e);
        this.clean();
    }

    /**
     * Is the method which gives back the current generation displayed.
     * 
     * @return the generation displayed.
     */
    @Override
    public Generation getGeneration() {
        final Matrix<Cell> cellMatrix = new ListMatrix<>(this.currentLevel.get().getEnvironmentMatrix().getWidth(),
                this.currentLevel.get().getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.currentLevel.get().getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.currentLevel.get().getEnvironmentMatrix().getWidth(); col++) {
                cellMatrix.set(row, col, 
                        this.currentLevel.get().getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)
                        ? new SimpleCell(this.currentStatus.get(row, col))
                        : this.currentLevel.get().getCellTypeMatrix().get(row, col).equals(CellType.GOLDEN)
                        ? new GameWinningCell(this.currentStatus.get(row, col), () -> {
                        //born
                            System.out.println("DEBUG| cell gold born");
                        }, () -> {
                        //death
                            System.out.println("DEBUG| cell gold death");
                        })
                        : this.currentLevel.get().getCellTypeMatrix().get(row, col).equals(CellType.WALL)
                        ? new NeverChangingCell(this.currentStatus.get(row, col))
                        : new SimpleCell(this.currentStatus.get(row, col)));
            }
        }
        return GenerationFactory.from(cellMatrix,
                this.currentLevel.get().getEnvironmentMatrix());
    }

    /**
     * 
     */
    @Override
    public void hit(final int row, final int col) {
        if (!this.isEnabled()) {
            throw new IllegalStateException();
        }
        if (this.currentLevel.get().getEditableMatrix().get(row, col).equals(Editable.EDITABLE)) {
            this.currentStatus.set(row, col,
                    this.currentStatus.get(row, col).equals(Status.DEAD) ? Status.ALIVE
                            : Status.DEAD);
            this.getGameGrid().displaySingleCell(row, col, calculateColor(row, col));
        }
    }

    /**
     * Is the method which shows a full white grid as every cell was dead or a new grid was just
     * created.
     */
    @Override
    public void clean() {
        this.currentStatus = Matrices.copyOf(currentLevel.get().getInitialStateMatrix());
        this.applyChanges();
    }

    /**
     * 
     */
    @Override
    protected void applyChanges() {
        final Matrix<Color> matrixColor = new ListMatrix<>(
                this.currentLevel.get().getEnvironmentMatrix().getWidth(),
                this.currentLevel.get().getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.currentLevel.get().getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.currentLevel.get().getEnvironmentMatrix()
                    .getWidth(); col++) {
                matrixColor.set(row, col, calculateColor(row, col));
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
        final Matrix<Color> matrixColor = new ListMatrix<>(
                this.currentLevel.get().getEnvironmentMatrix().getWidth(),
                this.currentLevel.get().getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.currentLevel.get().getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.currentLevel.get().getEnvironmentMatrix()
                    .getWidth(); col++) {
                matrixColor.set(row, col, calculateColorEditable(row, col));
            }
        }
        this.getGameGrid().paintGrid(0, 0, matrixColor);
    }

    private Color calculateColor(final int row, final int col) {
        if (this.currentLevel.get().getEditableMatrix().get(row, col).equals(Editable.EDITABLE)) {
            return calculateColorEditable(row, col);
        } else {
            if (this.currentLevel.get().getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)) {
                return Colors.blend(Color.RED,
                        this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK
                                : Color.WHITE);
            } else if (this.currentLevel.get().getCellTypeMatrix().get(row, col)
                    .equals(CellType.GOLDEN)) {
                return Colors.blend(Colors.GOLD,
                        this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK
                                : Color.WHITE);
            } else if (this.currentLevel.get().getCellTypeMatrix().get(row, col)
                    .equals(CellType.WALL)) {
                return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.DARK_GRAY
                        : Color.LIGHT_GRAY;
            } else {
                return Colors.blend(Color.RED,
                        this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK
                                : Color.WHITE);
            }
        }
    }

    private Color calculateColorEditable(final int row, final int col) {
        if (this.currentLevel.get().getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)) {
            return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK
                    : Color.WHITE;
        } else if (this.currentLevel.get().getCellTypeMatrix().get(row, col)
                .equals(CellType.GOLDEN)) {
            return Colors.blend(Colors.GOLD,
                    this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK
                            : Color.WHITE);
        } else if (this.currentLevel.get().getCellTypeMatrix().get(row, col)
                .equals(CellType.WALL)) {
            return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.DARK_GRAY
                    : Color.LIGHT_GRAY;
        } else {
            return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK
                    : Color.WHITE;
        }
    }

    /**
     * Is the method which displays the future pattern together with the matrix already existing.
     * The cursor of the mouse will guide the center of the pattern all over the grid (if it can be
     * fitted).
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
        }
    }

    /**
     * Is the method which merges together the existing matrix and the pattern.
     * 
     * @param row
     *            is the index describing the lastPreviewRow where to add the first pattern label
     * @param column
     *            is the index of the lastPreviewColumn where to add the first pattern label
     */
    @Override
    public void placeCurrentPattern(final int row, final int column) {
        if (!this.isEnabled() || !this.patternIsPresent()) {
            throw new IllegalStateException();
        }
        if (patternIsPlacable(row, column)) {
            super.placeCurrentPattern(row, column);
        }
    }

    private boolean patternIsPlacable(final int row, final int column) {
        final int[] vet = this.centerIndexes(row, column);
        final int newRow = vet[0];
        final int newCol = vet[1];
        for (int x = newRow; x < newRow + this.getPattern().getWidth(); x++) {
            for (int y = newCol; y < newCol + this.getPattern().getHeight(); y++) {
                if (this.currentLevel.get().getEditableMatrix().get(x, y)
                        .equals(Editable.UNEDITABLE)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Is the method which changes both dimensions of the grid currently used and shown.
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
