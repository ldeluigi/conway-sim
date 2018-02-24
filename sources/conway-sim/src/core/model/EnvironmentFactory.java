package core.model;

import core.utils.Matrix;

/**
 * A factory used to create {@link Environment}.
 */
public class EnvironmentFactory {

    /**
     * A method that creates an {@link Environment} with the standard rules of Conway's Game of Life.
     * @param width of the Environment
     * @param height of the Environment
     * @return the Environment as specified
     */
    public Environment standardRules(final int width, final int height) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * A method that creates an {@link Environment} from the given {@link CellEnvironment} matrix.
     * @param envMatrix a {@link Matrix<CellEnvironment>}
     * @return the Environment based on the given matrix
     */
    public Environment from(final Matrix<CellEnvironment> envMatrix) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * A method that creates an {@link Environment} filled with the given {@link CellEnvironment}r.
     * @param width of the Environment
     * @param height of the Environment
     * @param cellEnv the {@link CellEnvironment}
     * @return the Environment as specified
     */
    public Environment fill(final int width, final int height, final CellEnvironment cellEnv) {
        // TODO Auto-generated method stub
        return null;
    }

}
