package test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;

/**
 * JUnit test for Matrix implementations.
 *
 */
public class TestMatrix {

    private static final String EXCEPTION_CORRECTLY_THROWN = "Exception correctly thrown";
    private static final int SIX = 6;
    private static final int TWELVE = 12;
    private static final int CASUAL_NUMBER = 999;
    private static final int NINE = 9;
    private static final int FIVE = 5;
    private static final Integer[][] INTMATRIX = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
    private static final Integer[][] ROTATEDMATRIX = { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 } };
    private static final Integer[][] INTMATRIXRET = { { 1, 2, 3, 10 }, { 4, 5, 6, 11 }, { 7, 8, 9, 12 } };
    private static final Integer[][] ROTATEDMATRIXRET = { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 }, { 12, 11, 10 } };
    private static final Integer[][] MODIFIEDMATRIX = { { 999, 2, 3 }, { 4, 5, 6 }, { 7, 8, 999 } };
    private static final String[][] MAPPEDRETMATRIX = { { "2", "3", "4", "11" }, { "5", "6", "7", "12" },
            { "8", "9", "10", "13" } };
    private static final Integer[][] CUTMATRIX = { { 2, 3 }, { 5, 6 } };

    private static final Function<Integer[][], Matrix<Integer>> MATRIX_MAKER_INT = x -> new ListMatrix<>(x);
    private static final Function<String[][], Matrix<String>> MATRIX_MAKER_STRING = x -> new ListMatrix<>(x);

    private static <X> Matrix<X> filler(final int w, final int h, final Supplier<X> supplier) {
        return new ListMatrix<>(w, h, supplier);
    }

    /**
     * Test for get method.
     */
    @Test
    public void testGet() {
        final Matrix<Integer> m = MATRIX_MAKER_INT.apply(INTMATRIX);
        assertEquals(4, (int) m.get(1, 0), "Got wrong number in [1,0]");
        try {
            m.get(10, 0);
            fail("Should throw exception out of bounds");
        } catch (Exception e) {
            System.out.println(EXCEPTION_CORRECTLY_THROWN);
        }
        try {
            m.get(0, 10);
            fail("Should throw exception out of bounds");
        } catch (Exception e) {
            System.out.println(EXCEPTION_CORRECTLY_THROWN);
        }
        assertEquals(1, (int) m.get(0, 0), "Wrong number in [0,0]");
    }

    /**
     * Test for matrix rotation.
     */
    @Test
    public void testRotate() {
        final Matrix<Integer> m = MATRIX_MAKER_INT.apply(INTMATRIX);
        final Matrix<Integer> m2 = MATRIX_MAKER_INT.apply(INTMATRIX);
        m.rotateClockwise(4);
        assertEquals(MATRIX_MAKER_INT.apply(INTMATRIX), m, "4 rotations should leave the matrix unchanged");
        m.rotateClockwise(1);
        assertEquals(MATRIX_MAKER_INT.apply(ROTATEDMATRIX), m, "Matrix should rotate 90 degrees clockwise");
        m2.rotateClockwise(2);
        m.rotateClockwise(FIVE);
        assertEquals(m2, m, "Matrix should be rotate 180 degrees");
        m.rotateClockwise(1);
        m2.rotateClockwise(NINE);
        assertEquals(m2, m, "Another rotation of 90 degrees should be applied");
        final Matrix<Integer> m3 = MATRIX_MAKER_INT.apply(INTMATRIXRET);
        final Matrix<Integer> m4 = MATRIX_MAKER_INT.apply(ROTATEDMATRIXRET);
        m3.rotateClockwise(1);
        assertEquals(m3, m4, "Matrices should be equals");
    }

    /**
     * Test for set method.
     */
    @Test
    public void testSet() {
        final Matrix<Integer> m = MATRIX_MAKER_INT.apply(INTMATRIX);
        try {
            m.set(10, 0, CASUAL_NUMBER);
            fail("Should throw exception");
        } catch (Exception e) {
            System.out.println(EXCEPTION_CORRECTLY_THROWN);
        }
        try {
            m.set(0, 10, CASUAL_NUMBER);
            fail("Should throw exception");
        } catch (Exception e) {
            System.out.println(EXCEPTION_CORRECTLY_THROWN);
        }
        m.set(0, 0, CASUAL_NUMBER);
        m.set(m.getHeight() - 1, m.getWidth() - 1, CASUAL_NUMBER);
        assertEquals(MATRIX_MAKER_INT.apply(MODIFIEDMATRIX), m, "Setters didn't work");
    }

    /**
     * Test for height and width getters.
     */
    @Test
    public void testGetters() {
        assertEquals(3, MATRIX_MAKER_INT.apply(INTMATRIX).getHeight(), "Wrong height");
        assertEquals(4, MATRIX_MAKER_INT.apply(INTMATRIXRET).getWidth(), "Wrong width");
    }

    /**
     * Test for map method.
     */
    @Test
    public void testMap() {
        assertEquals(MATRIX_MAKER_STRING.apply(MAPPEDRETMATRIX), MATRIX_MAKER_INT.apply(INTMATRIXRET).map(x -> {
            x++;
            return x.toString();
        }), "Mapped returned unexpected result");
    }

    /**
     * Test filler constructor.
     */
    @Test
    public void testConstructors() {
        try {
            System.out.println(filler(TWELVE, SIX, () -> "hi"));
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * Test the equals.
     */
    @Test
    public void testEquals() {
        assertEquals(filler(2, 2, () -> true), Matrices.unmodifiableMatrix(filler(2, 2, () -> true)), "Equals had a wrong implementation");
    }

    /**
     * Test forEach method.
     */
    @Test
    public void testForEach() {
        final Matrix<List<Boolean>> m = filler(10, 10, () -> new LinkedList<Boolean>());
        m.stream().forEach(x -> {
            x.add(true);
            x.add(true);
        });
        assertEquals(filler(10, 10, () -> new LinkedList<>(Arrays.asList(true, true))), m, "ForEach didn't work");
    }

    /**
     * Test Matrices cut method.
     */
    @Test
    public void testCut() {
        assertEquals(MATRIX_MAKER_INT.apply(CUTMATRIX), Matrices.cut(MATRIX_MAKER_INT.apply(INTMATRIX), 0, 1, 1, 2), "cut method returned unexpected result");
    }
}
