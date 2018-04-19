package controller.editor;

import core.model.Status;
import core.utils.Matrix;

public interface ExtensionGridEditor extends PatternEditor {

	Matrix<Status> cutMatrix();

	void selectMode(boolean flag);

	boolean isCutReady();

	void cancelSelectMode();

}
