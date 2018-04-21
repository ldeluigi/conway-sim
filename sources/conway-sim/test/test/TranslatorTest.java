package test;



import controller.io.RLETranslator;
import core.model.Status;
import core.utils.Matrix;

public class TranslatorTest {

    public static void main(String[] args) {
       //Matrix<Status> m = new ListMatrix<>(12, 6, () -> Status.DEAD);
       //String matString = RLETranslator.rleMatrixToString(m);


       Matrix<Status> matFromString = RLETranslator.rleStringToMatrix("6a2b\n@#[$2a6b!", Status.class);
       System.out.println(matFromString.toString());

    }
}
