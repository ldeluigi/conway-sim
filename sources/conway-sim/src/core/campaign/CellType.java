package core.campaign;

/**
 * Represents the role of a single cell inside a level.
 */
public enum CellType {
	
/**
 * Normal cells have normal behavior and no special role.
 */
NORMAL,
/**
 * Golden cell are the key to victory.
 */
GOLDEN,
/**
 * These cells never die and start as alive, making a permanent alive wall in the map.
 */
ALIVE_WALL,
/**
 * These cells never live and start as dead, making a permanent dead wall in the map.
 */
DEAD_WALL;
}
