/**
 * 
 */
package controller.generation;

import java.util.Map;

/**
 * pattern memento.
 * @param <X> type
 */
public interface GenerationMemento<X> {

    /**
     * 
     * @return a Map<Long, Generation>, where Long is the long that represent the number generation, 
     * and Generation is the corresponding generation 
     */
    Map<Long, X> getSavedState();

    /**
     * 
     * @param numberGeneration the parameter that will be added only if numberGeneration > of all the
     *          numberGeneration into the memento
     * @param generation the generation to be added
     */
    void addGeneration(Long numberGeneration, X generation);

    /**
     * 
     * @param numberGeneartion remove the generation with the specified numberGeneration
     */
    void removeGeneration(Long numberGeneartion);

    /**
     * 
     * @return the first generation that have numberGeneration 0L
     */
    X getFirst();

    /**
     * @param numberGeneration remove all the generation after this generation, not included
     */
    void removeAllGenerationAfter(Long numberGeneration);
}
