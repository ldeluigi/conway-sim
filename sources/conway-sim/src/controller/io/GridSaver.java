package controller.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import core.model.Status;
import core.utils.Matrix;
/**
 * 
 *
 */
public class GridSaver {
    private static final String PATH = "./src/test/saves";
    /**
     * 
     * @param matrix d
     */
    public GridSaver(final Matrix<Status> matrix) {
        String now = LocalDateTime.now().toString();
        now = now.replace(" ", "");
        now = now.replace(":", "");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(PATH + "/" + now + ".save"));
            writer.write(matStatusToString(matrix));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * This method takes a Matrix<Status> and converts it into a String of 0 and 1.
     * @param matrix to be converted
     * @return converted matrix
     */
    public final String matStatusToString(final Matrix<Status> matrix) {
        String matString = "";
        final Status deadCell = Status.DEAD;
        for (int i = 0; i < matrix.getHeight(); i++) {
            for (int k = 0; k < matrix.getWidth(); k++) {
                matString = matString + (matrix.get(k, i).equals(deadCell) ? "0" : "1");
            }
            matString = matString + "\n";
        }
        return matString;
    }

}
