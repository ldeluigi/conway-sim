package controller.editor;


import core.model.Generation;
import core.model.Status;
import core.utils.Matrix;

/**
 * 
 */
public interface GridEditor {
	void draw(final Generation gen);
	
	void hit(int row, int column);
}
