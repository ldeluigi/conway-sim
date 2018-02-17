package core.model;

import core.utils.Matrix;

/**
 * 
 * 
 *
 */
public class GenerationImpl implements Generation {

    private final Matrix<Boolean> matrix;

    /**
     * Constructor for a new Generation.
     * @param matrix is the matrix of the generation
     */
    public GenerationImpl(final Matrix<Boolean> matrix) {
        this.matrix = matrix;
    }

    /**
     * This is the method to invoke in order to get the matrix of alive cells.
     * @return the alive matrix
     */
    public Matrix<Boolean> getAliveMatrix() {
        return this.matrix;
    }

}
