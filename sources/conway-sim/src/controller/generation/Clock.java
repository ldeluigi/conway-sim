package controller.generation;

/**
 * 
 */
public class Clock {

    private static final int MAX_TIME_GENERATION = 1000;
    private static final int MAX_SPEED = 10;
    private static final int SPEED_PART = MAX_TIME_GENERATION / MAX_SPEED;
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
        if (speed < 0 || speed > MAX_SPEED) {
            throw new IllegalArgumentException();
        }
        final Long sleepTime = Long.valueOf(MAX_SPEED - speed + 1) * SPEED_PART;
        this.clockAgent.setStep(Long.valueOf(sleepTime));
        System.err.println("current speed = " + speed + " sleep time = " + sleepTime);
    }

    class AgentClock extends Thread {

        private static final long INIT_STEP = 1000L;
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
