package core.model;

import java.util.LinkedList;
import java.util.List;
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
        return compute(1, start);
    }

    /**
     * Computes a new {@link Generation} from the given one, with multithreading option.
     * @param start that is the previous {@link Generation}
     * @param threads to use
     * @return the new computed {@link Generation}
     */
    public static Generation compute(final Generation start, final int threads) {
        Objects.requireNonNull(start);
        if (threads < 1) {
            throw new IllegalArgumentException("Number of threads must be at least 1.");
        }
        final Environment env = start.getEnviroment();
        final Matrix<Cell> previous = start.getCellMatrix();
        final Matrix<Cell> result = GenerationFactory.copyOf(start).getCellMatrix();
        final int cells = previous.getWidth() * previous.getHeight();
        final int nThread = Math.min(cells, threads);
        final int delta = cells / nThread;
        final List<Thread> threadList = new LinkedList<>();
        IntStream.range(0, nThread - 1).forEach(n -> {
            threadList.add(new Thread(() -> {
                compute(n * delta, (n + 1) * delta, previous, result, env);
            }));
        });
        threadList.forEach(Thread::start);
        compute((nThread - 1) * delta, cells, previous, result, env);
        for (final Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                return compute(1, start);
            }
        }
        return GenerationFactory.from(result, env);
    }

    private static void compute(final int fromCell, final int toCell, final Matrix<Cell> previous,
            final Matrix<Cell> result, final Environment env) {
        IntStream.range(fromCell, toCell).forEach(nCell -> {
            // alive neighbors count
            final int row = nCell / previous.getWidth();
            final int column = nCell % previous.getWidth();
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
    }

    /**
     * Computes n generations from start. 
     * @param number is the number of generation to be computed sequentially
     * @param start is the first {@link Generation}
     * @return the result of the computations
     */
    public static Generation compute(final int number, final Generation start) {
        return compute(number, start, 1);
    }

    /**
     * Computes n generations from start, with multithreading option (4 threads).
     * @param number is the number of generation to be computed sequentially
     * @param start is the first {@link Generation}
     * @param threads toggle if the computation should use multithreading (4 threads)
     * @return the result of the computations
     */
    public static Generation compute(final int number, final Generation start, final int threads) {
        Objects.requireNonNull(start);
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        Generation temp = start;
        for (int i = 0; i < number; i++) {
            temp = Generations.compute(temp, threads);
            System.out.println(temp.getCellMatrix());
        }
        return temp;
    }
}
