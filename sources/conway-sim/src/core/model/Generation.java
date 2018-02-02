package core.model;

import core.utils.Matrix;

/**
 * An interface for a cell generation.
 *
 */
public interface Generation {

    /**
     * Returns the {@link Matrix} of alive cells.
     * @return A boolean {@link Matrix} where true means that the cell is alive
     */
    Matrix<Boolean> getAliveMatrix();
}
