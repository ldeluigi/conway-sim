package controller.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import core.model.Generation;
import core.model.Generations;
import view.swing.sandbox.Sandbox;

/**
 * 
 */
public class GenerationControllerImpl implements GenerationController {

    private static final int MAX_SPEED = 10;
    private static final int MAX_SAVED_STATE = 20;
    private static final int SAVE_GAP = 100;

    private final Clock clock = new Clock(() -> this.computeNextGeneration(), MAX_SPEED);
    private final List<Long> savedState = new LinkedList<>();
    private Long currentGenerationNumber = 0L;
    private Sandbox view;
    private Generation currentGeneration;
    private Memento<Generation> oldGeneration;

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
        this.setCurrentNumberGeneration(0);
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public void pause() {
        this.clock.stopClock();
        this.view.refreshView();
    }

    @Override
    public void play() {
        this.clock.start();
    }

    @Override
    public void reset() {
        this.setCurrentNumberGeneration(0);
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public void setSpeed(final int speed) {
        this.clock.setSpeed(speed);
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    @Override
    public void loadGeneration(final Long generationNumber) {
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
        this.setCurrentGeneration(Generations.compute(this.getCurrentGeneration()));
        this.setCurrentNumberGeneration(this.getCurrentNumberGeneration() + 1L);
        this.saveGeneration(this.getCurrentGeneration(), getCurrentNumberGeneration());
        SwingUtilities.invokeLater(() -> this.view.refreshView());
    }

    private void saveGeneration(final Generation generationToSave, final Long generationNumber) {
        if (this.savedState.size() < MAX_SAVED_STATE) {
            this.savedState.add(generationNumber);
            this.oldGeneration.addElem(generationNumber, generationToSave);
        } else if (generationNumber % SAVE_GAP == 0) {
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
