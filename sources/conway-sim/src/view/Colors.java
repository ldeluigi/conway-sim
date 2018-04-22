package view;

import java.awt.Color;

import core.campaign.CellType;
import core.campaign.Editable;
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
     * 
     * @param editable
     *            The {@link Editable} that represent the cell
     * @param cellType
     *            The {@link CellType} that represent the cell
     * @param status
     *            The cell {@link Status} that represent the cell
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
