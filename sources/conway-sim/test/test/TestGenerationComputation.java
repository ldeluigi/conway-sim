package test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.model.SimpleCell;
import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Generations;
import core.utils.ListMatrix;
import core.utils.Matrix;
import static core.model.Status.ALIVE;
import static core.model.Status.DEAD;

/**
 * Test class for {@link Generations} computation methods.
 */
public class TestGenerationComputation {

    private static final int SIX = 6;
    private static final int FIVE = 5;
    private static final int GENERATION_AFTER_SAME = 13;
    private static final int GENERATIONS_AFTER = 20;
    private static final Matrix<Boolean> BEFORE = new ListMatrix<>(new Boolean[][] { { true, false, false, true },
            { true, true, false, false }, { false, false, true, false }, { true, true, true, false } });
    private static final Matrix<Boolean> AFTER_STANDARD = new ListMatrix<>(
            new Boolean[][] { { true, true, false, false }, { true, true, true, false }, { false, false, true, false },
                    { false, true, true, false } });

    private static final Matrix<Boolean> AFTER_THREE_STANDARD = new ListMatrix<>(
            new Boolean[][] { { false, false, false, false }, { true, false, true, true }, { true, false, false, true },
                    { false, true, true, false } });

    /**
     * Test for one single computation.
     */
    @Test
    public void testOneComputation() {
        final Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        final Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_STANDARD, Generations.compute(start).getCellMatrix(),
                "Computation produced unexpected cell matrix");
        System.out.println(
                "First:\n" + start.getCellMatrix().map(b -> b.getStatus().equals(ALIVE) ? "■" : "□") + "\nSecond:\n"
                        + Generations.compute(start).getCellMatrix().map(b -> b.getStatus().equals(ALIVE) ? "■" : "□"));
    }

    /**
     * Test for several computations.
     */
    @Test
    public void testSomeComputations() {
        final Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        final Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_THREE_STANDARD, Generations.compute(3, start).getCellMatrix(), "Wrong computation result");
        assertEquals(Generations.compute(GENERATIONS_AFTER, start).getCellMatrix(),
                Generations.compute(GENERATION_AFTER_SAME, start).getCellMatrix(), "Unexpected computation result");
    }

    /**
     * Test for multithreaded single computation.
     */
    @Test
    public void testOneComputationMultithread() {
        final Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        final Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_STANDARD, Generations.compute(start, 4).getCellMatrix(), "Unexpected computation result");
        System.out.println("First:\n" + start.getCellMatrix().map(b -> b.getStatus().equals(ALIVE) ? "■" : "□")
                + "\nSecond:\n"
                + Generations.compute(start, 4).getCellMatrix().map(b -> b.getStatus().equals(ALIVE) ? "■" : "□"));
    }

    /**
     * Test for multithreaded computations.
     */
    @Test
    public void testSomeComputationsMultithread() {
        final Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        final Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_THREE_STANDARD, Generations.compute(3, start, 4).getCellMatrix(),
                "Unexpected computation result");
        assertEquals(Generations.compute(GENERATIONS_AFTER, start, 4).getCellMatrix(),
                Generations.compute(GENERATION_AFTER_SAME, start, 4).getCellMatrix(), "Wrong computation result");
    }

    /**
     * OPTIONAL TEST. This test ensures that multithreading has better performances,
     * but may not work for all systems.
     */
    @Test
    public void testMultithreadPerformances() {
        final Environment env = EnvironmentFactory.standardRules(1000, 1000);
        final Generation start = GenerationFactory
                .from(new ListMatrix<>(1000, 1000, () -> new SimpleCell(Math.random() > 0.5 ? ALIVE : DEAD)), env);
        long startTime = System.currentTimeMillis();
        Generations.compute(100, start, 1);
        final long first = System.currentTimeMillis() - startTime;
        System.out.println(first);
        startTime = System.currentTimeMillis();
        Generations.compute(100, start, 2);
        long second = System.currentTimeMillis() - startTime;
        System.out.println(second);
        startTime = System.currentTimeMillis();
        Generations.compute(100, start, 4);
        second = System.currentTimeMillis() - startTime;
        System.out.println(second);
        startTime = System.currentTimeMillis();
        Generations.compute(100, start, FIVE);
        second = System.currentTimeMillis() - startTime;
        System.out.println(second);
        startTime = System.currentTimeMillis();
        Generations.compute(100, start, SIX);
        second = System.currentTimeMillis() - startTime;
        System.out.println(second);
        if (second > first) {
            fail("Second computation (2 thread) took more time than the first (1 thread)");
        }
    }
}
