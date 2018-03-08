package controller.editor;


import core.model.Generation;
import core.model.Status;
import core.utils.Matrix;

/**
 * 
 */
public interface GridController {

	void mouseEntered(int row, int column);

	void place(final Matrix<Status> statusMatrix, final int startRow, final int startColumn);

	void mouseClicked(int row, int column);
	
	void draw(final Generation gen);
	
	boolean isPlacing();
}
