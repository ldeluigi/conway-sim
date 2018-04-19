package core.model;

import core.utils.Matrix;

/**
 * A factory used to create {@link Environment}.
 */
public final class EnvironmentFactory {

    private EnvironmentFactory() {
    }

    /**
     * A method that creates an {@link Environment} with the standard rules of Conway's Game of
     * Life.
     * 
     * @param width
     *            of the Environment
     * @param height
     *            of the Environment
     * @return the Environment as specified
     */
    public static Environment standardRules(final int width, final int height) {
        return fill(width, height, StandardCellEnvironments.STANDARD);
    }

    /**
     * A method that creates an {@link Environment} from the given {@link CellEnvironment} matrix.
     * 
     * @param envMatrix
     *            a {@link Matrix<CellEnvironment>}
     * @return the Environment based on the given matrix
     */
    public static Environment from(final Matrix<? extends CellEnvironment> envMatrix) {
        return new Environment() {

            @Override
            public int getHeight() {
                return envMatrix.getHeight();
            }

            @Override
            public int getWidth() {
                return envMatrix.getWidth();
            }

            @Override
            public CellEnvironment getCellEnvironment(final int x, final int y) {
                return envMatrix.get(x, y);
            }

        };
    }

    /**
     * A method that creates an {@link Environment} filled with the given {@link CellEnvironment}.
     * 
     * @param width
     *            of the Environment
     * @param height
     *            of the Environment
     * @param cellEnv
     *            the {@link CellEnvironment}
     * @return the Environment as specified
     */
    public static Environment fill(final int width, final int height,
            final CellEnvironment cellEnv) {
        return new Environment() {

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public CellEnvironment getCellEnvironment(final int x, final int y) {
                return cellEnv;
            }

        };
    }

}
