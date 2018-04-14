package controller.generation;

/**
 *
 */
public interface GenerationController extends GenerationComputing {

    /**
     * Create a new Game, ready to start.
     */
    void newGame();

    /**
     * Start/resume the game.
     * 
     * Start the clock.
     */
    void play();

    /**
     * Set in pause the current game.
     * 
     * Stop the clock.
     */
    void pause();

    /**
     * 
     * @param speed set a speed value
     *          this value should be 
     */
    void setSpeed(int speed);

    /**
     * Totally reset the controller.
     */
    void reset();

}
