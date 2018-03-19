package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;

import controller.generation.GenerationControllerImpl;
import core.model.Generations;
import junit.framework.Assert;
import view.swing.MainGUI;
import view.swing.sandbox.Sandbox;

/**
 * JUnit test for GenerationHistory and GenerationController.
 */
public class TestGenerationUndo {

    private int count = 0;
    private boolean ok = true;

//    @Test
//    public void testForGenInOneSec() {
//        GenerationControllerImpl controller = new GenerationControllerImpl();
//        controller.setView(new Sandbox(new MainGUI()));
//        boolean stop = true;
//        FutureTask<Integer> f = new FutureTask<>(() -> {
//            while (sm(true)) {
//                this.inc();
//                controller.computeNextGeneration();
//            }
//        }, null);
//        Executor exe = Executors.newSingleThreadExecutor();
//        exe.execute(f);
//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        sm(false);
//        f.cancel(true);
//        int c = count;
//        System.err.println(count);
//        System.err.println(count);
//        System.err.println(count);
//        controller.computeNextGeneration();
//        System.err.println(count);
//        assertEquals(c, count);
//        int result = c / 100 * 100;
//        System.out.println(result);
//    }
//
//    private boolean sm(final boolean b) {
//        if (ok && b) {
//            return b;
//        } else {
//            ok = false;
//            return false;
//        }
//    }
//
//    @Test
//    public void testProcessor() {
//        GenerationControllerImpl controller = new GenerationControllerImpl();
//        controller.setView(new Sandbox(new MainGUI()));
//        long start = 0;
//        long end = 0;
//        long dif1 = 0, dif2 = 0, dif3 = 0;
//        int i = 0;
//        while ( i++ < 10) {
//            start = System.currentTimeMillis();
//            Generations.compute(1000, controller.getCurrentGeneration(), 4);
//            end = System.currentTimeMillis();
//            dif2 += end - start;
//            start = System.currentTimeMillis();
//            Generations.compute(1000, controller.getCurrentGeneration(), 1);
//            end = System.currentTimeMillis();
//            dif3 += end - start;
//            start = System.currentTimeMillis();
//            final int p = Runtime.getRuntime().availableProcessors();
//            System.out.println(p);
//            Generations.compute(1000, controller.getCurrentGeneration(), p);
//            end = System.currentTimeMillis();
//            dif1 += end - start;
//        }
//        System.err.println(dif1 / 10);
//        System.err.println(dif2 / 10);
//        System.err.println(dif3 / 10);
//    }
//
//    private void inc() {
//        count++;
//    }
}
