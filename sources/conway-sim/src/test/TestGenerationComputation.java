package test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.model.CellEnvironment;
import core.model.SimpleCell;
import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Generations;
import core.model.StandardCellEnvironments;
import core.utils.ListMatrix;
import core.utils.Matrix;
import static core.model.Status.*;

public class TestGenerationComputation {

    private static final Matrix<Boolean> BEFORE = new ListMatrix<>(new Boolean[][]{
        {true, false, false, true},
        {true, true, false, false},
        {false, false, true, false},
        {true, true, true, false}
});
    private static final Matrix<Boolean> AFTER_STANDARD = new ListMatrix<>(new Boolean[][]{
            {true, true, false, false},
            {true, true, true, false},
            {false, false, true, false},
            {false, true, true, false}
    });

    private static final Matrix<Boolean> AFTER_THREE_STANDARD = new ListMatrix<>(new Boolean[][]{
        {false, false, false, false},
        {true, false, true, true},
        {true, false, false, true},
        {false, true, true, false}
});
    @Test
    void testOneComputation() {
        Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_STANDARD, Generations.compute(start).getAliveMatrix());
        System.out.println("First:\n" + start.getAliveMatrix().map(b -> b ? "■" : "□") + "\nSecond:\n" + Generations.compute(start).getAliveMatrix().map(b -> b ? "■" : "□"));
    }

    @Test
    void testSomeComputations() {
            Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
            Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
            assertEquals(AFTER_THREE_STANDARD, Generations.compute(3, start).getAliveMatrix());
            assertEquals(Generations.compute(20, start).getAliveMatrix(), Generations.compute(13, start).getAliveMatrix());
     }
    
    @Test
    void testOneComputationMultithread() {
        Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_STANDARD, Generations.compute(start, 4).getAliveMatrix());
        System.out.println("First:\n" + start.getAliveMatrix().map(b -> b ? "■" : "□") + "\nSecond:\n" + Generations.compute(start, 4).getAliveMatrix().map(b -> b ? "■" : "□"));
    }

    @Test
    void testSomeComputationsMultithread() {
            Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
            Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
            assertEquals(AFTER_THREE_STANDARD, Generations.compute(3, start, 4).getAliveMatrix());
            assertEquals(Generations.compute(20, start, 4).getAliveMatrix(), Generations.compute(13, start, 4).getAliveMatrix());
     }

    @Test
    void testMultithreadPerformances() {
        Environment env = EnvironmentFactory.standardRules(1000, 1000);
        Generation start = GenerationFactory.from(new ListMatrix<>(1000, 1000, () -> new SimpleCell(Math.random() > 0.5 ? ALIVE : DEAD)), env);
        long startTime = System.currentTimeMillis();
        Generations.compute(100, start, 1);
        long first = System.currentTimeMillis() - startTime;
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
        Generations.compute(100, start, 5);
        second = System.currentTimeMillis() - startTime;
        System.out.println(second);
        startTime = System.currentTimeMillis();
        Generations.compute(100, start, 6);
        second = System.currentTimeMillis() - startTime;
        System.out.println(second);
        if (second > first) {
            fail();
        }
    }
    
    public static void main(String ...args) {
        Environment env = EnvironmentFactory.standardRules(BEFORE.getWidth(), BEFORE.getHeight());
        Generation start = GenerationFactory.from(BEFORE.map(b -> new SimpleCell(b ? ALIVE : DEAD)), env);
        Generations.compute(3, start);
        Generations.compute(Generations.compute(start));
    }
}
