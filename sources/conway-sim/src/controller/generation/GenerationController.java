/**
 * 
 */
package controller.generation;

import view.swing.sandbox.Sandbox;

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
     * @param viewPanel the panel that contain the output
     */
    void setView(Sandbox viewPanel);

    /**
     * Totally reset the controller.
     */
    void reset();

}
