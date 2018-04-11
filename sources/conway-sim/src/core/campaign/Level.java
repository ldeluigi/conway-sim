package core.campaign;

import core.model.StandardCellEnvironments;
import core.model.Status;
import core.utils.Matrix;
/**
 * Represents a single level of the campaign.
 */
public interface Level {
	
	/**
	 * @return A matrix of {@link Editable} that indicates which cells should be
	 *         editable or not
	 */
	Matrix<Editable> getEditableMatrix();

	/**
	 * @return A matrix of {@link CellType} that indicates which type (that is which
	 *         implementation) of a cell should correspond to each position
	 */
	Matrix<CellType> getCellTypeMatrix();

	/**
	 * @return The initial status of each cell
	 */
	Matrix<Status> getInitialStateMatrix();

	/**
	 * @return The environment of the generations in this level
	 */
	Matrix<StandardCellEnvironments> getEnvironmentMatrix();
}
