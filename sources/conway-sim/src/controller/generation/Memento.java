/**
 * 
 */
package controller.generation;

import java.util.Map;

import core.model.Generation;

/**
 * Pattern memento. To store elements with key.
 * 
 * @param <Y>
 *            key type, every key have a specific elements.
 * @param <X>
 *            the type of the elements to store.
 */
interface Memento<Y extends Number, X> {

    /**
     * 
     * @return a Map<Long, X>, where Long is the long that represent the number of X, and X is the
     *         corresponding X
     */
    Map<Y, X> getSavedState();

    /**
     * 
     * @param keyElement
     *            the parameter that will be added only if numberGeneration > of all the
     *            numberGeneration into the memento
     * @param elem
     *            the generation to be added
     */
    void addElem(Y keyElement, X elem);

    /**
     * 
     * @param keyElemet
     *            remove the generation with the specified numberGeneration
     */
    void removeElem(Y keyElemet);

    /**
     * 
     * @return the first element that have numberOfElement 0L
     */
    X getFirst();

    /**
     * Remove all elements that have an higher keyNumber of this element, not included.
     * 
     * @param keyElement
     *            the last element key that must be keep, all the greatest will be remove
     */
    void removeAllElemsAfter(Y keyElement);

    /**
     * 
     * @param pivot
     *            the central element, this element doesn't be removed by removeAllElemsAfter
     */
    void setFirst(Generation pivot);

    /**
     * 
     * @return the numberOfElementsStored
     */
    int getNumberOfElementsStored();

    /**
     * 
     * @param numberOfElementsStored
     */
    void setNumberOfElementsStored(int numberOfElementsToStored);
}
