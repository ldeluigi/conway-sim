package core.campaign;

import java.util.Random;

import core.model.CellEnvironment;
import core.model.Environment;
import core.model.EnvironmentFactory;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

public class LevelImplTest implements Level {

    private Environment env = EnvironmentFactory.standardRules(10, 10);
    private final Matrix<Editable> mEditable = new ListMatrix<>(10, 10, () -> {
        return Editable.EDITABLE;
    });
    private final Matrix<CellType> mCellType = new ListMatrix<>(10, 10, () -> {
       return CellType.NORMAL; 
    });
    private final Matrix<Status> mStatus = new ListMatrix<>(10, 10, () -> {
       return Math.random() <= 0.5 ? Status.ALIVE : Status.DEAD; 
    });

    @Override
    public Matrix<Editable> getEditableMatrix() {
        return this.mEditable;
    }

    @Override
    public Matrix<CellType> getCellTypeMatrix() {
        return this.mCellType;
    }

    @Override
    public Matrix<Status> getInitialStateMatrix() {
        return this.mStatus;
    }

    @Override
    public Environment getEnvironmentMatrix() {
        return EnvironmentFactory.standardRules(10, 10);
    }

}
