package controller.editor;

import core.model.Status;
import core.utils.Matrix;

/**
 * 
 */
public interface ExtendedGridEditor extends SimpleGridEditor, PatternEditor {

    /**
     * 
     * @return the cut matrix
     */
    Matrix<Status> cutMatrix();

    /**
     * Sets selectMode with enabled/disabled. If selectMode is enabled, you can't
     * change the {@link Status} cell, but you can see what is ready for the cut.
     * 
     * @param flag
     *            if flag is true selectMode is set enabled
     */
    void setSelectMode(boolean flag);

    /**
     * 
     * @return true if the pattern is ready to be cut
     */
    boolean isCutReady();

    /**
     * Cancels the selectMode if it's enabled and resets the normal view.
     */
    void cancelSelectMode();
}
