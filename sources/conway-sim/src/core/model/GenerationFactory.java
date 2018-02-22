package core.model;

import static core.model.Status.ALIVE;

import java.util.Objects;

import core.utils.Matrix;

/**
 * Utility class for creation of {@link Generation}.
 *
 */
public final class GenerationFactory {

    private GenerationFactory() { }

    /**
     * A method to clone a {@link Generation}. Note that cells are copied, whereas the environment is the exact same.
     * @param generation is the generation to clone
     * @return the cloned generation
     */
    public static Generation copyOf(final Generation generation) {
        return GenerationFactory.from(generation.getCellMatrix().map(c -> new CellImpl(c.getStatus())), generation.getEnviroment());
    }

    /**
     * Creates a new {@link Generation} from a given {@link Matrix<Cell>} and an {@link Environment} of the same dimensions, without making copies.
     * @param cellMatrix the {@link Matrix<Cell>}
     * @param e the {@link Environment}
     * @return the {@link Generation} created from the arguments
     */
    public static Generation from(final Matrix<Cell> cellMatrix, final Environment e) {
        Objects.requireNonNull(cellMatrix);
        Objects.requireNonNull(e);
        if (cellMatrix.getHeight() != e.getHeight() || cellMatrix.getWidth() != e.getWidth()) {
            throw new IllegalArgumentException("Cell Matrix and Environment must have the same dimensions.");
        }
        return new Generation() {

            @Override
            public Matrix<Boolean> getAliveMatrix() {
                return cellMatrix.map(c -> c.getStatus().equals(ALIVE));
            }

            @Override
            public int getWidth() {
                return cellMatrix.getWidth();
            }

            @Override
            public int getHeight() {
                return cellMatrix.getHeight();
            }

            @Override
            public Environment getEnviroment() {
                return e;
            }

            @Override
            public Matrix<Cell> getCellMatrix() {
                return cellMatrix;
            }

            @Override
            public String toString() {
                return this.getCellMatrix().toString();
            }
        };
    }
}
