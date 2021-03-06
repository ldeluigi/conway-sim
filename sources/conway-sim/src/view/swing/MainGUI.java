package view.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import controller.io.Logger;
import controller.io.ResourceLoader;
import view.swing.menu.MainMenu;

/**
 * Implementation of a {@link DesktopGUI} with swing.
 */
public final class MainGUI implements DesktopGUI {

    private static final int PIXELS_FROM_SCREEN_BORDERS = 50;
    private static final float MINIMUM_FRAME_RATIO = 2f;
    private static final int INNER_FRAME_SCALE = 5;
    private static final float MINIMUM_INTERNAL_FRAME_RATIO = 2f;

    private final JFrame frame;
    private final JDesktopPane desktop;
    private final JInternalFrame background;

    /**
     * Starts the application, displaying the frame.
     */
    public MainGUI() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
        this.frame = new JFrame(ResourceLoader.loadString("frame.title"));
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.frame.addWindowListener(new WindowListener() {
            public void windowOpened(final WindowEvent e) {
            }

            public void windowIconified(final WindowEvent e) {
            }

            public void windowDeiconified(final WindowEvent e) {
            }

            public void windowDeactivated(final WindowEvent e) {
            }

            public void windowClosing(final WindowEvent e) {
                close();
            }

            public void windowClosed(final WindowEvent e) {
                close();
            }

            public void windowActivated(final WindowEvent e) {
            }
        });
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setMinimumSize(new Dimension(Math.round(screenSize.width / MINIMUM_FRAME_RATIO),
                Math.round(screenSize.height / MINIMUM_FRAME_RATIO)));
        this.frame.setSize(screenSize.width - PIXELS_FROM_SCREEN_BORDERS * 2,
                screenSize.height - PIXELS_FROM_SCREEN_BORDERS * 2);
        this.frame.setLocationRelativeTo(null);
        this.desktop = new JDesktopPane();
        this.frame.setContentPane(this.desktop);
        this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        this.background = new JInternalFrame("", false, false, false);
        this.frame.getContentPane().add(this.background);
        try {
            background.setMaximum(true);
        } catch (PropertyVetoException e1) {
            throw new IllegalStateException("Background Internal Frame has to be full size");
        }
        this.background.setLayer(JDesktopPane.DEFAULT_LAYER);
        this.background.setEnabled(false);
        final BasicInternalFrameUI basicInternalFrameUI = ((BasicInternalFrameUI) this.background.getUI());
        basicInternalFrameUI.setNorthPane(null);
        this.background.setBorder(null);
        this.background.setVisible(true);
        // Start with MainMenu
        final JPanel menuPanel = new MainMenu(this);
        setView(menuPanel);
        this.frame.setIconImage(ResourceLoader.loadImage("main.icon"));
        this.frame.setVisible(true);

        Logger.logTime("Started application from " + this.getClass().getName());
    }

    /**
     * A method that changes the main view of the application (background).
     * 
     * @param viewPanel
     *            the panel that will be shown as main screen on the application
     *            desktop.
     */
    @Override
    public void setView(final JComponent viewPanel) {
        this.background.setContentPane(viewPanel);
    }

    /**
     * Method that closes the program, programmatically or called by user actions.
     */
    @Override
    public void close() {
        Logger.logTime("Closed application");
        System.exit(0);
    }

    /**
     * This method pops up a {@link JInternalFrame} in a fixed position.
     * 
     * @param iFrame
     *            the frame that pops up
     */
    @Override
    public void popUpFrame(final JInternalFrame iFrame, final boolean maximum) {
        final Dimension minDim = new Dimension(
                Math.max(iFrame.getMinimumSize().width,
                        Math.round(this.frame.getMinimumSize().width / MINIMUM_INTERNAL_FRAME_RATIO)),
                Math.max(iFrame.getMinimumSize().height,
                        Math.round(this.frame.getMinimumSize().height / MINIMUM_INTERNAL_FRAME_RATIO)));
        iFrame.setMinimumSize(minDim);
        iFrame.setSize(Math.max(minDim.width, iFrame.getWidth()), Math.max(minDim.height, iFrame.getHeight()));
        iFrame.setLocation((this.getCurrentWidth() - iFrame.getWidth()) / 2,
                this.desktop.getHeight() / INNER_FRAME_SCALE);
        iFrame.setVisible(true);
        this.desktop.add(iFrame);
        iFrame.setLayer(JDesktopPane.PALETTE_LAYER);
        if (maximum) {
            try {
                iFrame.setMaximum(true);
            } catch (PropertyVetoException e) {
                System.err.println("Frame not maximizable.");
            }
        }
    }

    @Override
    public List<JInternalFrame> getAllFrames() {
        return Arrays.asList(this.desktop.getAllFrames());
    }

    @Override
    public void detachFrame(final JInternalFrame iFrame) {
        this.desktop.remove(iFrame);
    }

    @Override
    public int getCurrentWidth() {
        return this.frame.getWidth();
    }

    @Override
    public int getCurrentHeight() {
        return this.frame.getHeight();
    }

    @Override
    public void backToMainMenu() {
        setView(new MainMenu(this));
    }

    @Override
    public int getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    @Override
    public int getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

}
