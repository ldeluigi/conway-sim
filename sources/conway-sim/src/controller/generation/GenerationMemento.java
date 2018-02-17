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
     * @param firstGeneration the first generation to save
     */
    void saveFirstState(Generation firstGeneration);

    /**
     * 
     * @return a Map<Long, Generation>, where Long is the long that represent the generation, 
     * and Generation is the corresponding generation 
     */
    Map<Long, Generation> getSavedState();

}
