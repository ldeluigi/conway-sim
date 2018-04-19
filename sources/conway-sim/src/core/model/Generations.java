package core.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.IntStream;

import static core.model.Status.ALIVE;
import static core.model.Status.DEAD;
import core.utils.Matrix;

/**
 * Utility class for computation of {@link Generation}.
 */
public final class Generations {

    private Generations() {
    }

    /**
     * Computes a new {@link Generation} from the given one.
     * 
     * @param start
     *            that is the previous {@link Generation}
     * @return the new computed {@link Generation}
     */
    public static Generation compute(final Generation start) {
        return compute(1, start);
    }

    /**
     * Computes number generations from start.
     * 
     * @param number
     *            is the number of generations to be computed sequentially
     * @param start
     *            is the first {@link Generation}
     * @return the result of the computations
     */
    public static Generation compute(final int number, final Generation start) {
        return compute(number, start, 1);
    }

    /**
     * Computes a new {@link Generation} from the given one, with multithreading option.
     * 
     * @param start
     *            that is the previous {@link Generation}
     * @param threads
     *            to use (the caller will wait for the others to end)
     * @return the new computed {@link Generation}
     */
    public static Generation compute(final Generation start, final int threads) {
        return compute(1, start, threads);
    }

    /**
     * Computes number generations from start, with multithreading option.
     * 
     * @param number
     *            is the number of generations to be computed sequentially
     * @param start
     *            is the first {@link Generation}
     * @param threads
     *            to use (the caller will wait for the others to end)
     * @return the result of the computations
     */
    public static Generation compute(final int number, final Generation start, final int threads) {
        Objects.requireNonNull(start);
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        final int cells = start.getWidth() * start.getHeight();
        final int nThread = Math.min(cells, threads);
        final ExecutorService executor = Executors.newFixedThreadPool(nThread);
        Generation temp = start;
        for (int i = 0; i < number; i++) {
            temp = computeGeneration(temp, executor, nThread);
        }
        executor.shutdown();
        return temp;
    }

    private static Generation computeGeneration(final Generation start,
            final ExecutorService executor, final int nThread) {
        final Environment env = start.getEnviroment();
        final Matrix<Cell> previous = start.getCellMatrix();
        final Matrix<Cell> result = GenerationFactory.copyOf(start).getCellMatrix();
        final int cells = start.getWidth() * start.getHeight();
        final int delta = cells / nThread;
        final List<FutureTask<?>> threadList = new LinkedList<>();
        IntStream.range(0, nThread).forEach(n -> {
            final FutureTask<Boolean> fTask = new FutureTask<>(() -> {
                computeSlice(n * delta, (n + 1) * delta, previous, result, env);
            }, null);
            threadList.add(fTask);
            executor.execute(fTask);
        });
        for (final FutureTask<?> tTask : threadList) {
            try {
                tTask.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException("Generation computation was interrupted. Aborted.");
            }
        }
        return GenerationFactory.from(result, env);
    }

    private static void computeSlice(final int fromCell, final int toCell,
            final Matrix<Cell> previous, final Matrix<Cell> result, final Environment env) {
        for (int nCell = fromCell; nCell < toCell; nCell++) {
            // alive neighbors count
            final int row = nCell / previous.getWidth();
            final int column = nCell % previous.getWidth();
            int neighbors = 0;
            for (int h = -1; h <= 1; h++) {
                for (int w = -1; w <= 1; w++) {
                    if (row + h >= 0 && row + h < previous.getHeight() && column + w >= 0
                            && column + w < previous.getWidth() && !(h == 0 && w == 0)) {
                        neighbors += previous.get(row + h, column + w).getStatus().equals(ALIVE) ? 1
                                : 0;
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
        }
    }
}
