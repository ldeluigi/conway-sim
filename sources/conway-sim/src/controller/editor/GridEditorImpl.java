package controller.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import core.model.Cell;
import core.model.CellImpl;
import core.model.Generation;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;

public class GridEditorImpl implements GridEditor, PatternEditor{

	private final GridPanel gameGrid;
	private Matrix<Status> pattern;

	public GridEditorImpl(final GridPanel grid) {
	    this.gameGrid = grid;
	}

    @Override
    public void draw(final Generation gen) {
        
    }

    @Override
    public void hit(final int row,final int column) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Generation getGeneration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void showPreview(final int row, final int column) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addPatternToPlace(final Matrix<Status> statusMatrix) {
        // TODO Auto-generated method stub
    }

    @Override
    public void placeCurrentPattern(final int row, final  int column) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isPlacing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void rotateCurrentPattern() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removePatternToPlace() {
        // TODO Auto-generated method stub
        
    }

}

class CellListener implements MouseListener{

    @Override
    public void mouseClicked(final MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}

