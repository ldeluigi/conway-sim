package core.model;

import java.util.Objects;
import java.util.function.Function;

/**
 * This class applies the pattern Strategy to implement {@link CellEnvironment}.
 *
 */
public final class CellEnvironmentImpl implements CellEnvironment {

    private static final int MAX_NEIGHBORS = 8;
    private final Function<Integer, Boolean> checkBorn;
    private final Function<Integer, Boolean> checkDeath;

    CellEnvironmentImpl(final Function<Integer, Boolean> checkBorn, final Function<Integer, Boolean> checkDeath) {
        this.checkBorn = Objects.requireNonNull(checkBorn);
        this.checkDeath = Objects.requireNonNull(checkDeath);
    }

    @Override
    public boolean checkCellBorn(final int neighbors) {
        return apply(neighbors, this.checkBorn);
    }

    @Override
    public boolean checkCellDeath(final int neighbors) {
        return apply(neighbors, this.checkDeath);
    }

    private boolean apply(final int neighbors, final Function<Integer, Boolean> rule) {
        if (!(neighbors >= 0 && neighbors <= MAX_NEIGHBORS)) {
            throw new IllegalArgumentException("Neighbors must be positive and less or equal than " + MAX_NEIGHBORS);
        }
        return rule.apply(neighbors);
    }

}
