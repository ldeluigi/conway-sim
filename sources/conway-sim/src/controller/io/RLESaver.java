package controller.io;


import core.utils.Matrix;

/**
 * 
 * 
 *
 */
public class RLESaver {
    private static final String EL = "$";

    private String mat;
    /**
     * @param matrix The Matrix of Enum to be converted into String .conwaysrle type.
     */
    public RLESaver(final Matrix<? extends Enum<?>> matrix) {
        String mat = "";

        for (int i = 0; i < matrix.getHeight(); i++) {
            for (int k = 0; k < matrix.getWidth(); k++) {
                final Enum<?> st = matrix.get(k, i);
                    mat = mat.concat(Character.toString((char) ('a' + st.ordinal())));
            }
            //END OF LINE
            mat = mat.concat(EL);
        }
        this.mat = mat;
        System.out.println(mat);
    }
    /**
     * 
     * @return matrix string
     */
    public final String getMat() {
        return mat;
    }
    /**
     * 
     * @param mat to be setted 
     *
     */
    //TODO Method unused
    public final void setMat(final String mat) {
        this.mat = mat;
    }
}
