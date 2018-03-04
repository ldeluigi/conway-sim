/**
 * 
 */
package controller.generation;

import core.model.Generation;
import view.swing.sandbox.Sandbox;

/**
 *
 */
public interface GenerationController {

    /**
     * 
     */
    void startGameWithGeneration();

    /**
     * 
     */
    void pause();

    /**
     * 
     */
    void resume();

    /**
     * 
     */
    void end();

    /**
     * 
     * @param timeSleep is the time that pass between a generation and the next one
     */
    void setSleepTime(Long timeSleep);

    /**
     * 
     * @param generationNumber the number of the generation to load
     */
    void loadOldGeneration(Long generationNumber);

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

    /**
     * Compute the next generation.
     */
    void computeNextGeneration();

    /**
     * @param viewPanel the panel that contain the output
     */
    void setView(Sandbox viewPanel);

}
