package controller.generation;

import java.util.Optional;

import controller.io.ResourceLoader;

/**
 * This class uses a thread to implement a Clock that triggers events in form of
 * Runnable.
 */
public class Clock {

    private static final int MIN_CLOCK_TIME = ResourceLoader.loadConstantInt("clock.MIN_CLOCK_TIME");
    private static final int MAX_CLOCK_TIME = ResourceLoader.loadConstantInt("clock.MAX_CLOCK_TIME");
    private static final int MIN_SPEED = ResourceLoader.loadConstantInt("generation.MIN_SPEED");
    private static final int MAX_SPEED = ResourceLoader.loadConstantInt("generation.MAX_SPEED");
    private int speed = MIN_SPEED;
    private Optional<AgentClock> clockAgent = Optional.empty();
    private final Runnable runnable;

    /**
     * @param runnable
     *            the runnable of this clock.
     */
    public Clock(final Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Stop the clock.
     */
    public void stopClock() {
        this.clockAgent.get().setClock(false);
        try {
            this.clockAgent.get().join();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Thread not terminated corectly");
        }
    }

    /**
     * Start the Agent clock.
     */
    public void start() {
        if (this.clockAgent.isPresent() && this.clockAgent.get().isAlive()) {
            throw new IllegalStateException("Clock is just alive");
        }
        this.clockAgent = Optional.of(new AgentClock());
        this.setSpeed(this.speed);
        this.clockAgent.get().setClock(true);
        this.clockAgent.get().start();
    }

    /**
     * Set the speed of this clock. wait ( 1000 / speed ) ms between every
     * computation.
     * 
     * @param speed
     *            to set
     */
    public void setSpeed(final int speed) {
        if (speed < 0 || speed > MAX_SPEED) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        if (this.clockAgent.isPresent()) {
            final Long sleepTime = Long.valueOf(-(MAX_CLOCK_TIME - MIN_CLOCK_TIME) / (MAX_SPEED - MIN_SPEED) * speed
                    + MAX_CLOCK_TIME + MIN_CLOCK_TIME);
            this.clockAgent.get().setStep(Long.valueOf(sleepTime));
        }
    }

    class AgentClock extends Thread {

        private static final long INIT_STEP = 1000L;
        private volatile Long step = INIT_STEP;
        private volatile boolean clock;

        public void run() {
            while (clock) {
                runnable.run();
                try {
                    sleep(this.getStep());
                } catch (Exception e) {
                    System.err.println("Errore nella sleep!" + e.getMessage());
                }
            }
        }

        public void setClock(final boolean flag) {
            this.clock = flag;
        }

        public synchronized void setStep(final Long step) {
            this.step = step;
        }

        private synchronized Long getStep() {
            return this.step;
        }
    }
}
