package test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;

import controller.generation.GenerationControllerImpl;
import core.model.Generation;
import core.model.Generations;
import junit.framework.Assert;
import view.swing.MainGUI;
import view.swing.sandbox.SandboxImpl;

/**
 * JUnit test for GenerationHistory and GenerationController.
 */
public class TestGenerationUndo {

    private int count = 0;
    private boolean ok = true;

     @Test
     public void testForGenInOneSec() {
         GenerationControllerImpl controller = new GenerationControllerImpl(new SandboxImpl(new MainGUI()));
         Generation first = controller.getCurrentElement();
         controller.computeNext();
         controller.computeNext();
         controller.computeNext();
         assertNotEquals(first, controller.getCurrentElement());

     }

     private boolean sm(final boolean b) {
     if (ok && b) {
     return b;
     } else {
     ok = false;
     return false;
     }
     }
    
     @Test
     public void testProcessor() {
     GenerationControllerImpl controller = new GenerationControllerImpl();
     controller.setView(new SandboxImpl(new MainGUI()));
     long start = 0;
     long end = 0;
     long dif1 = 0, dif2 = 0, dif3 = 0;
     int i = 0;
     while ( i++ < 10) {
     start = System.currentTimeMillis();
     Generations.compute(1000, controller.getCurrentGeneration(), 4);
     end = System.currentTimeMillis();
     dif2 += end - start;
     start = System.currentTimeMillis();
     Generations.compute(1000, controller.getCurrentGeneration(), 1);
     end = System.currentTimeMillis();
     dif3 += end - start;
     start = System.currentTimeMillis();
     final int p = Runtime.getRuntime().availableProcessors();
     System.out.println(p);
     Generations.compute(1000, controller.getCurrentGeneration(), p);
     end = System.currentTimeMillis();
     dif1 += end - start;
     }
     System.err.println(dif1 / 10);
     System.err.println(dif2 / 10);
     System.err.println(dif3 / 10);
     }
    
     private void inc() {
     count++;
     }
}
