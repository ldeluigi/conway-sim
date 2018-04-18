package core.campaign;

import java.util.List;

import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Status;
import core.utils.LazyMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
/**
 * 
 *
 */
public class LevelImplTest implements Level {

    private final List<String> aviablePatterns;
    private final List<Matrix<? extends Enum<?>>> stages;
    /**
     * 
     * @param patterns aviable patterns
     * @param stages selected stages
     */
    public LevelImplTest(final List<String> patterns, final List<Matrix<? extends Enum<?>>> stages) {
        this.stages = stages;
        this.aviablePatterns = patterns;
    }

    @Override
    public Matrix<Editable> getEditableMatrix() {
        Matrix<Editable> m = new LazyMatrix<>(10, 10, Editable.UNEDITABLE);
        for (int i = 6; i < 10; i++) {
            for (int j = 6; j < 10; j++) {
                m.set(i, j, Editable.EDITABLE);
            }
        }
        return Matrices.unmodifiableMatrix(m);
    }

    @Override
    public Matrix<CellType> getCellTypeMatrix() {
        Matrix<CellType> m = new LazyMatrix<>(10, 10, CellType.NORMAL);
        m.set(0, 0, CellType.GOLDEN);
        m.set(0, 1, CellType.GOLDEN);
        m.set(1, 0, CellType.GOLDEN);
        m.set(1, 1, CellType.GOLDEN);
        return Matrices.unmodifiableMatrix(m);
    }

    @Override
    public Matrix<Status> getInitialStateMatrix() {
        Matrix<Status> m = new LazyMatrix<>(10, 10, Status.DEAD);
        m.set(0, 0, Status.ALIVE);
        m.set(1, 0, Status.ALIVE);
        m.set(0, 1, Status.ALIVE);
        m.set(1, 1, Status.ALIVE);
        return Matrices.unmodifiableMatrix(m);
    }

    @Override
    public Environment getEnvironmentMatrix() {
        return EnvironmentFactory.standardRules(10, 10);
    }

    @Override
    public final List<String> availablePatterns() {
        return this.aviablePatterns;
    }
    
    @Override
    public final List<Matrix<? extends Enum<?>>> getStages() {
        return stages;
    }

}
