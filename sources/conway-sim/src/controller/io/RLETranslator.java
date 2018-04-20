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
    private static final char EC = '!';

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

        final int matHeight = Math.toIntExact(rle.chars().filter(ch -> ch == EL).count()) + 1;
        // TODO DEBUG
        System.out.println("DEBUG | MAT HEIGHT: " + matHeight);
        int matWidth = matHeight;
        try (BufferedReader br = new BufferedReader(new StringReader(rle))) {
            int cont = 0;
            int check = br.read();
            while (check != EL) {
                if (Character.isDigit(check)) {
                    cont = cont + Character.getNumericValue(check) - 1;
                } else if (check == EC) {
                    break;
                } else {
                    cont++;
                }
                check = br.read();
            }
            matWidth = cont;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO DEBUG
        System.out.println("DEBUG | MAT WIDTH: " + matWidth);
        final Matrix<X> mat = new ListMatrix<X>(matWidth, matHeight, () -> null);
        try (BufferedReader br = new BufferedReader(new StringReader(rle))) {
            final String l = br.readLine();
            //TODO DEBUG
            System.out.println(l);
            if (l != null) {
                int k = 0;
                int i = 0;
                int check = 0;
                StringReader sr = new StringReader(l);
                do {
                    //TODO DEBUG
                    //System.out.println(l);
                    check = sr.read();
                    //TODO DEBUG
//                    if (Character.isDigit(check)) {
//                        System.out.println(Character.getNumericValue(check));
//                    } else {
//                        System.out.println(check);
//                    }
                    if (Character.isDigit(check)) {
                        int next = sr.read();
                        for (int j = 0; j < Character.getNumericValue(check); j++) {
                            mat.set(i, k, en.getEnumConstants()[next - SP]);
                            k++;
                        }
                        //System.out.println("DEBUG | Added " + Character.getNumericValue(check) + " VALUES");
                    } else if (check == EC) {
                        //IF !
                        break;
                    } else if (check == EL) {
                        //IF $
                        i++;
                        k = 0;
                    } else {
                        //System.out.println(mat.toString());
                        mat.set(i, k, en.getEnumConstants()[check - SP]);
                        k++;
                        check = sr.read();
                    }
                } while (check != EC);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mat;
    }

    // PER LEGGERE mat.set(i, k, en.getEnumConstants()[readValue]);

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
                        // TODO DEBUG
                        // System.out.println("DEBUG | CELLA " + k + i + " uguale a cella " + (k + 1) +
                        // i);
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
            if (i == matrix.getWidth() - 1) {
                mtoStr = mtoStr.concat(Character.toString(EC));
            } else {
                mtoStr = mtoStr.concat(Character.toString(EL));
            }

        }

        // TODO RemoveMe - DEBUG
        System.out.println("DEBUG | " + mtoStr);
        return mtoStr;
    }

}
