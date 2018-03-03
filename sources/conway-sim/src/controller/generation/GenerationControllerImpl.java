package controller.generation;

import core.model.Generation;
import view.swing.sandbox.GenerationPanel;

/**
 * 
 */
public class GenerationControllerImpl implements GenerationController {

    private Long currentGeneration = 0L;
    private final AgentClock clock = new AgentClock();
    private GenerationPanel panel;

    @Override
    public void startGameWithGeneration() {
        clock.start();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void end() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSleepTime(final Long timeSleep) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadOldGeneration(final Long generationNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public Generation getCurrentGeneration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getCurrentNumberGeneration() {
        return this.currentGeneration;
    }

    @Override
    public void computeNextGeneration() {
        this.currentGeneration++;
        this.panel.updateNumCurrentGeneration(this.getCurrentNumberGeneration());
    }

    class AgentClock extends Thread {

        private static final int INIT_STEP = 500;
        private int step = INIT_STEP;
        private boolean clock = true;

        public void run() {
            while (clock) {
                computeNextGeneration();
                try {
                    sleep(step);
                } catch (Exception e) {
                    System.err.println("Errore nella sleep!" + e.getMessage());
                }
            }
        }

        public void setClock(final boolean flag) {
            this.clock = flag;
        }

        public void setStep(final int step) {
            this.step = step;
        }
    }

    @Override
    public void setView(GenerationPanel viewPanel) {
        this.panel = viewPanel;
    }

}
