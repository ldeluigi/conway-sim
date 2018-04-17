package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;

/**
 * JUnit test for Matrix implementations.
 *
 */
public class TestMatrix {

    private static final Integer[][] INTMATRIX = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
    private static final Integer[][] ROTATEDMATRIX = { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 } };
    private static final Integer[][] INTMATRIXRET = { { 1, 2, 3, 10 }, { 4, 5, 6, 11 },
            { 7, 8, 9, 12 } };
    private static final Integer[][] ROTATEDMATRIXRET = { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 },
            { 12, 11, 10 } };
    private static final Integer[][] MODIFIEDMATRIX = { { 999, 2, 3 }, { 4, 5, 6 }, { 7, 8, 999 } };
    private static final String[][] MAPPEDRETMATRIX = { { "2", "3", "4", "11" },
            { "5", "6", "7", "12" }, { "8", "9", "10", "13" } };
    private static final Integer[][] CUTMATRIX = { { 2, 3 }, { 5, 6 } };

    @Test
    void testGet() {
        final Matrix<Integer> m = new ListMatrix<>(INTMATRIX);
        assertEquals(4, (int) m.get(1, 0));
        try {
            m.get(10, 0);
            fail("Should throw exception");
        } catch (Exception e) {
        }
        try {
            m.get(0, 10);
            fail("Should throw exception");
        } catch (Exception e) {
        }
        assertEquals(1, (int) m.get(0, 0));
    }

    @Test
    void testRotate() {
        final Matrix<Integer> m = new ListMatrix<>(INTMATRIX);
        final Matrix<Integer> m2 = new ListMatrix<>(INTMATRIX);
        m.rotateClockwise(4);
        assertEquals(new ListMatrix<>(INTMATRIX), m);
        m.rotateClockwise(1);
        assertEquals(new ListMatrix<>(ROTATEDMATRIX), m);
        m2.rotateClockwise(2);
        m.rotateClockwise(5);
        assertEquals(m2, m);
        m.rotateClockwise(1);
        m2.rotateClockwise(9);
        assertEquals(m2, m);
        final Matrix<Integer> m3 = new ListMatrix<>(INTMATRIXRET);
        final Matrix<Integer> m4 = new ListMatrix<>(ROTATEDMATRIXRET);
        m3.rotateClockwise(1);
        assertEquals(m3, m4);
    }

    @Test
    void testSet() {
        final Matrix<Integer> m = new ListMatrix<>(INTMATRIX);
        try {
            m.set(10, 0, 999);
            fail("Should throw exception");
        } catch (Exception e) {
        }
        try {
            m.set(0, 10, 999);
            fail("Should throw exception");
        } catch (Exception e) {
        }
        m.set(0, 0, 999);
        m.set(m.getHeight() - 1, m.getWidth() - 1, 999);
        assertEquals(new ListMatrix<>(MODIFIEDMATRIX), m);
    }

    @Test
    void testGetters() {
        assertEquals(3, new ListMatrix<>(INTMATRIX).getHeight());
        assertEquals(4, new ListMatrix<>(INTMATRIXRET).getWidth());
    }

    @Test
    void testMap() {
        assertEquals(new ListMatrix<>(MAPPEDRETMATRIX), new ListMatrix<>(INTMATRIXRET).map(x -> {
            x++;
            return x.toString();
        }));
    }

    @Test
    void testConstructors() {
        System.out.println(new ListMatrix<>(12, 6, () -> "hi"));
    }

    @Test
    void testEquals() {
        assertEquals(new ListMatrix<>(2, 2, () -> true),
                Matrices.unmodifiableMatrix(new ListMatrix<>(2, 2, () -> true)));
    }

    @Test
    void testForEach() {
        Matrix<List<Boolean>> m = new ListMatrix<>(10, 10, () -> new LinkedList<Boolean>());
        m.stream().forEach(x -> {
            x.add(true);
            x.add(true);
        });
        assertEquals(new ListMatrix<>(10, 10, () -> new LinkedList<>(Arrays.asList(true, true))),
                m);
    }

    @Test
    void testCut() {
        assertEquals(new ListMatrix<>(CUTMATRIX),
                Matrices.cut(new ListMatrix<>(INTMATRIX), 0, 1, 1, 2));
    }
}
