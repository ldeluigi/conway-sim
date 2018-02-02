package core.model;

import core.utils.Matrix;

/**
 * An interface for a cell generation.
 *
 */
public interface Generation {

    /**
     * Returns the matrix of alive cells.
     * @return A boolean matrix where true means that the cell is alive
     */
    Matrix<Boolean> getAliveMatrix();
}
