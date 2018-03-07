package controller.generation;

/**
 * 
 */
public class Clock {

    private static final int MAX_TIME_GENERATION = 100000;
    private final AgentClock clockAgent = new AgentClock();
    private final Runnable runnable;

    /**
     * 
     * @param runnable the runnable of this clock.
     */
    public Clock(final Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Stop the clock.
     */
    public void stopClock() {
        this.clockAgent.setClock(false);
    }

    /**
     * Start the clock.
     */
    public void restartClock() {
        this.clockAgent.setClock(true);
    }

    /**
     * Start the Agent clock.
     */
    public void start() {
        this.clockAgent.start();
    }

    /**
     * 
     * @param speed Set the speed of this clock. wait ( 1000 / speed ) ms between every computation.
     */
    public void setSpeed(final int speed) {
        final Long sleepTime = 1000 / Long.valueOf(speed);
        if (sleepTime > 0 && sleepTime < MAX_TIME_GENERATION) {
            this.clockAgent.setStep(Long.valueOf(sleepTime));
        } else {
            throw new IllegalArgumentException();
        }
    }

    class AgentClock extends Thread {

        private static final long INIT_STEP = 900L;
        private Long step = INIT_STEP;
        private boolean clock;

        public void run() {
            while (true) {
                while (clock) {
                    runnable.run();
                    try {
                        sleep(step);
                    } catch (Exception e) {
                        System.err.println("Errore nella sleep!" + e.getMessage());
                    }
                }
                try {
                    sleep(step * 2);
                } catch (Exception e) {
                    System.err.println("Errore nella sleep!" + e.getMessage());
                }
            }
        }

        public void setClock(final boolean flag) {
            this.clock = flag;
        }

        public void setStep(final Long step) {
            this.step = step;
        }
    }
}
