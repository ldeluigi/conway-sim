package controller.generation;

/**
 * This interface represents a library of indexed generations.
 * 
 * @param <X>
 *            the element, that should have a corresponding number
 * @param <Y>
 *            the number of the element
 */
public interface Computing<X, Y extends Number> {

    /**
     * Compute a single element X and update corresponding number Y updating the
     * current element X and the current number Y.
     */
    void computeNext();

    /**
     * @return the current element X
     */
    X getCurrentElement();

    /**
     * @return the current number Y of the element X
     */
    Y getCurrentNumberElement();
}
