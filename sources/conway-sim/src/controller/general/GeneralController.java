/**
 * 
 */
package controller.general;

import core.utils.Matrix;

/**
 *
 */
public interface GeneralController {

    /**
     * 
     */
    void start();

    /**
     * @return Matrix<Boolean> return a Matrix where alive cell is true and death cell is false
     */
    Matrix<Boolean> getAliveMatrix();

//    /**
//     * @return Generation update the current generation
//     */
//    Generation updateGeneration();

}
