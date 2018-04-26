package controller.generation;

import core.model.Generation;

/**
 * This interface is the controller of Generation computing.
 */
public interface GenerationObserver extends Computing<Generation, Long> {

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
     * Set the speed value, max and min value is define in
     * ConstantBundle.properties.
     * 
     * @param speed
     *            set the speed value
     */
    void setSpeed(int speed);

    /**
     * Totally reset the controller, to the original state.
     */
    void reset();

    /**
     * Load an old element, removing all the newest element saved after oldNumber.
     * 
     * @param number
     *            the Long number of the element to load
     */
    void loadGeneration(Long number);

}
