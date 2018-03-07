package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import controller.generation.GenerationController;
import controller.generation.GenerationControllerImpl;
import view.swing.MainGUI;
import view.swing.sandbox.Sandbox;

/**
 * JUnit test for GenerationHistory and GenerationController.
 */
public class TestGenerationUndo {

    @Test
    public void test() {
        GenerationController controller = new GenerationControllerImpl();
        controller.setView(new Sandbox(new MainGUI()));
        assertEquals(0, controller.getCurrentNumberGeneration().intValue());
        for (int i = 0; i < 20; i++) {
            controller.computeNextGeneration();
        }
        assertEquals(20, controller.getCurrentNumberGeneration().intValue());
        controller.loadOldGeneration(5L);
        assertEquals(5, controller.getCurrentNumberGeneration().intValue());
        for (int i = 0; i < 200; i++) {
            controller.computeNextGeneration();
        }
        assertEquals(205, controller.getCurrentNumberGeneration().intValue());
        controller.loadOldGeneration(67L);
        assertEquals(67, controller.getCurrentNumberGeneration().intValue());
        long now = System.currentTimeMillis() / 100;
        controller.loadOldGeneration(1000L);
        System.err.println("1000L " + (System.currentTimeMillis() / 100 - now));
        controller.loadOldGeneration(0L);
        now = System.currentTimeMillis() / 100;
        controller.loadOldGeneration(10000L);
        System.err.println("10000L " + (System.currentTimeMillis() / 100 - now));
        controller.loadOldGeneration(0L);
        now = System.currentTimeMillis() / 100;
        controller.loadOldGeneration(500L);
        System.err.println("500L " + (System.currentTimeMillis() / 100 - now));
        controller.loadOldGeneration(0L);
        now = System.currentTimeMillis() / 100;
        controller.loadOldGeneration(2000L);
        System.err.println("2000 " + (System.currentTimeMillis() / 100 - now));
    }

}
