package controller.editor;

import core.model.Status;
import core.utils.Matrix;

public interface PatternEditor extends GridEditor {

    void showPreview(int row, int column);

    void addPatternToPlace(final Matrix<Status> statusMatrix);

    void placeCurrentPattern(int row, int column);

    boolean isPlacing();
    
    void rotateCurrentPattern();
}
