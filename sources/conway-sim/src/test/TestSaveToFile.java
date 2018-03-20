package test;

import controller.io.GridSaver;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
/**
 * Test Class.
 *
 */
public class TestSaveToFile {
    static final int SIZE = 20;
    /**
     * Builder.
     */
    public TestSaveToFile() {
        //EMPTY
    }
    /**
     * Test Method.
     * @param dd
     */
    public static void main(String[] args) {
        Matrix<Status> matrix = new ListMatrix<>(SIZE, SIZE, () -> Status.DEAD);
        GridSaver gs = new GridSaver(matrix);
        System.out.println("Saved to file");
    }

}
