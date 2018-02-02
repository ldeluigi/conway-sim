/**
 * 
 */
package controller.generation;

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
}
