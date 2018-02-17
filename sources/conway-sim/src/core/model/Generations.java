package core.model;

import core.utils.Matrix;

/**
 * Utility class for computation and editing of {@link Generation}.
 *
 */
public final class Generations {

    private  Generations() { }

    /**
     * Computes a new {@link Generation} from the given one.
     * @param start that is the previous {@link Generation}
     * @return the new computed {@link Generation}
     */
    public static Generation compute(final Generation start) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Computes n generations from start. 
     * @param start is the first {@link Generation}
     * @param number is the number of generation to be computed sequentially
     * @return the result of the computations
     */
    public static Generation compute(final Generation start, final int number) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * A method to modify a {@link Generation} by applying a certain alive cell pattern.
     * @param generation is the {@link Generation} to be modified
     * @param x is the row of the top left cell of the pattern
     * @param y is the column of the top left cell of the pattern
     * @param patternAliveCells is the alive cells {@link Matrix} of the pattern
     * @return the modified generation with the pattern applied in the given position
     */
    public static Generation mergePatternXY(final Generation generation, final int x, final int y, final Matrix<Boolean> patternAliveCells) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * A method to clone a {@link Generation}.
     * @param generation is the generation to clone
     * @return the cloned generation
     */
    public static Generation copyOf(final Generation generation) {
        // TODO Auto-generated method stub
        return null;
    }

}

