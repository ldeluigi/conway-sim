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
     * Create a new Game, of the selected type.
     */
    void newGame();

    /**
     * 
     */
    void play();

    /**
     * 
     */
    void pause();

    /**
     * 
     * @param timeSleep is the time that pass between a generation and the next one
     */
    void setSpeed(int timeSleep);

    /**
     * 
     * @param generationNumber the number of the generation to load
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

    /**
     * Compute the next generation.
     */
    void computeNextGeneration();

    /**
     * @param viewPanel the panel that contain the output
     */
    void setView(Sandbox viewPanel);

    /**
     * Totally reset the controller.
     */
    void reset();

}
