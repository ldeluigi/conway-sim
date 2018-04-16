package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import controller.generation.GenerationControllerImpl;
import core.model.Generation;
import core.model.Generations;
import view.swing.MainGUI;
import view.swing.sandbox.SandboxImpl;

/**
 * JUnit test for GenerationHistory and GenerationController.
 */
public class TestGenerationUndo {

    /**
     * 
     */
     @Test
     public void testComputingUndo() {
         GenerationControllerImpl controller = new GenerationControllerImpl(new SandboxImpl(new MainGUI()));
         Generation first = controller.getCurrentElement();
         controller.computeNext();
         controller.computeNext();
         controller.computeNext();
         Generation third = Generations.compute(3, first);
         assertEquals(third.getCellMatrix(), controller.getCurrentElement().getCellMatrix());
         //load the first generation
         controller.loadOldElement(0L);
         assertEquals(first, controller.getCurrentElement());
     }
}
