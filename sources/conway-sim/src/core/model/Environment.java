package core.model;

/**
 * This is the main environment for the generation.
 * 
 *
 */

public interface Environment {

    /**
     * 
     * @return the current height of the environment
     */
    int getHeight();

    /**
     * 
     * @return the current height
     */
    int getWidth();


    /**
     * 
     * @param x height of the environment to be used
     * @param y width of the environment to be used
     * @param env the environment to be used
     */
    void setEnvironment(int x, int y, CellEnvironment env);
}
