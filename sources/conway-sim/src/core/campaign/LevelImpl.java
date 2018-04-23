package core.campaign;

import core.model.Environment;
import core.model.Status;
import core.utils.Matrix;

/**
 * 
 *
 */
public class LevelImpl implements Level {
    private final Matrix<Editable> edM;
    private final Matrix<CellType> ctM;
    private final Matrix<Status> stM;
    private final Environment env;

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
        this.edM = edM;
        this.ctM = ctM;
        this.stM = stM;
        this.env = env;
    }

    @Override
    public final Matrix<Editable> getEditableMatrix() {
        return this.edM;
    }

    @Override
    public final Matrix<CellType> getCellTypeMatrix() {
        return this.ctM;
    }

    @Override
    public final Matrix<Status> getInitialStateMatrix() {
        return this.stM;
    }

    @Override
    public final Environment getEnvironmentMatrix() {
        return this.env;
    }

}
