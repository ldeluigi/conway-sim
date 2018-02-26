package controller.io;
/**
 * 
 * 
 *
 */
public class RLESaver {
    private boolean[][] grid;
    /**
     * 
     * @return
     */
    private boolean[][] getGrid(){
        return this.grid;
    }
    /**
     * 
     * @param grid
     * @return the grid in RLE format
     */
    private String gridToRLE(boolean[][] grid) {
        //Method TBI
        return null;
    }
    /**
     * This method uses the inside getGrid() that extracts the grid from the table,
     * then it translate it to an RLE format using gridToRLE and than saves it, returns true.
     * @return true if is successful
     */
    public boolean saveToFile() {
        //Method TBI
        return true;
    }
}
