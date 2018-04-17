package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import core.utils.LazyMatrix;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;

public class TestLazyMatrix {

    @Test
    void testGet() {
        final Matrix<Integer> m = new LazyMatrix<>(10, 10, 4);
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
        assertEquals(4, (int) m.get(0, 0));
    }

    @Test
    void testRotate() {
        final Matrix<Integer> m = new LazyMatrix<>(2, 2, 4);
        m.set(0, 1, 5);
        final Matrix<Integer> m2 = new LazyMatrix<>(2, 2, 4);
        m2.set(1, 1, 5);
        final Matrix<Integer> b = new LazyMatrix<>(2, 2, 4);
        b.set(0, 1, 5);
        m.rotateClockwise(4);
        assertEquals(b, m);
        m.rotateClockwise(1);
        assertEquals(m2, m);
        m2.rotateClockwise(2);
        m.rotateClockwise(6);
        assertEquals(m2, m);
        m.rotateClockwise(1);
        m2.rotateClockwise(9);
        assertEquals(m2, m);
    }

    @Test
    void testSet() {
        final Matrix<Integer> m = new LazyMatrix<>(10, 10, 4);
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
        assertEquals((Integer) 999, m.get(0, 0));
        assertEquals((Integer) 999, m.get(9, 9));
    }

    @Test
    void testGetters() {
        assertEquals(11, new LazyMatrix<>(12, 11, 4).getHeight());
        assertEquals(10, new LazyMatrix<>(10, 12, 4).getWidth());
    }

    @Test
    void testMap() {
        assertEquals(new LazyMatrix<>(11, 11, "9"), new LazyMatrix<>(11, 11, 8).map(x -> {
            x++;
            return x.toString();
        }));
    }

    @Test
    void testConstructors() {
        System.out.println(new LazyMatrix<>(12, 6, "hi"));
    }

    @Test
    void testEquals() {
        assertEquals(new LazyMatrix<>(12, 6, "hi"),
                Matrices.unmodifiableMatrix(new LazyMatrix<>(12, 6, "hi")));
    }

    @Test
    void testForEach() {
        Matrix<List<Boolean>> m = new LazyMatrix<>(2, 1, new LinkedList<Boolean>());
        m.stream().forEach(x -> {
            x.add(true);
        });
        assertEquals(new ListMatrix<>(2, 1, () -> new LinkedList<>(Arrays.asList(true, true))), m);
    }

    @Test
    void testCut() {
        assertEquals(new LazyMatrix<>(2, 2, 2),
                Matrices.cut(new LazyMatrix(10, 10, 2), 0, 1, 0, 1));
    }
}
