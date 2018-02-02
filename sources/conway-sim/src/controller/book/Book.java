package controller.book;

import java.util.List;

/**
 * 
 * 
 * @param <E>
 *
 */
public interface Book<E> {
    /**
     * Returns the item from the given position.
     * 
     * @param placeholder
     */
    void dummyMethod();
    /**
     * 
     */
    void dummyMethod2();
    /**
     * Returns the list of patterns to be dragged on the main panel.
     * 
     * @return the item
     */
    List<E> getListPatterns();
    /**
     * 
     * @return the length of the list of patterns
     */
    int getListLength();
}
