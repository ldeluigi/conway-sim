package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.model.CellEnvironment;
import core.model.CellImpl;
import core.model.Environment;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Generations;
import core.model.StandardCellEnviroments;
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
        Environment env = new Environment() {

            @Override
            public int getWidth() {
                return BEFORE.getWidth();
            }

            @Override
            public int getHeight() {
                return BEFORE.getHeight();
            }

            @Override
            public CellEnvironment getCellEnvironment(int x, int y) {
                return StandardCellEnviroments.STANDARD;
            }
        };
        Generation start = GenerationFactory.from(BEFORE.map(b -> new CellImpl(b ? ALIVE : DEAD)), env);
        assertEquals(AFTER_STANDARD, Generations.compute(start).getAliveMatrix());
        System.out.println("First:\n" + start + "\nSecond:\n" + Generations.compute(start));
    }

    @Test
    void testSomeComputations() {
            Environment env = new Environment() {

                @Override
                public int getWidth() {
                    return BEFORE.getWidth();
                }

                @Override
                public int getHeight() {
                    return BEFORE.getHeight();
                }

                @Override
                public CellEnvironment getCellEnvironment(int x, int y) {
                    return StandardCellEnviroments.STANDARD;
                }
            };
            Generation start = GenerationFactory.from(BEFORE.map(b -> new CellImpl(b ? ALIVE : DEAD)), env);
            assertEquals(AFTER_THREE_STANDARD, Generations.compute(start, 3).getAliveMatrix());
            assertEquals(Generations.compute(start, 20).getAliveMatrix(), Generations.compute(start, 13).getAliveMatrix());
     }
}