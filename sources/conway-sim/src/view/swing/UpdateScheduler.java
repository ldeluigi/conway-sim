package view.swing;

/**
 * UpdateScheduler interface.
 */
public interface UpdateScheduler {

    /**
     * 
     * @param runnable the runnable that have to be added to the scheduler
     */
    void scheduleGUIUpdate(Runnable runnable);
}
