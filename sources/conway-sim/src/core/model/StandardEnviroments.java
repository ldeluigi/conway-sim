package core.model;

import java.util.function.Function;

/**
 * Enumeration for common rule sets used in Conway's Game of Life.
 * 
 */
public enum StandardEnviroments implements CellEnvironment {
    /**
     * This is the classic rule set of Conway's Game of Life.
     */
    STANDARD(n -> n == 3, n -> n < 2 || n > 3);

    private final Function<Integer, Boolean> checkBorn;
    private final Function<Integer, Boolean> checkDeath;

    StandardEnviroments(final Function<Integer, Boolean> checkBorn, final Function<Integer, Boolean> checkDeath) {
        this.checkBorn = checkBorn;
        this.checkDeath = checkDeath;
    }
    @Override
    public boolean checkCellBorn(final int neighbors) {
        return this.checkBorn.apply(neighbors);
    }

    @Override
    public boolean checkCellDeath(final int neighbors) {
        return this.checkDeath.apply(neighbors);
    }
}
