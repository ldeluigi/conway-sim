package view;

import java.awt.Color;

import core.campaign.CellType;
import core.campaign.Editable;
import core.model.Status;

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
}
