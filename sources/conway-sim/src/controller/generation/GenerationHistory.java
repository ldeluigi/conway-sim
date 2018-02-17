/**
 * 
 */
package controller.generation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import core.model.Generation;

/**
 *	The implementation of GenerationMemento
 */
public class GenerationHistory implements GenerationMemento {

	private final Generation firstGeneration;
	private final Map<Long, Generation> historyGeneration;

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
		/*
		 * TODO
		 * return Generations.copyOf(firstGeneration);
		 */
		return null;
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
		return false;
		
	}
}
