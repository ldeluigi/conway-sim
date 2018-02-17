package core.model;

import java.util.Objects;
import java.util.stream.IntStream;
import static core.model.Status.ALIVE;
import static core.model.Status.DEAD;
import core.utils.Matrix;

/**
 * Utility class for computation and editing of {@link Generation}.
 *
 */
public final class Generations {

    private  Generations() { }

    /**
     * Computes a new {@link Generation} from the given one.
     * @param start that is the previous {@link Generation}
     * @return the new computed {@link Generation}
     */
    public static Generation compute(final Generation start) {
        Objects.requireNonNull(start);
        final Environment env = start.getEnviroment();
        final Matrix<Cell> previous = start.getCellMatrix();
        final Matrix<Cell> result = Generations.copyOf(start).getCellMatrix();
        //Iteration of the cell matrix
        IntStream.range(0, previous.getHeight()).forEach(row -> {
            IntStream.range(0, previous.getWidth()).forEach(column -> {
                //Alive neighbors count
                int neighbors = 0;
                for (int h = -1; h <= 1; h++) {
                    for (int w = -1; w <= 1; w++) {
                        if (row + h >= 0 && row + h < previous.getHeight() && column + w >= 0 && column + w < previous.getWidth() && !(h == 0 && w == 0)) {
                            neighbors += previous.get(row + h, column + w).getStatus().equals(ALIVE) ? 1 : 0;
                        }
                    }
                }
                //Next Status evaluation
                if (previous.get(row, column).getStatus().equals(ALIVE) && env.getCellEnvironment(row, column).checkCellDeath(neighbors)) {
                    result.get(row, column).setStatus(DEAD);
                } else if (previous.get(row, column).getStatus().equals(DEAD) && env.getCellEnvironment(row, column).checkCellBorn(neighbors)) {
                    result.get(row, column).setStatus(ALIVE);
                }
            });
        });
        return Generations.from(result, env);
    }

    /**
     * Computes n generations from start. 
     * @param start is the first {@link Generation}
     * @param number is the number of generation to be computed sequentially
     * @return the result of the computations
     */
    public static Generation compute(final Generation start, final int number) {
        Objects.requireNonNull(start);
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        Generation temp = start;
        for (int i = 0; i < number; i++) {
            temp = Generations.compute(temp);
        }
        return temp;
    }

    /**
     * A method to modify a {@link Generation} by applying a certain alive cell pattern. Note that in order to do this it creates a new generation without modifying the given one.
     * @param generation is the {@link Generation} to be modified
     * @param x is the row of the top left cell of the pattern
     * @param y is the column of the top left cell of the pattern
     * @param patternAliveCells is the alive cells {@link Matrix} of the pattern
     * @return the modified generation with the pattern applied in the given position
     */
    public static Generation mergePatternXY(final Generation generation, final int x, final int y, final Matrix<Boolean> patternAliveCells) {
        Objects.requireNonNull(generation);
        Objects.requireNonNull(patternAliveCells);
        if (x < 0 || y < 0 || x + patternAliveCells.getHeight() > generation.getHeight() || y + patternAliveCells.getWidth() > generation.getWidth()) {
            throw new IllegalArgumentException("Invalid position or invalid pattern dimension.");
        }
        final Matrix<Cell> gen = Generations.copyOf(generation).getCellMatrix();
        final Matrix<Cell> toApply = patternAliveCells.map(b -> new CellImpl(b ? ALIVE : DEAD));
        IntStream.range(0, toApply.getHeight()).forEach(row -> {
            IntStream.range(0, toApply.getWidth()).forEach(column -> {
                gen.set(row + x, column + y, toApply.get(row, column));
            });
        });
        return Generations.from(gen, generation.getEnviroment());
    }

    /**
     * A method to clone a {@link Generation}. Note that cells are copied, whereas the environment is the exact same.
     * @param generation is the generation to clone
     * @return the cloned generation
     */
    public static Generation copyOf(final Generation generation) {
        return Generations.from(generation.getCellMatrix().map(c -> new CellImpl(c.getStatus())), generation.getEnviroment());
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
        };
    }

}

