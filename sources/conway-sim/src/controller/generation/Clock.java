package controller.generation;

import java.util.Optional;

/**
 * 
 */
public class Clock {

    private static final int MAX_TIME_GENERATION = 1000;
    private int speed = 1;
    private final int maxSpeed;
    private final int speedPart;
    private Optional<AgentClock> clockAgent = Optional.empty();
    private final Runnable runnable;

    /**
     * 
     * @param runnable the runnable of this clock.
     * @param maxSpeed the maxSpeed for the clock.
     */
    public Clock(final Runnable runnable, final int maxSpeed) {
        this.runnable = runnable;
        this.maxSpeed = maxSpeed;
        this.speedPart = MAX_TIME_GENERATION / maxSpeed;
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
     * 
     * @param speed Set the speed of this clock. wait ( 1000 / speed ) ms between every computation.
     */
    public void setSpeed(final int speed) {
        if (speed < 0 || speed > maxSpeed) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        if (this.clockAgent.isPresent()) {
            final Long sleepTime = Long.valueOf(maxSpeed - speed + 1) * speedPart;
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
