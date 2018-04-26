package core.campaign;

import core.model.Environment;
import core.model.Status;
import core.utils.Matrix;

/**
 * Implementation of {@link Level}.
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
     *            {@link Matrix}<{@link Editable}>
     * @param ctM
     *            {@link Matrix}<{@link CellType}>
     * @param stM
     *            {@link Matrix}<{@link Status}>
     * @param env
     *            {@link Environment}
     */
    public LevelImpl(final Matrix<Editable> edM, final Matrix<CellType> ctM, final Matrix<Status> stM,
            final Environment env) {
        this.editableMatrix = edM;
        this.cellTypeMatrix = ctM;
        this.statusMatrix = stM;
        this.environment = env;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Matrix<Editable> getEditableMatrix() {
        return this.editableMatrix;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final Matrix<CellType> getCellTypeMatrix() {
        return this.cellTypeMatrix;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final Matrix<Status> getInitialStateMatrix() {
        return this.statusMatrix;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public final Environment getEnvironmentMatrix() {
        return this.environment;
    }

}
