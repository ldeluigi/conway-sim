package controller.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import core.model.Cell;
import core.model.SimpleCell;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Generations;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.Sandbox;

/**
 * 
 */
public class GenerationControllerImpl implements GenerationController {

    private static final int MAX_SPEED = 10;

    private Long currentGenerationNumber = 0L;
    private Sandbox view;
    private Generation currentGeneration;
    private Memento<Generation> oldGeneration;
    private final Clock clock = new Clock(() -> this.computeNextGeneration(), MAX_SPEED);
    private boolean firstStart = true;
    private final int saveGap = 100;

    private final List<Long> savedState = new LinkedList<>();
    private static final int MAX_SAVED_STATE = 20;

    private FutureTask<Boolean> fTask;


    /**
     * New Generation controller empty.
     * @param view the view
     */
    public GenerationControllerImpl(final Sandbox view) {
        this.view = view;
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
    }

    @Override
    public void newGame() {
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        this.clock.stopClock();
        if (firstStart) {
            this.firstStart = false;
            this.clock.start();
        }
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public void pause() {
        this.clock.stopClock();
        this.view.refreshView();
    }

    @Override
    public void play() {
        if (this.view.getGridEditor().isEnabled()) {
            this.view.getGridEditor().setEnabled(false);
            this.setCurrentGeneration(this.view.getGridEditor().getGeneration());
            this.oldGeneration.setFirst(this.getCurrentGeneration());
        }
        this.clock.restartClock();
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset() {
        this.loadGeneration(0L);
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public void setSpeed(final int speed) {
        this.clock.setSpeed(speed);
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public void loadGeneration(final Long generationNumber) {
        if (this.view.getGridEditor().isEnabled()) {
            this.view.getGridEditor().setEnabled(false);
            this.setCurrentGeneration(this.view.getGridEditor().getGeneration());
            this.oldGeneration.setFirst(this.getCurrentGeneration());
        }
        if (generationNumber.equals(0L)) {
            this.setCurrentGeneration(this.oldGeneration.getFirst());
            this.setCurrentNumberGeneration(0L);
            this.oldGeneration.removeAllElemsAfter(0L);
        } else if (generationNumber.longValue() < 0L) {
            throw new IllegalArgumentException();
        } else if (generationNumber > this.getCurrentNumberGeneration()) {
            final Long difference = generationNumber - this.getCurrentNumberGeneration();
            final int threadNumber = Runtime.getRuntime().availableProcessors();
            final Generation valueGeneration = Generations.compute(difference.intValue(), this.getCurrentGeneration(), threadNumber);
            this.setCurrentGeneration(valueGeneration);
            this.setCurrentNumberGeneration(generationNumber);
        } else {
            final Long value = this.oldGeneration.getSavedState().keySet().stream()
                            .filter(l -> l <= generationNumber)
                            .max((x, y) -> Long.compare(x, y))
                            .orElse(-1L);
            Generation valueGeneration;
            Long difference;
            if (value.equals(-1L)) {
                valueGeneration = this.oldGeneration.getFirst();
            } else {
                valueGeneration = this.oldGeneration.getSavedState().get(value);
            }
            difference = generationNumber - value;
            if (difference.longValue() != 0L) {
                    final int threadNumber = Runtime.getRuntime().availableProcessors();
                    valueGeneration = Generations.compute(difference.intValue(), valueGeneration, threadNumber);
            }
            this.setCurrentGeneration(valueGeneration);
            this.setCurrentNumberGeneration(generationNumber);
            this.oldGeneration.removeAllElemsAfter(this.getCurrentNumberGeneration());
        }
        this.savedState.removeAll(savedState.stream()
                                            .filter(l -> l > 1 && l > this.getCurrentNumberGeneration())
                                            .collect(Collectors.toList()));
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public Generation getCurrentGeneration() {
        return this.currentGeneration;
    }

    private synchronized void setCurrentGeneration(final Generation generation) {
        this.currentGeneration = generation;
    }

    @Override
    public synchronized Long getCurrentNumberGeneration() {
        return this.currentGenerationNumber;
    }

    private synchronized void setCurrentNumberGeneration(final long number) {
        this.currentGenerationNumber = number;
    }

    @Override
    public synchronized void computeNextGeneration() {
        if (Objects.isNull(fTask) || this.fTask.isDone()) {
            fTask = new FutureTask<>(() -> {
                this.setCurrentGeneration(Generations.compute(this.getCurrentGeneration()));
                this.setCurrentNumberGeneration(this.getCurrentNumberGeneration() + 1L);
                this.saveGeneration(this.getCurrentGeneration(), getCurrentNumberGeneration());
                SwingUtilities.invokeLater(() -> this.view.refreshView());
            }, true);
            fTask.run();
        } else {
            System.err.println("wait for end of task");
        }
    }

    private void saveGeneration(final Generation generationToSave, final Long generationNumber) {
        if (this.savedState.size() < MAX_SAVED_STATE) {
            this.savedState.add(generationNumber);
            this.oldGeneration.addElem(generationNumber, generationToSave);
        } else if (generationNumber % saveGap == 0) {
            if (this.savedState.size() >= MAX_SAVED_STATE) {
                this.savedState.remove(0);
            }
            this.savedState.add(generationNumber);
            this.oldGeneration.addElem(generationNumber, generationToSave);
        }
    }

    @Override
    public void setView(final Sandbox viewPanel) {
        this.view = viewPanel;
    }

}
