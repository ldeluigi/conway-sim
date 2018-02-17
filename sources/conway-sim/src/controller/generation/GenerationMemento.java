/**
 * 
 */
package controller.generation;

import java.util.Map;
import core.model.Generation;

/**
 * pattern memento.
 */
public interface GenerationMemento {

    /**
     * 
     * @return a Map<Long, Generation>, where Long is the long that represent the number generation, 
     * and Generation is the corresponding generation 
     */
    Map<Long, Generation> getSavedState();

    /**
     * 
     * @param numberGeneration the parameter that will be added only if numberGeneration > of all the
     * 							numberGeneration into the memento
     * @param generation the generation to be added
     */
    void addGeneration(Long numberGeneration, Generation generation);

    /**
     * 
     * @param numberGeneartion remove the generation with the specified numberGeneration
     * @return return true if correctly remove the generation, false otherwise
     */
	boolean removeGeneration(Long numberGeneartion);

	/**
	 * 
	 * @return the first generation that have numberGeneration 0L
	 */
	Generation getFirst();
}
