package core.utils;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class for {@link Matrix} objects.
 */
public final class Matrices {

    private Matrices() {
    }

    /**
     * A method to modify a {@link Matrix<X>} by applying a smaller matrix of the
     * same type.
     * 
     * @param <X>
     *            the generic type of matrix
     * @param main
     *            is the larger {@link Matrix<X>} where the smaller should be
     *            applied
     * @param x
     *            is the row of the top left cell of the matrix
     * @param y
     *            is the column of the top left cell of the matrix
     * @param smaller
     *            is the {@link Matrix<X>} to be merged with main
     * @return the modified matrix
     */
    public static <X> Matrix<X> mergeXY(final Matrix<X> main, final int x, final int y,
            final Matrix<? extends X> smaller) {
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
     * Copies a rectangular portion of a given {@link Matrix} to a new matrix of the
     * required dimension.
     * 
     * @param <X>
     *            the generic type of matrix
     * @param from
     *            the matrix from which the new one is copied
     * @param fromRow
     *            the first row to copy
     * @param toRow
     *            the last (inclusive) row to copy
     * @param fromColumn
     *            the first column to copy
     * @param toColumn
     *            the last (inclusive) column to copy
     * @return a new matrix taken from the given one
     */
    public static <X> Matrix<X> cut(final Matrix<? extends X> from, final int fromRow, final int toRow,
            final int fromColumn, final int toColumn) {
        if (fromRow < 0 || toRow < fromRow || toRow >= from.getHeight() || fromColumn < 0 || toColumn < fromColumn
                || toColumn >= from.getWidth()) {
            throw new IllegalArgumentException("Input coordinates are invalid (from row: " + fromRow + " to " + toRow
                    + " - max is " + (from.getHeight() - 1) + "; from column: " + fromColumn + " to " + toColumn
                    + " - max is " + (from.getWidth() - 1) + ")");
        }
        return new ListMatrix<>(
                IntStream
                        .rangeClosed(fromRow, toRow).mapToObj(r -> IntStream.rangeClosed(fromColumn, toColumn)
                                .mapToObj(c -> (X) from.get(r, c)).collect(Collectors.toList()))
                        .collect(Collectors.toList()));
    }

    /**
     * Returns an unmodifiable view of the specified matrix.
     * 
     * @param matrix
     *            the {@link Matrix} to wrap
     * @param <X>
     *            the generic type of the matrix
     * @return a wrapped matrix that protects from modifications
     */
    public static <X> Matrix<X> unmodifiableMatrix(final Matrix<? extends X> matrix) {
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
            public Stream<X> stream() {
                return matrix.stream().map(x -> (X) x);
            }

            @Override
            public void reverseEachRow() {
                throw new UnsupportedOperationException("This matrix cannot be modified");
            }
        };
    }

    /**
     * Returns true only if the two Matrices have the same dimensions and every
     * corresponding object equals the counterpart.
     * 
     * @param a
     *            a matrix
     * @param b
     *            a second matrix
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

    /**
     * Calls {@link Matrix#map(Function)} with (x -> x) as visitor and returns the
     * result. The effect is to recreate a new matrix data structure but with the
     * same references to the contained objects.
     * 
     * @param <X>
     *            generic type
     * @param source
     *            a matrix
     * @return the Matrix created with {@link Matrix#map} method
     */
    public static <X> Matrix<X> copyOf(final Matrix<? extends X> source) {
        return source.map(x -> x);
    }
}
