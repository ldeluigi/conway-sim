/**
 * 
 */
package controller.generation;

import java.util.Map;

import core.model.Generation;

/**
 * pattern memento for type X.
 * @param <X> type
 */
interface Memento<X> {

    /**
     * 
     * @return a Map<Long, X>, where Long is the long that represent the number of X, 
     * and X is the corresponding X 
     */
    Map<Long, X> getSavedState();

    /**
     * 
     * @param numberGeneration the parameter that will be added only if numberGeneration > of all the
     *          numberGeneration into the memento
     * @param elem the generation to be added
     */
    void addElem(Long numberGeneration, X elem);

    /**
     * 
     * @param numberGeneartion remove the generation with the specified numberGeneration
     */
    void removeElem(Long numberGeneartion);

    /**
     * 
     * @return the first generation that have numberOfElement 0L
     */
    X getFirst();

    /**
     * @param numberGeneration remove all elements that have an higher numberGeneration this element, not included
     */
    void removeAllElemsAfter(Long numberGeneration);

    /**
     * 
     * @param newFirst
     */
    void setFirst(Generation newFirst);
}
