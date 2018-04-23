package view;

import java.awt.Color;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.campaign.CellType;
import core.campaign.Editable;
import core.model.Cell;
import core.model.Status;
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
}
