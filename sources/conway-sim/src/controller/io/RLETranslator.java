package controller.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import core.utils.ListMatrix;
import core.utils.Matrix;

/**
 * 
 *
 */
public final class RLETranslator {
    private static final char EL = '$';
    private static final char SP = 'a';

    private RLETranslator() {
    }

    /**
     * @param <X>
     *            a
     * @param rle
     *            a
     * @param en
     *            a
     * @return a
     */
    public static <X extends Enum<?>> Matrix<X> rleStringToMatrix(final String rle, final Class<X> en) {
        
        final int matHeight = Math.toIntExact(rle.chars().filter(ch -> ch == EL).count());
        // TODO DEBUG
        System.out.println("DEBUG | MAT HEIGHT: " + matHeight);
        BufferedReader br = new BufferedReader(new StringReader(rle));
        int matWidth = matHeight;
        try {
            int cont = 0;
            while ((char) br.read() != EL) {
                cont++;
            }
            matWidth = cont;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO DEBUG
        System.out.println("DEBUG | MAT WIDTH: " + matWidth);
        final Matrix<X> mat = new ListMatrix<X>(matWidth, matHeight, () -> null);
        br = new BufferedReader(new StringReader(rle));
        for (int i = 0; i < matHeight; i++) {
            for (int k = 0; k < matWidth; k++) {
                try {
                    final int readValue = (int) (char) br.read() - SP;
                    if (readValue >= en.getEnumConstants().length) {
                        // TODO RemoveMe - Check char bounds in status
                        throw new IllegalArgumentException("Status out of bounds.");
                    }
                    mat.set(i, k, en.getEnumConstants()[readValue]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // TODO RemoveMe - END OF LINE
            try {
                if (br.read() != EL) {
                    throw new IllegalArgumentException("Reading out of bounds, maybe the rle got manipulated.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mat;
    }

    /**
     * 
     * @param matrix
     *            to be translated
     * @return string to be converted into String .conwaysrle type.
     */
    public static String rleMatrixToString(final Matrix<? extends Enum<?>> matrix) {
        String mtoStr = "";
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int k = 0; k < matrix.getHeight(); k++) {
                int count = 1;
                while (matrix.get(k, i).equals(matrix.get(k + 1, i))) {
                    if (matrix.get(k, i).equals(matrix.get(k + 1, i))) {
                        //TODO DEBUG
                        //System.out.println("DEBUG | CELLA " + k + i + " uguale a cella " + (k + 1) + i);
                        count++;
                        k++;
                    }
                    if (k == matrix.getHeight() - 1) {
                        break;
                    }
                }

                final Enum<?> en = matrix.get(k, i);
                if (count > 1) {
                    mtoStr = mtoStr.concat(Integer.toString(count));
                }
                mtoStr = mtoStr.concat(Character.toString((char) (SP + en.ordinal())));

            }

            // TODO RemoveMe - END OF LINE
            mtoStr = mtoStr.concat(Character.toString(EL));
        }
        // TODO RemoveMe - DEBUG
        System.out.println("DEBUG | " + mtoStr);
        return mtoStr;
    }

}
