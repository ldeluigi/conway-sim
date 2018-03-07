package controller.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
    private GenerationMemento<Generation> oldGeneration;
    private final AgentClock clock = new AgentClock();
    private boolean firstStart = true;

    private final List<Long> savedState = new LinkedList<>();
    private static final int MAX_SAVED_STATE = 6;

    /**
     * Init a new Generation Controller.
     */
    public GenerationControllerImpl() {
        final Matrix<Cell> m = new ListMatrix<>(100, 100, () -> new CellImpl(Status.DEAD));
        this.currentGeneration = GenerationFactory.from(m, EnvironmentFactory.standardRules(100, 100));
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
    }

    @Override
    public void newGame() {
        final Matrix<Cell> m = new ListMatrix<>(100, 100, () -> new CellImpl(Math.random() > 0.5 ? Status.ALIVE : Status.DEAD));
        this.currentGeneration = GenerationFactory.from(m, EnvironmentFactory.standardRules(100, 100));
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        this.stopClock();
        if (firstStart) {
            this.firstStart = false;
            this.clock.start();
        }
        this.view.refreshView();
    }

    @Override
    public void pause() {
        this.stopClock();
        this.view.refreshView();
    }

    @Override
    public void play() {
        this.restartClock();
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset() {
        this.currentGeneration = GenerationFactory.from(new ListMatrix<>(100, 100, () -> new CellImpl(Status.DEAD)), EnvironmentFactory.standardRules(100, 100));
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        this.currentGenerationNumber = 0L;
        this.view.refreshView();
    }

    @Override
    public void setSpeed(final int speed) {
        final Long sleepTime = 1000 / Long.valueOf(speed);
        if (sleepTime > 0 && sleepTime < MAX_TIME_GENERATION) {
            this.clock.setStep(Long.valueOf(sleepTime));
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
        } else if (generationNumber.longValue() < 0L) {
            throw new IllegalArgumentException();
        } else {
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

                    final int threadNumber = (difference.intValue() / 4000 > 4 ? 4 : difference.intValue() / 2000) + 1;
                    System.err.println(threadNumber + " thread");
                    valueGeneration = Generations.compute(difference.intValue(), valueGeneration, threadNumber);
            }
            this.currentGeneration = valueGeneration;
            this.currentGenerationNumber = generationNumber;
            this.oldGeneration.removeAllGenerationAfter(this.getCurrentNumberGeneration());
        }
        this.savedState.removeAll(savedState.stream()
                                            .filter(l -> l > 1 && l > this.getCurrentNumberGeneration())
                                            .collect(Collectors.toList()));
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

    private Future<Generation> computeFuture(final Generation generation, final int numberGen) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            return Generations.compute(numberGen, generation);
        });
    }

    @Override
    public void computeNextGeneration() {
        final Future<Generation> futurGeneration = computeFuture(this.getCurrentGeneration(), 1);
        try {
            this.currentGeneration = futurGeneration.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        this.currentGenerationNumber++;
        this.saveGeneration(this.getCurrentGeneration(), getCurrentNumberGeneration());
        this.view.refreshView();
    }

    private void saveGeneration(final Generation generationToSave, final Long generationNumber) {
        if (this.savedState.size() < MAX_SAVED_STATE) {
            this.savedState.add(generationNumber);
            this.oldGeneration.addGeneration(generationNumber, generationToSave);
        } else if (this.savedState.stream().reduce((x, y) -> x + y).get() + 1 == generationNumber) {
            if (this.savedState.size() >= MAX_SAVED_STATE) {
                this.savedState.remove(0);
            }
            this.savedState.add(generationNumber);
            this.oldGeneration.addGeneration(generationNumber, generationToSave);
        }
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

        private static final long INIT_STEP = 900L;
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
