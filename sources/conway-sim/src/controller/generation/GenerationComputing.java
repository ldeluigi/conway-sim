package controller.generation;

import core.model.Generation;

/**
 * 
 */
public interface GenerationComputing {

    /**
     * Compute the next generation.
     */
    void computeNextGeneration();

    /**
     * 
     * @param generationNumber
     *            the number of the generation to load
     */
    void loadGeneration(Long generationNumber);

    /**
     * 
     * @return the current generation
     */
    Generation getCurrentGeneration();

    /**
     * 
     * @return the current number of the generation
     */
    Long getCurrentNumberGeneration();
}
