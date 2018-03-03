package core.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * 
 *
 */
public final class Matrices {

    private Matrices() { }

    /**
     * A method to modify a {@link Matrix<X>} by applying a smaller matrix of the same type.
     * @param <X> the generic type of matrix
     * @param main is the larger {@link Matrix<X>} where the smaller should be applied
     * @param x is the row of the top left cell of the matrix
     * @param y is the column of the top left cell of the matrix
     * @param smaller is the {@link Matrix<X>} to be merged with main
     * @return the modified matrix
     */
    public static <X> Matrix<X> mergeXY(final Matrix<X> main, final int x, final int y, final Matrix<X> smaller) {
        Objects.requireNonNull(main);
        Objects.requireNonNull(smaller);
        if (x < 0 || y < 0 || x + smaller.getHeight() > main.getHeight() || y + smaller.getWidth() > main.getWidth()) {
            throw new IllegalArgumentException("Invalid position or invalid matrices dimensions.");
        }
        IntStream.range(0, smaller.getHeight()).forEach(row -> {
            IntStream.range(0, smaller.getWidth()).forEach(column -> {
                main.set(row + x, column + y, smaller.get(row, column));
            });
        });
        return main;
    }

    /**
     *  Returns an unmodifiable view of the specified matrix.
     * @param matrix the {@link Matrix} to wrap
     * @param <X> the generic type of the matrix
     * @return a wrapped matrix that protects from modifications
     */
    public static <X> Matrix<X> unmodifiableMatrix(final Matrix<X> matrix) {
        return new Matrix<X>() {
            @Override
            public X get(final int row, final int column) {
                return matrix.get(row, column);
            }
            @Override
            public void rotateClockwise(final int times) {
                throw new UnsupportedOperationException("This matrix cannot be modified");
            }
            @Override
            public void set(final int row, final int column, final X value) {
                throw new UnsupportedOperationException("This matrix cannot be modified");
            }
            @Override
            public int getHeight() {
                return matrix.getHeight();
            }
            @Override
            public int getWidth() {
                return matrix.getWidth();
            }
            @Override
            public <Y> Matrix<Y> map(final Function<? super X, ? extends Y> mapper) {
                return matrix.map(mapper);
            }
            @Override
            public String toString() {
                return matrix.toString();
            }
            @Override
            public boolean equals(final Object obj) {
                return matrix.equals(obj);
            }
            @Override
            public int hashCode() {
                return matrix.hashCode();
            }
            @Override
            public void forEach(final Consumer<? super X> action) {
                matrix.forEach(action);
            }
        };
    }

    /**
     * Returns true only if the two Matrices have the same dimensions and every corresponding cell equals the counterpart.
     * @param a a matrix
     * @param b a second matrix
     * @return true if they are equals
     */
    public static boolean areEquals(final Matrix<?> a, final Matrix<?> b) {
        if (a.getHeight() != b.getHeight() || a.getWidth() != b.getWidth()) {
            return false;
        }
        for (int i = 0; i < a.getHeight(); i++) {
            for (int j = 0; j < a.getWidth(); j++) {
                if (!a.get(i, j).equals(b.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
