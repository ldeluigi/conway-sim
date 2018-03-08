package controller.editor;

import core.model.Cell;
import core.model.CellImpl;
import core.model.Generation;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

public class GridControllerImpl implements GridController {

	private final Matrix<Cell> current;

	public GridControllerImpl(final int width, final int height) {
		this.current = new ListMatrix<>(width, height, () -> new CellImpl(Status.DEAD));
	}
	
	public Matrix<Boolean> getAliveMatrix() {
		return this.current.map(s -> s.getStatus().equals(Status.ALIVE) ? true : false);
	}
	
	public Generation getGeneration() {
		return null;
	}
	
	public void setAlive(int x, int y, boolean alive) {
		
	}

	@Override
	public void mouseEntered(int row, int column) {
		// TODO Auto-generated method stub
		//TODO usare merge di Matrices per modificare una piccola a partire da una grande
		
	}

	@Override
	public void place(Matrix<Status> statusMatrix, int startRow, int startColumn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(int row, int column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Generation gen) {
		// TODO Auto-generated method stub
		
	}
}
