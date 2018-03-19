package view.swing.sandbox;

/**
 * SINGLETON.
 * 
 */
public final class GridSize {

    private static final int MAX = 200;
    private static final int MIN = 20;

    private int width = 100;
    private int height = 100;
    private static final GridSize SINGLETON = new GridSize();

    private GridSize() { }

    /**
     * 
     * @return a g
     */
    public static synchronized GridSize gridSize() {
        return SINGLETON;
    }

    /**
     * 
     * @return width
     */
    public int getGridWidht() {
        return this.width;
    }

    /**
     * 
     * @return height
     */
    public int getGridHeight() {
        return this.height;
    }

    /**
     * 
     * @param width the width
     */
    public void setGridWidht(final int width) {
        this.width = width > MAX ? MAX : width < MIN ? MIN : width;
    }

    /**
     * 
     * @param height the height
     */
    public void setGridHeight(final int height) {
        this.height = height > MAX ? MAX : height < MIN ? MIN : height;
    }

}
