package controller.generation;

import core.model.Cell;
import core.model.CellImpl;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Generations;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.swing.sandbox.Sandbox;

/**
 * 
 */
public class GenerationControllerImpl implements GenerationController {

    private static final int MAX_TIME_GENERATION = 100000;

    private Long currentGenerationNumber = 0L;
    private Sandbox view;
    private Generation currentGeneration;
    private final AgentClock clock = new AgentClock();
    private final GenerationMemento<Generation> oldGeneration;

    /**
     * 
     */
    public GenerationControllerImpl() {
        Matrix<Cell> m = new ListMatrix<>(100, 100, () -> new CellImpl(Math.random() > 0.5 ? Status.ALIVE : Status.DEAD));
        this.currentGeneration = GenerationFactory.from(m, EnvironmentFactory.standardRules(100, 100));
        oldGeneration = new GenerationHistory(this.currentGeneration);
    }

    @Override
    public void startGameWithGeneration() {
        this.clock.start();
    }

    @Override
    public void pause() {
        this.stopClock();
        this.view.refreshView();
    }

    @Override
    public void resume() {
        this.restartClock();
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSleepTime(final Long timeSleep) {
        if (timeSleep > 0 && timeSleep < MAX_TIME_GENERATION) {
            this.clock.setStep(timeSleep);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void loadOldGeneration(final Long generationNumber) {
        if (generationNumber.equals(0L)) {
            this.currentGeneration = this.oldGeneration.getFirst();
            this.currentGenerationNumber = 0L;
            this.oldGeneration.removeAllGenerationAfter(0L);
            return;
        } else if (generationNumber.longValue() < 0L) {
            throw new IllegalArgumentException();
        }
        final Long value = this.oldGeneration.getSavedState().keySet().stream()
                        .filter(l -> l <= generationNumber)
                        .max((x, y) -> Long.compare(x, y))
                        .orElse(0L);
        Generation valueGeneration;
        Long difference;
        if (value.longValue() == 0L) {
            valueGeneration = this.oldGeneration.getFirst();
        } else {
            valueGeneration = this.oldGeneration.getSavedState().get(value);
        }
        difference = generationNumber - value;
        if (difference.longValue() != 0L) {
            valueGeneration = Generations.compute(difference.intValue(), valueGeneration);
        }
        this.currentGeneration = valueGeneration;
        this.currentGenerationNumber = generationNumber;
        this.oldGeneration.removeAllGenerationAfter(this.getCurrentNumberGeneration());
        this.view.refreshView();
    }

    @Override
    public Generation getCurrentGeneration() {
        return this.currentGeneration;
    }

    @Override
    public Long getCurrentNumberGeneration() {
        return this.currentGenerationNumber;
    }

    @Override
    public void computeNextGeneration() {
        this.currentGeneration = Generations.compute(this.currentGeneration);
        this.currentGenerationNumber++;
        if (this.getCurrentNumberGeneration().intValue() % 3 == 0) {
            this.oldGeneration.addGeneration(this.getCurrentNumberGeneration(), getCurrentGeneration());
        }
        this.view.refreshView();
    }
    
    @Override
    public void setView(final Sandbox viewPanel) {
        this.view = viewPanel;
    }
    
    private void stopClock() {
        clock.setClock(false);
    }
    
    private void restartClock() {
        clock.setClock(true);
    }

    class AgentClock extends Thread {

        private static final long INIT_STEP = 500L;
        private Long step = INIT_STEP;
        private boolean clock = true;

        public void run() {
            while (true) {
                while (clock) {
                    computeNextGeneration();
                    try {
                        sleep(step);
                    } catch (Exception e) {
                        System.err.println("Errore nella sleep!" + e.getMessage());
                    }
                }
                try {
                    sleep(step * 2);
                } catch (Exception e) {
                    System.err.println("Errore nella sleep!" + e.getMessage());
                }
            }
        }

        public void setClock(final boolean flag) {
            this.clock = flag;
        }

        public void setStep(final Long step) {
            this.step = step;
        }
    }

}
