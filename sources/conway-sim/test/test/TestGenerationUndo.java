package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import controller.editor.PatternEditor;
import controller.generation.GenerationController;
import controller.generation.GenerationControllerImpl;
import core.model.EnvironmentFactory;
import core.model.Generation;
import core.model.GenerationFactory;
import core.model.Generations;
import core.model.SimpleCell;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.Sandbox;

/**
 * JUnit test for GenerationHistory and GenerationController.
 */
public class TestGenerationUndo {

    private static final long LOAD_ELEMENT = 10L;

    /**
     * 
     */
    @Test
    public void testLoad() {
        final GenerationController controller = new GenerationControllerImpl(newSandboxTest());
        final Generation firstGeneration = GenerationFactory.copyOf(controller.getCurrentElement());
        assertEquals(firstGeneration.toString(), controller.getCurrentElement().toString(),
                "Error in the firse equals");
        final Generation secondGeneration = Generations.compute(firstGeneration);
        controller.computeNext();
        assertEquals(secondGeneration.toString(), controller.getCurrentElement().toString(),
                "Error in computeNextGeneration");
        controller.loadElement(LOAD_ELEMENT);
        final Generation loadGeneration = Generations.compute((int) LOAD_ELEMENT, firstGeneration);
        assertEquals(loadGeneration.toString(), controller.getCurrentElement().toString(), "Error in load element");
        final Generation previousGeneration = Generations.compute(((int) LOAD_ELEMENT) - 1, firstGeneration);
        controller.loadElement(LOAD_ELEMENT - 1L);
        assertEquals(previousGeneration.toString(), controller.getCurrentElement().toString(),
                "Error in load previous element");
    }

    private Sandbox newSandboxTest() {
        return new Sandbox() {

            @Override
            public void scheduleGUIUpdate(final Runnable runnable) {
            }

            @Override
            public void setButtonClearEnabled(final boolean flag) {
            }

            @Override
            public void setButtonBookEnable(final boolean flag) {
            }

            @Override
            public void resetGrid() {
            }

            @Override
            public void refreshView() {
            }

            @Override
            public PatternEditor getGridEditor() {
                return new PatternEditor() {

                    @Override
                    public void setEnabled(final Boolean enabled) {
                    }

                    @Override
                    public boolean isEnabled() {
                        return false;
                    }

                    @Override
                    public void hit(final int row, final int column) {
                    }

                    @Override
                    public Generation getGeneration() {
                        return GenerationFactory.from(
                                new ListMatrix(10, 10,
                                        () -> new SimpleCell(Math.random() > 0.5 ? Status.DEAD : Status.ALIVE)),
                                EnvironmentFactory.standardRules(10, 10));
                    }

                    @Override
                    public void draw(final Generation gen) {
                    }

                    @Override
                    public void clean() {
                    }

                    @Override
                    public void changeSizes(final int horizontal, final int vertical) {
                    }

                    @Override
                    public void showPreview(final int row, final int column) {
                    }

                    @Override
                    public void rotateCurrentPattern(final int hits) {
                    }

                    @Override
                    public void removePatternToPlace() {
                    }

                    @Override
                    public void placeCurrentPattern(final int row, final int column) {
                    }

                    @Override
                    public boolean isPlacingModeOn() {
                        return false;
                    }

                    @Override
                    public void addPatternToPlace(final Matrix<Status> statusMatrix) {
                    }
                };
            }
        };
    }
}
