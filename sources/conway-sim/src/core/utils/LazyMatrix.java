package core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A matrix that keeps record of every position edited while returns a default value for the others.
 * 
 * @param <X>
 *            the generic type of this matrix
 */
public class LazyMatrix<X> implements Matrix<X> {

    private final Map<Position, X> edits = new HashMap<>();
    private final X base;
    private int width;
    private int height;

    /**
     * Creates a matrix from a base that is returned for every get call, except for edited
     * positions, which are kept in a list.
     * 
     * @param width
     *            the width of the matrix
     * @param height
     *            the height of the matrix
     * @param base
     *            the base (initial value) for each cell
     */
    public LazyMatrix(final int width, final int height, final X base) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Height and Width must be greater than 0.");
        }
        this.base = base;
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if that position was ever edited and if not returns base, otherwise returns the saved
     * last edit.
     */
    @Override
    public X get(final int row, final int column) {
        if (!checkBounds(row, column)) {
            throw new IllegalArgumentException(
                    "Input position was out of bounds (" + this.width + "," + this.height + ")");
        }
        final Position p = new Position(row, column);
        if (edits.containsKey(p)) {
            return edits.get(p);
        }
        return base;
    }

    /**
     * Rotates the matrix by doing transposition and reversing rows.
     */
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

    /**
     * Adds the value to the edit list, which will be used before the base in the get method.
     */
    @Override
    public void set(final int row, final int column, final X value) {
        if (!checkBounds(row, column)) {
            throw new IllegalArgumentException(
                    "Input position was out of bounds (" + this.width + "," + this.height + ")");
        }
        if ((base == null && value != null) || (base != null && !base.equals(value))) {
            this.edits.put(new Position(row, column), value);
        } else {
            this.edits.remove(new Position(row, column));
        }
    }

    /**
     * Returns saved value of heigth.
     */
    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns saved value of width.
     */
    @Override
    public int getWidth() {
        return this.width;
    }

    /**
     * Applies the mapper to the base and every edit done, in order to replicate them.
     */
    @Override
    public <Y> Matrix<Y> map(final Function<? super X, ? extends Y> mapper) {
        final LazyMatrix<Y> result = new LazyMatrix<>(getWidth(), getHeight(),
                mapper.apply(this.base));
        this.edits.forEach((p, x) -> {
            result.set(p.getX(), p.getY(), mapper.apply(x));
        });
        return result;
    }

    /**
     * Iterates the matrix calling {@link LazyMatrix#get} to get each element.
     */
    @Override
    public Stream<X> stream() {
        return IntStream.range(0, this.height).mapToObj(
                row -> IntStream.range(0, this.width).mapToObj(col -> new Position(row, col)))
                .flatMap(s -> s).map(pos -> this.get(pos.getX(), pos.getY()));
    }

    /**
     * Eclipse generated hashCode base don fields.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + ((edits == null) ? 0 : edits.hashCode());
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }

    /**
     * Eclipse generated equals based on fields.
     */
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

    /**
     * Prints all the fields.
     */
    @Override
    public String toString() {
        return "LazyMatrix [edits=" + edits + ", base=" + base + ", width=" + width + ", height="
                + height + "]";
    }

    private boolean checkBounds(final int row, final int column) {
        return row >= 0 && row < this.height && column >= 0 && column < this.width;
    }

    private void transpose() {
        final Map<Position, X> old = new HashMap<>(this.edits);
        this.edits.clear();
        old.forEach((pos, x) -> {
            this.edits.put(new Position(pos.getY(), pos.getX()), old.get(pos));
        });
        final int temp = this.height;
        this.height = this.width;
        this.width = temp;
    }

    private void reverseEachRow() {
        final Map<Position, X> old = new HashMap<>(this.edits);
        this.edits.clear();
        old.forEach((pos, x) -> {
            this.edits.put(new Position(pos.getX(), this.width - pos.getY() - 1), old.get(pos));
        });
    }

    private class Position {
        private final int x;
        private final int y;

        Position(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof LazyMatrix.Position)) {
                return false;
            }
            final LazyMatrix.Position other = (LazyMatrix.Position) obj;
            if (x != other.x) {
                return false;
            }
            return y == other.y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
}
