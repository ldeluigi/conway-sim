/**
 * 
 */
package controller.generation;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import core.model.Generation;


/**
 *  The implementation of GenerationMemento.
 */
public class GenerationHistory implements Memento<Generation> {

    private static final int NUMBER_OF_GENERATION_STORED = 20;

    private Generation firstGeneration;
    private final Map<Long, Generation> historyGeneration;

    /**
     * 
     * @param firstGeneration the first generation that have to be saved. The starting status.
     */
    public GenerationHistory(final Generation firstGeneration) {
        this.firstGeneration = firstGeneration;
        this.historyGeneration = new HashMap<>();
    }

    @Override
    public final Map<Long, Generation> getSavedState() {
        return Collections.unmodifiableMap(this.historyGeneration);
    }

    @Override
    public final void setFirst(final Generation newFirst) {
        this.firstGeneration = newFirst;
    }

    @Override
    public final Generation getFirst() {
        return this.firstGeneration;
    }

    @Override
    public final void addElem(final Long numberGeneration, final Generation generation) {
        if (this.historyGeneration.keySet().size() >= NUMBER_OF_GENERATION_STORED) {
            this.historyGeneration.remove(this.historyGeneration.keySet().stream().min((x, y) -> Long.compare(x, y)).get());
        }
        if (!this.historyGeneration.keySet().stream().allMatch(e -> e < numberGeneration)) {
            throw new IllegalArgumentException();
        }
        this.historyGeneration.put(numberGeneration, generation);
    }

    @Override
    public final void removeElem(final Long numberGeneartion) {
        this.historyGeneration.remove(numberGeneartion);
    }

    @Override
    public final void removeAllElemsAfter(final Long numberGeneration) {
        Objects.requireNonNull(this.historyGeneration);
        final List<Long> longToRemove = new LinkedList<>();
        if (!this.historyGeneration.isEmpty()) {
            this.historyGeneration.keySet().stream().filter(l -> l > numberGeneration).forEach(l -> longToRemove.add(l));
        }
        longToRemove.forEach(l -> this.historyGeneration.remove(l));
    }
}
