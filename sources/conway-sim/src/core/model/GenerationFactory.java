package core.model;

import java.util.Objects;

import core.utils.Matrices;
import core.utils.Matrix;

/**
 * Utility class for creation of {@link Generation}.
 *
 */
public final class GenerationFactory {

    private GenerationFactory() {
    }

    /**
     * A method to clone a {@link Generation}. Note that cells are copied with
     * {@link Cell#copy()}, whereas the {@link Environment} is the exact same.
     * 
     * @param generation
     *            is the generation to clone
     * @return the cloned generation
     */
    public static Generation copyOf(final Generation generation) {
        return GenerationFactory.from(generation.getCellMatrix().map(c -> c.copy()), generation.getEnviroment());
    }

    /**
     * Creates a new {@link Generation} from a given {@link Matrix<Cell>} and an
     * {@link Environment} of the same dimensions, without making copies.
     * 
     * @param cellMatrix
     *            the {@link Matrix<Cell>}
     * @param e
     *            the {@link Environment}
     * @return the {@link Generation} created from the arguments
     */
    public static Generation from(final Matrix<? extends Cell> cellMatrix, final Environment e) {
        Objects.requireNonNull(cellMatrix);
        Objects.requireNonNull(e);
        if (cellMatrix.getHeight() != e.getHeight() || cellMatrix.getWidth() != e.getWidth()) {
            throw new IllegalArgumentException(
                    "Cell Matrix and Environment must have the same dimensions. (" + cellMatrix.getWidth() + ","
                            + cellMatrix.getHeight() + " - " + e.getWidth() + "," + e.getHeight() + ")");
        }
        return new Generation() {

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
                return Matrices.unmodifiableMatrix(cellMatrix);
            }

            @Override
            public String toString() {
                return this.getCellMatrix().toString();
            }
        };
    }
}
