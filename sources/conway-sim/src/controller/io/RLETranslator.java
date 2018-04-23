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
     *            Abstract type that extends {@link Enum}
     * @param rle
     *            The RLE String to be converted into {@link Matrix}
     * @param en
     *            The Enumerative class
     * @return The converted {@link Matrix}
     */
    public static <X extends Enum<?>> Matrix<X> rleStringToMatrix(final String rle, final Class<X> en) {
        final String pRLE = patternize(rle.split("!")[0]);
        final int matHeight = Math.toIntExact(pRLE.chars().filter(ch -> ch == EL).count()) + 1;
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
        final Matrix<X> mat = new ListMatrix<X>(matWidth, matHeight, () -> null);
        for (int i = 0; i < matHeight; i++) {
            try (StringReader sr = new StringReader(
                    new BufferedReader(new StringReader(decode(pRLE.replace("\\!", "\\$").split("\\$")[i]))).lines()
                            .collect(Collectors.joining()))) {
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

            // END OF LINE
            if (i == matrix.getWidth() - 1) {
                mtoStr = mtoStr.concat(Character.toString(EC));
            } else {
                mtoStr = mtoStr.concat(Character.toString(EL));
            }

        }
        return mtoStr;
    }

    /**
     * This method decodes the given string from RLE format to exploded RLE format
     * (like "aaabb$babaa!").
     * 
     * @param str
     *            String to be decoded
     * @return decoded String
     */
    private static String decode(final String str) {
        final StringBuffer dest = new StringBuffer();
        final Pattern pattern = Pattern.compile("[0-9]+|[a-zA-Z]");
        final Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            matcher.find();
            while (number != 0) {
                dest.append(matcher.group());
                number--;
            }
        }
        return dest.toString();
    }

    /**
     * Static method to apply the pattern to normalize the RLE String.
     * 
     * @param str
     *            The String to be patternized
     * @return the patternized Strings
     */
    private static String patternize(final String str) {
        return str.replaceAll(RLEPATTERN, "");
    }

}
