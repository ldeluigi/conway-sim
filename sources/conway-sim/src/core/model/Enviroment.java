package core.model;

/**
 * This is the main environment for the generation.
 * 
 *
 */

public interface Enviroment {

    /**
     * Gets the height of {@link CellEnviroment} matrix.
     * @return the current height of the environment matrix
     */
    int getHeight();

    /**
     * Gets the height of {@link CellEnviroment} matrix.
     * @return the current height of the environment matrix
     */
    int getWidth();


    /**
     * 
     * @param x is the row where the {@link CellEnviroment} should be set
     * @param y is the column where the {@link CellEnviroment} should be set
     * @param env the {@link CellEnviroment} to be used
     */
    void setEnvironment(int x, int y, CellEnviroment env);
}
