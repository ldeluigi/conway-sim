package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import controller.io.RLETranslator;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

/**
 * JUnit test for RLE Translator.
 *
 */
public class TestRLETranslator {
    private static final Status[][] STATUSMATRIX = { { Status.ALIVE, Status.ALIVE, Status.DEAD },
            { Status.ALIVE, Status.DEAD, Status.ALIVE }, { Status.DEAD, Status.ALIVE, Status.ALIVE } };
    private static final Matrix<Status> RESULTMATRIX = new ListMatrix<Status>(STATUSMATRIX);
    private static final String RESULTSTRING = "2b1a$1b1a1b$1a2b!";

    /**
     * Test for conversion from Matrix<? extends Enum<?>> to String
     */
    @Test
    public void testMatrixTranslation() {
        final String resultTranslation = RLETranslator.rleMatrixToString(RESULTMATRIX);
        assertEquals(RESULTSTRING, resultTranslation, "Matrix to String Translation Test:");
    }
    /**
     * Test for conversion from String to Matrix<? extends Enum<?>>
     */
    @Test
    public void testStringTranslation() {
        final Matrix<Status> resultTranslation = RLETranslator.rleStringToMatrix(RESULTSTRING, Status.class);
        assertEquals(RESULTMATRIX, resultTranslation, "String to Matrix Translation Test:");
    }
}
