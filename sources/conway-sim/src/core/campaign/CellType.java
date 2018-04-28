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
     * These cells never change and start as alive or dead.
     */
    WALL;
}
