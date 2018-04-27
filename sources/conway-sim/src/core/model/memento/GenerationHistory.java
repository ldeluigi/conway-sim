/**
 * 
 */
package core.model.memento;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import core.model.Generation;

/**
 * The implementation of GenerationMemento. With this class you can save a
 * numberGeneration connect with a Generation
 */
public final class GenerationHistory implements Memento<Long, Generation> {

    private static final int START_GENERATION_SAVED = 20;
    private int numberOfGenerationStored = START_GENERATION_SAVED;

    private Generation firstGeneration;
    private final Map<Long, Generation> historyGeneration;

    /**
     * Simple constructor that initializes empty collections.
     * 
     * @param firstGeneration
     *            the first generation that have to be saved. The starting status.
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
    public void setFirst(final Generation newFirst) {
        this.firstGeneration = newFirst;
    }

    @Override
    public Generation getFirst() {
        return this.firstGeneration;
    }

    @Override
    public void addElem(final Long numberGeneration, final Generation generation) {
        if (this.historyGeneration.keySet().size() >= numberOfGenerationStored) {
            this.historyGeneration
                    .remove(this.historyGeneration.keySet().stream().min((x, y) -> Long.compare(x, y)).get());
        }
        if (!this.historyGeneration.keySet().stream().allMatch(e -> e < numberGeneration)) {
            throw new IllegalArgumentException();
        }
        this.historyGeneration.put(numberGeneration, generation);
    }

    @Override
    public void removeElem(final Long numberGeneartion) {
        this.historyGeneration.remove(numberGeneartion);
    }

    @Override
    public void removeAllElemsAfter(final Long numberGeneration) {
        Objects.requireNonNull(this.historyGeneration);
        final List<Long> longToRemove = new LinkedList<>();
        if (!this.historyGeneration.isEmpty()) {
            this.historyGeneration.keySet().stream().filter(l -> l > numberGeneration)
                    .forEach(l -> longToRemove.add(l));
        }
        longToRemove.forEach(l -> this.historyGeneration.remove(l));
    }

    @Override
    public int getNumberOfElementsStored() {
        return numberOfGenerationStored;
    }

    @Override
    public void setNumberOfElementsStored(final int numberOfGenerationToStored) {
        this.numberOfGenerationStored = numberOfGenerationToStored;
    }
}
