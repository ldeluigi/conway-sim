package test;


import java.util.Random;

import controller.io.RLETranslator;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

public class TranslatorTest {

    public static void main(String[] args) {
       Matrix<Status> m = new ListMatrix<>(12, 6, () -> Status.DEAD);
       String matString = RLETranslator.rleMatrixToString(m);

       Matrix<Status> matFromString = RLETranslator.rleStringToMatrix("6a$6a$6a$6a$6a$6a$6a$6a$6a$6a$6a$6a$", Status.class);
       System.out.println(matFromString.toString());

        
    }
}
