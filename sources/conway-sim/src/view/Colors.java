package view;

import java.awt.Color;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.campaign.CellType;
import core.campaign.Editable;
import core.model.Cell;
import core.model.Environment;
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
    public static final Color GOLD = new Color(255, 215, 0);

    private static final BiFunction<Status, Color, Color> STATUSTOCOLOR = (s, c) -> s.equals(Status.ALIVE) ? c
            : Color.WHITE;
    private static final Function<Status, Color> ALIVETOBLACK = s -> STATUSTOCOLOR.apply(s, Color.BLACK);
    private static final Function<Status, Color> ALIVETOGRAY = s -> STATUSTOCOLOR.apply(s, Color.GRAY);
    private static final Function<Cell, Color> CELLTOCOLOR = c -> ALIVETOBLACK.apply(c.getStatus());

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
        return new Color(
                (a.getRed() * a.getAlpha() + b.getRed() * b.getAlpha())
                        / (a.getAlpha() + b.getAlpha()),
                (a.getGreen() * a.getAlpha() + b.getGreen() * b.getAlpha())
                        / (a.getAlpha() + b.getAlpha()),
                (a.getBlue() * a.getAlpha() + b.getBlue() * b.getAlpha())
                        / (a.getAlpha() + b.getAlpha()),
                (a.getAlpha() + b.getAlpha()) / 2);
    }

    /**
     * 
     * @param editable
     *                The status that represent if a cell is editable or not
     * @param cellType
     *                The cell type,
     *                Normal, Gold, Wall...
     * @param status
     *                The cell status,
     *                Alive or Dead
     * @return the color that the cell should have
     */
    public static Color cellColor(final Editable editable, final CellType cellType, final Status status) {
        if (editable.equals(Editable.UNEDITABLE)) {
            if (cellType.equals(CellType.NORMAL)) {
                return Colors.blend(Color.RED, status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            } else if (cellType.equals(CellType.GOLDEN)) {
                return Colors.blend(Colors.GOLD, status.equals(Status.ALIVE) ? new Color(0, 0, 0, 100) : Color.WHITE);
            } else if (cellType.equals(CellType.WALL)) {
                return status.equals(Status.ALIVE) ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            } else {
                return Colors.blend(Color.RED, status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            }
        } else {
            if (cellType.equals(CellType.NORMAL)) {
                return status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
            } else if (cellType.equals(CellType.GOLDEN)) {
                return Colors.blend(Colors.GOLD, status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE);
            } else if (cellType.equals(CellType.WALL)) {
                return status.equals(Status.ALIVE) ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            } else {
                return status.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE;
            }
        }
    }

    /**
     * Gives back the color representing the given Status.
     *
     * @param s the Status to be mapped
     * @return the Color representing that Status 
     */
    public static Color colorSingleCell(final Status s) {
        return Colors.ALIVETOBLACK.apply(s);
    }

    /**
     * Gives back a matrix of colors (Black and White) painting it from the given one of Cell.
     * 
     * @param mat a Matrix of Cell to be mapped
     * @return the resulting Matrix of Color
     */
    public static Matrix<Color> cellToColor(final Matrix<Cell> mat) {
        return mat.map(Colors.CELLTOCOLOR);
    }

    /**
     * Gives back a matrix of colors (Gray)  painting it from the given one of Status.
     * @param mat a Matrix of Status to be mapped
     * @return the resulting Matrix of Color
     */
    public static Matrix<Color> statusToGray(final Matrix<Status> mat) {
        return mat.map(Colors.ALIVETOGRAY);
    }

    /**
     * Gives back a matrix of colors (Black) painting it from the given one of Status.
     * 
     * @param mat a Matrix of Status to be mapped
     * @return the resulting Matrix of Color
     */
    public static Matrix<Color> statusToBlack(final Matrix<Status> mat) {
        return mat.map(Colors.ALIVETOBLACK);
    }

    /**
     * @param status
     *            The cell {@link Status} that represent the cell
     * @return the color that the cell should have
     */
    public static Color cellNormalColor(final Status status) {
        return Colors.cellColor(Editable.EDITABLE, CellType.NORMAL, status);
    }

    /**
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
                colorMatrix.set(row, col, Colors.cellColor(editableMatrix.get(row, col), cellTypeMatrix.get(row, col),
                        statusMatrix.get(row, col)));
            }
        }
        return colorMatrix;
    }

    /**
     * 
     * @param statusMatrix
     *            the current {@link Status} {@link Matrix}
     * @param cellTypeMatrix
     *            the matrix of {@link CellType}
     * @param environment
     *            the {@link Environment}
     * @return a Matrix<{@link Color}>, ready to be displayed
     */
    public static Matrix<Color> colorEditableMatrix(final Matrix<Status> statusMatrix,
            final Matrix<CellType> cellTypeMatrix, final Environment environment) {
        return Colors.colorMatrix(statusMatrix, cellTypeMatrix,
                new ListMatrix<>(environment.getWidth(), environment.getHeight(), () -> null)
                        .map(e -> Editable.EDITABLE),
                environment);
    }

    /**
     * 
     * @param statusMatrix
     *            the current {@link Status} {@link Matrix}
     * @param environment
     *            the {@link Environment}
     * @return a Matrix<{@link Color}>, ready to be displayed
     */
    public static Matrix<Color> colorNormalMatrix(final Matrix<Status> statusMatrix, final Environment environment) {
        return Colors.colorEditableMatrix(statusMatrix,
                new ListMatrix<>(environment.getWidth(), environment.getHeight(), () -> null).map(e -> CellType.NORMAL),
                environment);
    }

}
