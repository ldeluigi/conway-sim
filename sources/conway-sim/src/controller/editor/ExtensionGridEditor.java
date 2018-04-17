package controller.editor;

import core.model.Status;
import core.utils.Matrix;

/**
 * 
 */
public interface ExtensionGridEditor extends GridEditor, PatternEditor {

    /**
     * 
     * @return the cut matrix
     */
    Matrix<Status> cutMatrix();

    /**
     * @param flag a flag
     */
    void selectMode(boolean flag);

    /**
     * 
     * @return return the cut condition
     */
    boolean isCutReady();

    /**
     * 
     */
    void cancelSelectMode();
}
