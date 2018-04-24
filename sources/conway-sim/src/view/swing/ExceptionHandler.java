package view.swing;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.SwingUtilities;

import controller.io.Logger;

/**
 * Implementation of {@link UncaughtExceptionHandler}.
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public final void uncaughtException(final Thread t, final Throwable e) {
        Logger.logTime("Exception in thread: " + t.getName()
                + (e.getMessage() == null ? "" : ". Message: " + e.getMessage() + "."));
        Logger.logThrowable(e);
        SwingUtilities.invokeLater(() -> {
            Logger.logTime("Aborted");
            System.exit(1);
        });
    }

}
