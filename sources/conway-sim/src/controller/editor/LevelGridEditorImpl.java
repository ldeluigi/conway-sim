package controller.editor;

import java.awt.Color;
import core.campaign.CellType;
import core.campaign.Editable;

import core.campaign.Level;
import core.campaign.LevelImplTest;
import core.model.Environment;
import core.model.Generation;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.Colors;
import view.swing.sandbox.GridPanel;

/**
 * 
 */
public class LevelGridEditorImpl extends GridEditorImpl {

    private final Level currentLevel;
    private final Matrix<Editable> editableMatrix;
    private final Matrix<CellType> cellTypeMatrix;
    private final Environment environment;
    private Matrix<Status> currentStatus;

    /**
     * 
     * @param grid the initial grid
     * @param level the level to be loaded
     */
    public LevelGridEditorImpl(final GridPanel grid, final Level level) {
        super(grid);
        this.currentLevel = new LevelImplTest();
        this.currentStatus = this.currentLevel.getInitialStateMatrix().map(e -> e);
        this.environment = this.currentLevel.getEnvironmentMatrix();
        this.editableMatrix = this.currentLevel.getEditableMatrix();
        this.cellTypeMatrix = this.currentLevel.getCellTypeMatrix();
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
            this.currentStatus.set(row, col, this.currentStatus.get(row, col).equals(Status.DEAD) ? Status.ALIVE : Status.DEAD);
            this.getGameGrid().displaySingleCell(row, col, calculateColor(row, col));
        }
    }

    /**
     * Is the method which shows a full white grid as every cell was dead or a new
     * grid was just created.
     */
    @Override
    public void clean() {
        this.currentStatus = Matrices.copyOf(currentLevel.getInitialStateMatrix());
        this.applyChanges();
    }

    /**
     * 
     */
    protected void applyChanges() {
        Matrix<Color> matrixColor = new ListMatrix<>(this.environment.getWidth(), this.environment.getHeight(), () -> null);
        for (int row = 0; row < this.environment.getHeight(); row++) {
            for (int col = 0; col < this.environment.getWidth(); col++) {
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
        Matrix<Color> matrixColor = new ListMatrix<>(this.environment.getWidth(), this.environment.getHeight(), () -> null);
        for (int row = 0; row < this.environment.getHeight(); row++) {
            for (int col = 0; col < this.environment.getWidth(); col++) {
                matrixColor.set(row, col, calculateColorEditable(row, col));
            }
        }
        this.getGameGrid().paintGrid(0, 0, matrixColor);
    }

    private Color calculateColor(final int row, final int col) {
        if (this.editableMatrix.get(row, col).equals(Editable.EDITABLE)) {
            return calculateColorEditable(row, col);
        } else {
            if (this.cellTypeMatrix.get(row, col).equals(CellType.NORMAL)) {
                return Colors.blend(Color.RED, this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            } else if (this.cellTypeMatrix.get(row, col).equals(CellType.GOLDEN)) {
                return Colors.blend(Colors.GOLD, this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            } else if (this.cellTypeMatrix.get(row, col).equals(CellType.WALL)) {
                return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            } else {
                return Colors.blend(Color.RED, this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private Color calculateColorEditable(final int row, final int col) {
        if (this.cellTypeMatrix.get(row, col).equals(CellType.NORMAL)) {
            return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
        } else if (this.cellTypeMatrix.get(row, col).equals(CellType.GOLDEN)) {
            return Colors.blend(Colors.GOLD, this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
        } else if (this.cellTypeMatrix.get(row, col).equals(CellType.WALL)) {
            return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.DARK_GRAY : Color.LIGHT_GRAY;
        } else {
            return this.currentStatus.get(row, col).equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
        }
    }
}
