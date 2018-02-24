package core.model;


import core.utils.Matrix;

/**
 * A factory used to create {@link Environment}.
 */
public interface EnvironmentFactory {

    /**
     * A method that creates an {@link Environment} with the standard rules of Conway's Game of Life.
     * @param width of the Environment
     * @param height of the Environment
     * @return the Environment as specified
     */
    Environment standardRules(int width, int height);

    /**
     * A method that creates an {@link Environment} from the given {@link CellEnvironment} matrix.
     * @param envMatrix a {@link Matrix<CellEnvironment>}
     * @return the Environment based on the given matrix
     */
    Environment from(Matrix<CellEnvironment> envMatrix);

    /**
     * A method that creates an {@link Environment} filled with the given {@link CellEnvironment}r.
     * @param width of the Environment
     * @param height of the Environment
     * @param cellEnv the {@link CellEnvironment}
     * @return the Environment as specified
     */
    Environment fill(int width, int height, CellEnvironment cellEnv);
}
