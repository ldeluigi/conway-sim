package core.campaign;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import core.model.SimpleCell;

public final class CellCodes {
	
	private static final Map<String, Integer> CODES = new HashMap<>();
	
	static {
		CellCodes.CODES.put("normal", SimpleCell.STANDARD_CELL_CODE);
		CellCodes.CODES.put("wall", NeverChangingCell.NEVER_CHANGING_CODE);
		CellCodes.CODES.put("gold", GameWinningCell.GAME_WINNING_CODE);
	}

	private CellCodes() { }
	
	public synchronized static Set<String> availableCells() {
		return CODES.keySet();
	}
	
	public synchronized static int get(final String cellName) {
		return CODES.get(cellName.toLowerCase(Locale.getDefault()));
	}
	
	public synchronized static void add(final String name, final int cellCode) {
		CODES.put(name.toLowerCase(Locale.getDefault()), cellCode);
	}
	
	public synchronized static int getFirstAvailableCode() {
		return CODES.values().stream().mapToInt(x -> x).max().orElse(1);
	}
}
