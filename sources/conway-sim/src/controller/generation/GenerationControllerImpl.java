package controller.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import controller.io.ResourceLoader;
import core.model.Generation;
import core.model.Generations;
import core.model.memento.GenerationHistory;
import core.model.memento.Memento;
import view.Sandbox;

/**
 * Implementation of {@link GenerationController}.
 */
public class GenerationControllerImpl implements GenerationController {
    private static final int SAVE_GAP = ResourceLoader.loadConstantInt("generation.SAVE_GAP");

    private final Clock clock = new Clock(() -> this.computeNext());
    private final List<Long> savedState = new LinkedList<>();
    private final Sandbox view;
    private Long currentGenerationNumber;
    private Generation currentGeneration;
    private Memento<Long, Generation> oldGeneration;

    /**
     * New Generation controller empty.
     * 
     * @param view
     *            the view
     */
    public GenerationControllerImpl(final Sandbox view) {
        this.view = view;
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.currentGenerationNumber = 0L;
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newGame() {
        this.setCurrentNumberGeneration(0);
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        this.view.scheduleGUIUpdate(() -> this.view.refreshView());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        this.clock.stopClock();
        this.view.refreshView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play() {
        this.clock.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.setCurrentNumberGeneration(0);
        this.currentGeneration = this.view.getGridEditor().getGeneration();
        this.oldGeneration = new GenerationHistory(this.currentGeneration);
        this.view.scheduleGUIUpdate(() -> this.view.refreshView());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpeed(final int speed) {
        this.clock.setSpeed(speed);
        this.view.scheduleGUIUpdate(() -> this.view.refreshView());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadGeneration(final Long generationNumber) {
        if (generationNumber.equals(0L)) {
            this.setCurrentGeneration(this.oldGeneration.getFirst());
            this.setCurrentNumberGeneration(0L);
            this.oldGeneration.removeAllElemsAfter(0L);
        } else if (generationNumber.longValue() < 0L) {
            throw new IllegalArgumentException();
        } else if (generationNumber > this.getCurrentNumberElement()) {
            Long difference = generationNumber - this.getCurrentNumberElement();
            Generation toBeComputed = this.getCurrentElement();
            if (difference > 1000) {
                final Long gap = difference / this.oldGeneration.getNumberOfElementsStored();
                while (difference > gap) {
                    if (this.savedState.size() >= this.oldGeneration.getNumberOfElementsStored()) {
                        this.savedState.remove(0);
                    }
                    final int threadNumber = Runtime.getRuntime().availableProcessors();
                    toBeComputed = Generations.compute(gap.intValue(), toBeComputed, threadNumber);
                    this.setCurrentNumberGeneration(this.getCurrentNumberElement() + gap);
                    this.savedState.add(this.getCurrentNumberElement());
                    this.oldGeneration.addElem(this.getCurrentNumberElement(), toBeComputed);
                    difference = difference - gap;
                }
            }
            final int threadNumber = Runtime.getRuntime().availableProcessors();
            final Generation valueGeneration = Generations.compute(difference.intValue(), toBeComputed, threadNumber);
            this.setCurrentGeneration(valueGeneration);
            this.setCurrentNumberGeneration(generationNumber);
        } else {
            final Long value = this.oldGeneration.getSavedState().keySet().stream().filter(l -> l <= generationNumber)
                    .max((x, y) -> Long.compare(x, y)).orElse(-1L);
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
            this.oldGeneration.removeAllElemsAfter(this.getCurrentNumberElement());
        }
        this.savedState.removeAll(savedState.stream().filter(l -> l > 1 && l > this.getCurrentNumberElement())
                .collect(Collectors.toList()));
        this.view.scheduleGUIUpdate(() -> this.view.refreshView());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Generation getCurrentElement() {
        return this.currentGeneration;
    }

    private synchronized void setCurrentGeneration(final Generation generation) {
        this.currentGeneration = generation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Long getCurrentNumberElement() {
        return this.currentGenerationNumber;
    }

    private synchronized void setCurrentNumberGeneration(final long number) {
        this.currentGenerationNumber = number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void computeNext() {
        this.getCurrentElement();
        this.setCurrentGeneration(Generations.compute(this.getCurrentElement()));
        this.getCurrentElement();
        this.setCurrentNumberGeneration(this.getCurrentNumberElement() + 1L);
        this.saveGeneration(this.getCurrentElement(), getCurrentNumberElement());
        this.view.refreshView();
    }

    private void saveGeneration(final Generation generationToSave, final Long generationNumber) {
        if (this.savedState.size() < this.oldGeneration.getNumberOfElementsStored()) {
            this.savedState.add(generationNumber);
            this.oldGeneration.addElem(generationNumber, generationToSave);
        } else if (generationNumber % SAVE_GAP == 0) {
            if (this.savedState.size() >= this.oldGeneration.getNumberOfElementsStored()) {
                this.savedState.remove(0);
            }
            this.savedState.add(generationNumber);
            this.oldGeneration.addElem(generationNumber, generationToSave);
        }
    }

}
