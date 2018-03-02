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
        return compute(start, false);
    }

    /**
     * Computes a new {@link Generation} from the given one, with multithreading option.
     * @param start that is the previous {@link Generation}
     * @param multithread toggle if the computation should use multithreading
     * @return  the new computed {@link Generation}
     */
    public static Generation compute(final Generation start, final boolean multithread) {
        Objects.requireNonNull(start);
        final Environment env = start.getEnviroment();
        final Matrix<Cell> previous = start.getCellMatrix();
        final Matrix<Cell> result = GenerationFactory.copyOf(start).getCellMatrix();
        //Multithread evaluation
        if (multithread) {
            final int halfRow = previous.getHeight() / 2;
            final int halfColumn = previous.getWidth() / 2;
            final Thread t1, t2, t3;
            t1 = new Thread(() -> {
                compute(0, halfRow, halfColumn, previous.getWidth(), previous, result, env);
            });
            t2 = new Thread(() -> {
                compute(halfRow, previous.getHeight(), 0, halfColumn, previous, result, env);
            });
            t3 = new Thread(() -> {
                compute(halfRow, previous.getHeight(), halfColumn, previous.getWidth(), previous, result, env);
            });
            t1.start();
            t2.start();
            t3.start();
            //Iteration of the cell matrix, or one fourth if multithreading
            compute(0, halfRow, 0, halfColumn, previous, result, env);
            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                return compute(start, false); //if for some reason multithreading fails, non-multithreading is a solution
            }
        } else {
            compute(0, previous.getHeight(), 0, previous.getWidth(), previous, result, env);
        }
        return GenerationFactory.from(result, env);
    }

    private static void compute(final int fromRow, final int toRowExclusive, final int fromColumn, final int toColumnExclusive,
            final Matrix<Cell> previous, final Matrix<Cell> result, final Environment env) {
        IntStream.range(fromRow, toRowExclusive).forEach(row -> {
            IntStream.range(fromColumn, toColumnExclusive).forEach(column -> {
                // alive neighbors count
                int neighbors = 0;
                for (int h = -1; h <= 1; h++) {
                    for (int w = -1; w <= 1; w++) {
                        if (row + h >= 0 && row + h < previous.getHeight() && column + w >= 0
                                && column + w < previous.getWidth() && !(h == 0 && w == 0)) {
                            neighbors += previous.get(row + h, column + w).getStatus().equals(ALIVE) ? 1 : 0;
                        }
                    }
                }
                // next Status evaluation
                if (previous.get(row, column).getStatus().equals(ALIVE)
                        && env.getCellEnvironment(row, column).checkCellDeath(neighbors)) {
                    result.get(row, column).setStatus(DEAD);
                } else if (previous.get(row, column).getStatus().equals(DEAD)
                        && env.getCellEnvironment(row, column).checkCellBorn(neighbors)) {
                    result.get(row, column).setStatus(ALIVE);
                }
            });
        });
    }

    /**
     * Computes n generations from start. 
     * @param start is the first {@link Generation}
     * @param number is the number of generation to be computed sequentially
     * @return the result of the computations
     */
    public static Generation compute(final Generation start, final int number) {
        return compute(start, false, number);
    }

    /**
     * Computes n generations from start, with multithreading option.
     * @param start is the first {@link Generation}
     * @param multithread toggle if the computation should use multithreading
     * @param number is the number of generation to be computed sequentially
     * @return the result of the computations
     */
    public static Generation compute(final Generation start, final boolean multithread, final int number) {
        Objects.requireNonNull(start);
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        Generation temp = start;
        for (int i = 0; i < number; i++) {
            temp = Generations.compute(temp, multithread);
        }
        return temp;
    }
}
