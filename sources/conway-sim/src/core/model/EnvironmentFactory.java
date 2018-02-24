package core.model;

import core.utils.ListMatrix;
import core.utils.Matrix;

/**
 * A factory used to create {@link Environment}.
 */
public final class EnvironmentFactory {

    private EnvironmentFactory() { }
    /**
     * A method that creates an {@link Environment} with the standard rules of Conway's Game of Life.
     * @param width of the Environment
     * @param height of the Environment
     * @return the Environment as specified
     */
    public static Environment standardRules(final int width, final int height) {
        return new Environment() {

            private int envHeight = height; 
            private int envWidth = width;

			@Override
			public int getHeight() {
				return this.envHeight;
			}

			@Override
			public int getWidth() {
				return this.envWidth;
			}

			@Override
			public CellEnvironment getCellEnvironment(final int x, final int y) {
				return StandardCellEnvironments.STANDARD;
			}

        };
    }
    /**
     * A method that creates an {@link Environment} from the given {@link CellEnvironment} matrix.
     * @param envMatrix a {@link Matrix<CellEnvironment>}
     * @return the Environment based on the given matrix
     */
    public static Environment from(final Matrix<CellEnvironment> envMatrix) {
        return new Environment() {

        	private Matrix<CellEnvironment> matrix = envMatrix;

        	@Override
			public int getHeight() {
				return this.matrix.getHeight();
			}

			@Override
			public int getWidth() {
				return this.matrix.getWidth();
			}

			@Override
			public CellEnvironment getCellEnvironment(final int x, final int y) {
				return this.matrix.get(x, y);
			}

        };
    }

    /**
     * A method that creates an {@link Environment} filled with the given {@link CellEnvironment}r.
     * @param width of the Environment
     * @param height of the Environment
     * @param cellEnv the {@link CellEnvironment}
     * @return the Environment as specified
     */
    public static Environment fill(final int width, final int height, final CellEnvironment cellEnv) {
        return new Environment() {

        	private int envHeight = height;
        	private int envWidth = width;
        	private CellEnvironment env = cellEnv;
        	private Matrix<CellEnvironment> matrix = new ListMatrix<CellEnvironment>(this.envWidth, this.envHeight, () -> this.env);

        	@Override
			public int getHeight() {
				// TODO Auto-generated method stub
				return this.envHeight;
			}

			@Override
			public int getWidth() {
				// TODO Auto-generated method stub
				return this.envWidth;
			}

			@Override
			public CellEnvironment getCellEnvironment(final int x, final int y) {
				return this.matrix.get(x, y);
			}

        };
    }

}
