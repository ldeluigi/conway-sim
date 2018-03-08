package controller.editor;


import core.model.Generation;
import core.model.Status;
import core.utils.Matrix;

/**
 * 
 */
public interface GridEditor {

	void mouseEntered(int row, int column);

	void place(final Matrix<Status> statusMatrix);

	void mouseClicked(int row, int column);
	
	void draw(final Generation gen);
	
	boolean isPlacing();
}
