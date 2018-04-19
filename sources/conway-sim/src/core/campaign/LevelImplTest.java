package core.campaign;

import java.util.List;

import core.model.Environment;
import core.model.Status;
import core.utils.Matrices;
import core.utils.Matrix;

/**
 * 
 *
 */
public class LevelImplTest implements Level {
    private final List<String> aviablePatterns;
    private final Matrix<Editable> edM;
    private final Matrix<CellType> ctM;
    private final Matrix<Status> stM;
    private final Environment env;

    /**
     * 
     * @param edM Matrix of Editable
     * @param ctM Matrix of CellType
     * @param stM Matrix of Status
     * @param env Environment
     * @param patterns AviablePatterns list
     */
    public LevelImplTest(final Matrix<Editable> edM, final Matrix<CellType> ctM, final Matrix<Status> stM,
            final Environment env, final List<String> patterns) {
        this.edM = edM;
        this.ctM = ctM;
        this.stM = stM;
        this.env = env;
        this.aviablePatterns = patterns;
    }

    @Override
    public final Matrix<Editable> getEditableMatrix() {
        return Matrices.unmodifiableMatrix(this.edM);
    }

    @Override
    public final Matrix<CellType> getCellTypeMatrix() {
        return Matrices.unmodifiableMatrix(this.ctM);
    }

    @Override
    public final Matrix<Status> getInitialStateMatrix() {
        return Matrices.unmodifiableMatrix(this.stM);
    }

    @Override
    public final Environment getEnvironmentMatrix() {
        return this.env;
    }

    @Override
    public final List<String> availablePatterns() {
        return this.aviablePatterns;
    }

}
