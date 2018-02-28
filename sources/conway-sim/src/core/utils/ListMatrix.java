package core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.function.Supplier;

/**
 * Matrix implementation with ArrayLists.
 * @param <X> generic type
 */
public final class ListMatrix<X> implements Matrix<X> {

    private List<List<X>> matrix;

    /**
     * Constructor that takes a two-dimensional array.
     * @param matrix of {@link X}
     */
    public ListMatrix(final X[][] matrix) {
        Objects.requireNonNull(matrix);
        if (matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Input array was empty or had an empty first row.");
        }
        for (final X[] row : matrix) {
            if (row.length != matrix[0].length) {
                throw new IllegalArgumentException("Input array wasn't rectangular.");
            }
        }
        this.matrix = new ArrayList<>();
        Arrays.asList(matrix).forEach(row -> {
            this.matrix.add(new ArrayList<>(Arrays.asList(row)));
        });
    }

    /**
     * Constructor that takes a list of list. Must be rectangular.
     * @param matrix of {@link X}
     */
    public ListMatrix(final List<List<X>> matrix) {
        Objects.requireNonNull(matrix);
        if (matrix.isEmpty() || matrix.get(0).isEmpty()) {
            throw new IllegalArgumentException("Input list was empty or had an empty list as first row.");
        }
        if (!matrix.stream().allMatch(row -> row.size() == matrix.get(0).size())) {
            throw new IllegalArgumentException("Input list wasn't rectangular.");
        }
        this.matrix = new ArrayList<>();
        matrix.forEach(row -> {
            this.matrix.add(new ArrayList<>(row));
        });
    }

    /**
     * Constructor that takes the dimensions and fills the matrix with a {@link Supplier}.
     * @param width that is the number of columns
     * @param height that is the number of rows
     * @param supplier that produces elements of type {@link X}
     */
    public ListMatrix(final int width, final int height, final Supplier<X> supplier) {
        Objects.requireNonNull(supplier);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimension must be greater than zero.");
        }
        this.matrix = new ArrayList<>(height);
        IntStream.range(0, height).forEach(row -> {
            this.matrix.add(row, new ArrayList<>(width));
            IntStream.range(0, width).forEach(column -> {
                this.matrix.get(row).add(column, supplier.get());
            });
        });
    }

    @Override
    public X get(final int row, final int column) {
        return this.matrix.get(row).get(column);
    }

    @Override
    public void rotateClockwise(final int times) {
        if (times % 4 == 1) {
            transpose();
            reverseEachRow();
        } else if (times % 4 == 2) {
            rotateClockwise(1);
            rotateClockwise(1);
        } else if (times % 4 == 3) {
            reverseEachRow();
            transpose();
        }
    }

    private void reverseEachRow() {
        this.matrix.forEach(row -> {
            Collections.reverse(row);
        });
    }

    private void transpose() {
        final List<List<X>> temp = new ArrayList<>();
        IntStream.range(0, this.getWidth()).forEach(i -> {
            temp.add(new ArrayList<>());
        });
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                temp.get(j).add(i, this.get(i, j));
            }
        }
        this.matrix = temp;
    }

    @Override
    public void set(final int row, final int column, final X value) {
        Objects.requireNonNull(value);
        this.matrix.get(row).set(column, value);
    }

    @Override
    public int getHeight() {
        return this.matrix.size();
    }

    @Override
    public int getWidth() {
        return this.matrix.get(0).size();
    }

    @Override
    public <Y> Matrix<Y> map(final Function<? super X, ? extends Y> mapper) {
        final List<List<Y>> temp = new ArrayList<>();
        this.matrix.forEach(row -> {
            temp.add(row.stream().map(mapper).collect(Collectors.toList()));
        });
        return new ListMatrix<>(temp);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.matrix == null) ? 0 : this.matrix.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Matrix)) {
            return false;
        }
        final Matrix<?> other = (Matrix<?>) obj;
        return Matrices.areEquals(this, other);
    }

    @Override
    public String toString() {
        return this.matrix.stream().map(l -> l.toString()).collect(Collectors.joining("\n", "[", "]"));
    }

}
