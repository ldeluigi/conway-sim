package controller.editor;

import core.campaign.CellType;
import core.campaign.GameWinningCell;
import core.campaign.Level;
import core.campaign.NeverChangingCell;
import core.model.Cell;
import core.model.SimpleCell;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

/**
 * 
 */
public class LevelWinnigCondition {

    private final Level level;
    private final Runnable runnable;
    private final int cellThatHaveToChangeStatus;
    private int actualCount;
    private boolean win;

    /**
     * 
     * @param level the current level
     * @param winningRunnable the {@link Runnable} to call when winning condition
     */
    public LevelWinnigCondition(final Level level, final Runnable winningRunnable) {
        this.level = level;
        this.runnable = winningRunnable;
        this.cellThatHaveToChangeStatus = Long.valueOf(this.level.getCellTypeMatrix().stream().filter(cellType -> cellType.equals(CellType.GOLDEN)).count()).intValue();
    }

    /**
     * 
     * @param currentStatus the current status matrix
     * @return return a Matrix<Cell>
     */
    public Matrix<Cell> getCellMatrix(final Matrix<Status> currentStatus) {
        final Matrix<Cell> cellMatrix = new ListMatrix<>(this.level.getEnvironmentMatrix().getWidth(),
                this.level.getEnvironmentMatrix().getHeight(), () -> null);
        for (int row = 0; row < this.level.getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < this.level.getEnvironmentMatrix().getWidth(); col++) {
                cellMatrix.set(row, col, 
                        this.level.getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)
                        ? new SimpleCell(currentStatus.get(row, col))
                        : this.level.getCellTypeMatrix().get(row, col).equals(CellType.GOLDEN)
                        ? new GameWinningCell(currentStatus.get(row, col), () -> {
                        //born
                            cellBorn();
                        }, () -> {
                        //death
                            cellDie();
                        })
                        : this.level.getCellTypeMatrix().get(row, col).equals(CellType.WALL)
                        ? new NeverChangingCell(currentStatus.get(row, col))
                        : new SimpleCell(currentStatus.get(row, col)));
            }
        }
        return cellMatrix;
    }

    private synchronized void cellBorn() {
        actualCount++;
        control();
    }

    private synchronized void cellDie() {
        actualCount--;
        control();
    }

    private synchronized void control() {
        if (cellThatHaveToChangeStatus == Math.abs(actualCount) && !win) {
            runnable.run();
        }
    }

    class GoaldCell extends GameWinningCell {

        private int counter;
        private final Runnable born;
        private final Runnable death;

        public GoaldCell(Status state, Runnable born, Runnable death) {
            super(state, null, null);
            this.born = born;
            this.death = death;
        }

        @Override
        public void setStatus(final Status nextStatus) {
            if (this.getStatus().equals(nextStatus)) {
                counter++;
            } else {
                if (counter > 3) {
                    if (nextStatus.equals(Status.ALIVE)) {
                        born.run();
                    } else {
                        death.run();
                    }
                }
                super.setStatus(nextStatus);
                counter = 0;
            }
        }
    }
}
