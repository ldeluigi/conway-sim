package controller.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private static final String RLEPATTERN = "[^0-9a-z!$]";

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
        final String pRLE = patternize(rle.split("!")[0]);
        final int matHeight = Math.toIntExact(pRLE.chars().filter(ch -> ch == EL).count()) + 1;
        // TODO DEBUG
        System.out.println("DEBUG | MAT HEIGHT: " + matHeight);
        int matWidth = matHeight;
        try (BufferedReader br = new BufferedReader(new StringReader(decode(pRLE.split("\\$")[0]).concat("$")))) {

            int cont = 0;
            int check = br.read();
            while (check != EL) {
                if (check == EC) {
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
        for (int i = 0; i < matHeight; i++) {
            try (StringReader sr = new StringReader(new BufferedReader(new StringReader(decode(pRLE.split("\\$")[i])))
                    .lines().collect(Collectors.joining()))) {
                for (int k = 0; k < matWidth; k++) {
                    final int check = sr.read();
                    mat.set(i, k, en.getEnumConstants()[check - SP]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    //TODO JAVADOC
    private static String decode(final String str) {
        StringBuffer dest = new StringBuffer();
        Pattern pattern = Pattern.compile("[0-9]+|[a-zA-Z]");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            matcher.find();
            while (number-- != 0) {
                dest.append(matcher.group());
            }
        }
        return dest.toString();
    }

    //TODO JAVADOC
    private static String patternize(final String str) {
        return str.replaceAll(RLEPATTERN, "");
    }

}
