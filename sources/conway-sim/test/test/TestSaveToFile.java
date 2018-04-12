package test;

import controller.io.RLESaver;
import controller.io.RLETranslator;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
/**
 * Test Class.
 *
 */
public class TestSaveToFile {
    static RLESaver rs;
    /**
     * Builder.
     */
    /**
     * Test Method.
     * @param args dasda
     * @throws IllegalArgumentException ds
     */
    public static void main(final String[] args) throws IllegalArgumentException {
        final Matrix<? extends Enum<?>> matrix = new ListMatrix<Status>(6, 6, () -> Status.ALIVE);
        rs = new RLESaver(matrix);

        rs = new RLESaver(RLETranslator.rleStringToMatrix("bbbbbb$bbbaab$bbbbbb$bbabbb$bbbbbb$bbbaab$", null));
    }

}
