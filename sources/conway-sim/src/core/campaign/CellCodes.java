package core.campaign;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import core.model.SimpleCell;

/**
 * Utility static class that collects cellCodes globally. Cell codes are used to
 * recognize which cell type hides under the {@link Cell} interface without
 * using the "instance of" construct. It's possible that two or more cell types
 * have the same code. This is the case for specialized cells that want to be
 * considered as their supertype.
 * 
 * The cell names are case insensitive.
 */
public final class CellCodes {

    private static final Map<String, Integer> CODES = new HashMap<>();

    static {
        CellCodes.CODES.put("normal", SimpleCell.STANDARD_CELL_CODE);
        CellCodes.CODES.put("wall", NeverChangingCell.NEVER_CHANGING_CODE);
        CellCodes.CODES.put("gold", GameWinningCell.GAME_WINNING_CODE);
    }

    private CellCodes() {
    }

    /**
     * Getter for the set of current saved cell types.
     * 
     * @return a {@link Set} of strings that corresponds to cell types
     */
    public static synchronized Set<String> availableCells() {
        return CODES.keySet();
    }

    /**
     * Getter for the code of a given cell type.
     * 
     * @param cellName
     *            the string representing the cell type
     * @return its code (not guaranteed to be unique for that type)
     */
    public static synchronized int get(final String cellName) {
        return CODES.get(cellName.toLowerCase(Locale.getDefault()));
    }

    /**
     * Adds a new entry (cell type, code) to the global map.
     * 
     * @param name
     *            the unique name for that cell type
     * @param cellCode
     *            its code or the code of another cell
     */
    public static synchronized void add(final String name, final int cellCode) {
        CODES.put(name.toLowerCase(Locale.getDefault()), cellCode);
    }

    /**
     * Returns the maximum code that appears in the map + 1.
     * 
     * @return an integer that is almost sure to be available as a unique code (the
     *         bad case is the case of integer overflowing to an already used value)
     */
    public static synchronized int getFirstAvailableCode() {
        return CODES.values().stream().mapToInt(x -> x).max().orElse(0) + 1;
    }
}
