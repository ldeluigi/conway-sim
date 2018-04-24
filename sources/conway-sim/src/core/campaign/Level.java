package core.campaign;

import core.model.Environment;
import core.model.Status;
import core.utils.Matrix;

/**
 * Represents a single level of the campaign. If the matrices returned have
 * different dimension the Level is in Illegal State.
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
     * This method is also suggested to be used for level overall dimensions,
     * because Environment is usually a Flyweight object.
     * 
     * @return The environment of the generations in this level
     */
    Environment getEnvironmentMatrix();

}
