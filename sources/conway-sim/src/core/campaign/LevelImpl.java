package core.campaign;

import core.model.Environment;
import core.model.Status;
import core.utils.Matrix;

/**
 * 
 *
 */
public class LevelImpl implements Level {
    private final Matrix<Editable> editableMatrix;
    private final Matrix<CellType> cellTypeMatrix;
    private final Matrix<Status> statusMatrix;
    private final Environment environment;

    /**
     * 
     * @param edM
     *            Matrix of Editable
     * @param ctM
     *            Matrix of CellType
     * @param stM
     *            Matrix of Status
     * @param env
     *            Environment
     */
    public LevelImpl(final Matrix<Editable> edM, final Matrix<CellType> ctM, final Matrix<Status> stM,
            final Environment env) {
        this.editableMatrix = edM;
        this.cellTypeMatrix = ctM;
        this.statusMatrix = stM;
        this.environment = env;
    }

    @Override
    public final Matrix<Editable> getEditableMatrix() {
        return this.editableMatrix;
    }

    @Override
    public final Matrix<CellType> getCellTypeMatrix() {
        return this.cellTypeMatrix;
    }

    @Override
    public final Matrix<Status> getInitialStateMatrix() {
        return this.statusMatrix;
    }

    @Override
    public final Environment getEnvironmentMatrix() {
        return this.environment;
    }

}
