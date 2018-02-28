/**
 * 
 */
package controller.generation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import core.model.Generation;
import core.model.GenerationFactory;

/**
 *  The implementation of GenerationMemento.
 */
public class GenerationHistory implements GenerationMemento {

    private final Generation firstGeneration;
    private final Map<Long, Generation> historyGeneration;

    /**
     * 
     * @param firstGeneration is the first generation
     */
    public GenerationHistory(final Generation firstGeneration) {
        this.firstGeneration = firstGeneration;
        this.historyGeneration = new HashMap<>();
    }

    @Override
    public Map<Long, Generation> getSavedState() {
        return Collections.unmodifiableMap(this.historyGeneration);
    }

    @Override
    public Generation getFirst() {
        return GenerationFactory.copyOf(firstGeneration);
    }

    @Override
    public void addGeneration(final Long numberGeneration, final Generation generation) {
        if (!this.historyGeneration.keySet().stream().allMatch(e -> e < numberGeneration)) {
            throw new IllegalArgumentException();
        }
        this.historyGeneration.put(numberGeneration, generation);
    }

    @Override
    public boolean removeGeneration(final Long numberGeneartion) {
        /*
         * TODO
         */
        return false;
    }
}
