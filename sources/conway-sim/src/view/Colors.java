package view;

import java.awt.Color;

import core.campaign.CellType;
import core.campaign.Editable;
import core.campaign.Level;
import core.model.Cell;
import core.model.Environment;
import core.model.StandardCellEnvironments;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

/**
 * Utility class for colors.
 */
public final class Colors {

    /**
     * Color for gold.
     */
    public static final Color GOLD = new Color(210, 210, 0);
    /**
     * Color for light gold.
     */
    public static final Color LIGHT_GOLD = new Color(255, 255, 150);
    /**
     * Color for light green.
     */
    public static final Color LIGHT_GREEN = new Color(200, 255, 200);

    private Colors() {
    }

    /**
     * Blends two colors into a single one.
     * 
     * @param a
     *            the first color
     * @param b
     *            the second color
     * @return the result
     */
    public static Color blend(final Color a, final Color b) {
        return new Color((a.getRed() * a.getAlpha() + b.getRed() * b.getAlpha()) / (a.getAlpha() + b.getAlpha()),
                (a.getGreen() * a.getAlpha() + b.getGreen() * b.getAlpha()) / (a.getAlpha() + b.getAlpha()),
                (a.getBlue() * a.getAlpha() + b.getBlue() * b.getAlpha()) / (a.getAlpha() + b.getAlpha()),
                (a.getAlpha() + b.getAlpha()) / 2);
    }

    /**
     * Method to get to know the color to be shown by the cell.
     * 
     * @param editable
     *            The status that represent if a cell is editable or not
     * @param cellType
     *            The cell type, Normal, Gold, Wall...
     * @param status
     *            The cell status, Alive or Dead
     * @param cellEnvironments
     *            The {@link Environment} of this cell
     * @return the color that the cell should have
     */
    public static Color cellColor(final Editable editable, final CellType cellType, final Status status,
            final StandardCellEnvironments cellEnvironments) {
        if (editable.equals(Editable.UNEDITABLE)) {
            if (cellEnvironments.equals(StandardCellEnvironments.RADIOACTIVE)) {
                return status.equals(Status.ALIVE) ? Colors.blend(Color.BLACK, Color.GREEN)
                        : Colors.blend(Color.BLACK, Colors.blend(Color.BLACK, Color.GREEN));
            } else if (cellType.equals(CellType.NORMAL)) {
                return Colors.blend(Color.RED, status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            } else if (cellType.equals(CellType.GOLDEN)) {
                return status.equals(Status.ALIVE) ? Colors.GOLD : Colors.LIGHT_GOLD;
            } else if (cellType.equals(CellType.WALL)) {
                return status.equals(Status.ALIVE) ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            } else {
                return Colors.blend(Color.RED, status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            }
        } else {
            if (cellEnvironments.equals(StandardCellEnvironments.RADIOACTIVE)) {
                return status.equals(Status.ALIVE) ? Colors.blend(Color.GREEN, Color.BLACK) : Colors.LIGHT_GREEN;
            } else if (cellType.equals(CellType.NORMAL)) {
                return status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
            } else if (cellType.equals(CellType.GOLDEN)) {
                return status.equals(Status.ALIVE) ? Colors.GOLD : Colors.LIGHT_GOLD;
            } else if (cellType.equals(CellType.WALL)) {
                return status.equals(Status.ALIVE) ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            } else {
                return status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
            }
        }
    }

    /**
     * Gives back a matrix of colors (Black and White) painting it from the given
     * one of Cell.
     * 
     * @param mat
     *            a Matrix of Cell to be mapped
     * @return the resulting Matrix of Color
     */
    public static Matrix<Color> colorDefaultCellMatrix(final Matrix<Cell> mat) {
        return mat.map(cell -> colorDefaultCell(cell.getStatus()));
    }

    /**
     * Gives back a matrix with the color that a pattern to show should have.
     * 
     * @param mat
     *            a Matrix of Status to be mapped
     * @return the resulting Matrix of Color
     */
    public static Matrix<Color> colorPattern(final Matrix<Status> mat) {
        return mat.map(s -> s.equals(Status.ALIVE) ? Color.GRAY : Color.WHITE);
    }

    /**
     * Gives back a matrix of colors (Black) painting it from the given one of
     * Status.
     * 
     * @param mat
     *            a Matrix of Status to be mapped
     * @return the resulting Matrix of Color
     */
    public static Matrix<Color> colorDefaultMatrix(final Matrix<Status> mat) {
        return mat.map(s -> colorDefaultCell(s));
    }

    /**
     * Method to color a simple cell.
     * 
     * @param status
     *            The cell {@link Status} that represent the cell
     * @return the color that the cell should have
     */
    public static Color colorDefaultCell(final Status status) {
        return Colors.cellColor(Editable.EDITABLE, CellType.NORMAL, status, StandardCellEnvironments.STANDARD);
    }

    /**
     * Method which gives a matrix of colors to be shown based on those representing
     * the status, type and the editable areas.
     * 
     * @param statusMatrix
     *            the current {@link Status} {@link Matrix}
     * @param cellTypeMatrix
     *            the matrix of {@link CellType}
     * @param editableMatrix
     *            the matrix of {@link Editable}
     * @param environment
     *            the {@link Environment}
     * @return a Matrix<{@link Color}>, ready to be displayed
     */
    public static Matrix<Color> colorMatrix(final Matrix<Status> statusMatrix, final Matrix<CellType> cellTypeMatrix,
            final Matrix<Editable> editableMatrix, final Environment environment) {
        if (environment.getHeight() != statusMatrix.getHeight() || environment.getWidth() != statusMatrix.getWidth()
                || environment.getHeight() != cellTypeMatrix.getHeight()
                || environment.getWidth() != cellTypeMatrix.getWidth()
                || environment.getHeight() != editableMatrix.getHeight()
                || environment.getWidth() != editableMatrix.getWidth()) {
            throw new IllegalArgumentException();
        }
        final Matrix<Color> colorMatrix = new ListMatrix<>(environment.getWidth(), environment.getHeight(), () -> null);
        for (int row = 0; row < environment.getHeight(); row++) {
            for (int col = 0; col < environment.getWidth(); col++) {
                colorMatrix.set(row, col,
                        Colors.cellColor(editableMatrix.get(row, col), cellTypeMatrix.get(row, col),
                                statusMatrix.get(row, col),
                                environment.getCellEnvironment(row, col) == StandardCellEnvironments.RADIOACTIVE
                                        ? StandardCellEnvironments.RADIOACTIVE
                                        : StandardCellEnvironments.STANDARD));
            }
        }
        return colorMatrix;
    }

    /**
     * Method which gives a matrix of colors to be shown based on those representing
     * the status, type and the environment used.
     * 
     * @param statusMatrix
     *            the current {@link Matrix} of {@link Status}
     * @param cellTypeMatrix
     *            the matrix of {@link CellType}
     * @param environment
     *            the {@link Environment}
     * @return a Matrix<{@link Color}>, ready to be displayed
     */
    public static Matrix<Color> colorMatrix(final Matrix<Status> statusMatrix, final Matrix<CellType> cellTypeMatrix,
            final Environment environment) {
        return Colors.colorMatrix(statusMatrix, cellTypeMatrix,
                new ListMatrix<>(environment.getWidth(), environment.getHeight(), () -> null)
                        .map(e -> Editable.EDITABLE),
                environment);
    }

    /**
     * Method which gives a matrix of colors to be shown based on those representing
     * the status and the environment.
     * 
     * @param statusMatrix
     *            the current {@link Status} {@link Matrix}
     * @param environment
     *            the {@link Environment}
     * @return a Matrix<{@link Color}>, ready to be displayed
     */
    public static Matrix<Color> colorMatrix(final Matrix<Status> statusMatrix, final Environment environment) {
        return Colors.colorMatrix(statusMatrix,
                new ListMatrix<>(environment.getWidth(), environment.getHeight(), () -> null).map(e -> CellType.NORMAL),
                environment);
    }

    /**
     * Method which gives a matrix of colors to be shown based on those representing
     * the status and those given in the level.
     * 
     * @param currentStatus
     *            is the current {@link Matrix} of {@link Status} of the
     *            {@link Level}
     * @param level
     *            is the current Level
     * @return A Matrix of Color of the current status with the current level
     *         parameters
     */
    public static Matrix<Color> colorMatrix(final Matrix<Status> currentStatus, final Level level) {
        return colorMatrix(currentStatus, level.getCellTypeMatrix(), level.getEditableMatrix(),
                level.getEnvironmentMatrix());
    }

    /**
     * Method which gives a matrix of colors to be shown based on those given in the
     * level.
     * 
     * @param level
     *            is the current level
     * @return a matrix<{@link Color}> of this level with statusMatrix is
     *         initialStateMatrix
     */
    public static Matrix<Color> colorMatrix(final Level level) {
        return colorMatrix(level.getInitialStateMatrix(), level.getCellTypeMatrix(), level.getEditableMatrix(),
                level.getEnvironmentMatrix());
    }

    /**
     * Method which gives a color to be assumed by the cell based on the status.
     * 
     * @param status
     *            Status of the cell
     * @return color that the cell should have in select mode.
     */
    public static Color selectMode(final Status status) {
        return status.equals(Status.DEAD) ? Color.ORANGE : Color.RED;
    }
}
