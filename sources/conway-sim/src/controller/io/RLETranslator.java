package controller.io;

import java.util.ArrayList;
import java.util.List;
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

    private RLETranslator() {
    }
    /**
     * @param <X> a
     * @param rle a
     * @param en a
     * @return a
     */
    public static <X extends Enum<?>> Matrix<X> rleStringToMatrix(final String rle, final Class<X> en) {
        final int matHeight = Math.toIntExact(rle.chars().filter(ch -> ch == EL).count());
        //TODO DEBUG
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
        //TODO DEBUG
        System.out.println("DEBUG | MAT WIDTH: " + matWidth);
        List<List<X>> listM = new ArrayList<List<X>>();
        List<X> listInt = new ArrayList<X>();
        for (int i = 0; i < matHeight; i++) {
            for (int k = 0; k < matWidth; k++) {
                listInt.add(en.getEnumConstants()[0]);
            }
            listM.add(listInt);
            listInt.clear();
        }
        final Matrix<X> mat = new ListMatrix<X>(listM);
        br = new BufferedReader(new StringReader(rle));
        for (int i = 0; i < matHeight; i++) {
            for (int k = 0; k < matWidth; k++) {
                try {
                        final int readValue = (int) (char) br.read() - 'a';
                        if (readValue >= en.getEnumConstants().length) {
                            //TODO RemoveMe - Check char bounds in status
                            throw new IllegalArgumentException("Status out of bounds");
                        }
                        mat.set(i, k, en.getEnumConstants()[readValue]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //TODO RemoveMe - END OF LINE
            try {
                br.read();
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

}
