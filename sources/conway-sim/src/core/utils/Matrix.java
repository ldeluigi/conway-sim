package core.utils;

import java.util.function.Function;

/**
 * A generic matrix with useful utility methods.
 * 
 * @param <X>
 */
public interface Matrix<X> {

    /**
     * Returns the item from the given position.
     * 
     * @param row of the item
     * @param column of the item
     * @return the item
     */
    X get(int row, int column);

    /**
     * Rotates the matrix by 90 degrees clockwise.
     * @param times the number of rotations
     */
    void rotateClockwise(int times);

    /**
     * Sets the given value at the given position.
     * 
     * @param row of the item
     * @param column of the item
     * @param value of the item
     */
    void set(int row, int column, X value);

    /**
     * Returns the height of the matrix.
     * 
     * @return the height of the matrix
     */
    int getHeight();

    /**
     * Returns the width of the matrix.
     * 
     * @return the width of the matrix
     */
    int getWidth();

    /**
     * A method to map this matrix to a new one of the same dimension.
     * @param mapper to create elements of the new matrix.
     * @param <Y> the type of the new matrix
     * @return the new mapped matrix
     */
    <Y> Matrix<Y> map(Function<? super X, ? extends Y> mapper);
}
