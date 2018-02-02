/**
 * 
 */
package controller.general;

import core.model.Generation;
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
     * @return Matrix<Boolean> return a Matrix where alive cell is true and dead cell is false
     */
    Matrix<Boolean> getAliveMatrix();

    /**
     * @return Generation updates the current generation
     */
    Generation updateGeneration();

}
