package controller.generation;

/**
 *
 */
public interface GenerationController extends GenerationComputing {

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
     * Totally reset the controller.
     */
    void reset();

}
