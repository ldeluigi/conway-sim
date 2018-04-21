package test;



import controller.io.RLETranslator;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

public class TranslatorTest {

    public static void main(String[] args) {
//       Matrix<Status> m = new ListMatrix<>(12, 12, () -> Status.DEAD);
//       String matString = RLETranslator.rleMatrixToString(m);
//       System.out.println(matString);


       Matrix<Status> matFromString = RLETranslator.rleStringToMatrix("56a2b\n@#[$2a56b!", Status.class);
       System.out.println(matFromString.toString());

    }
}
