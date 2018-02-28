package view.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
/**
 * 
 *
 */
public final class MainGUI implements GUI {

    private static final int PIXELS_FROM_SCREEN_BORDERS = 50;
    private static final int MINIMUM_FRAME_RATIO = 2;
    private static final int INNER_FRAME_SCALE = 5;


    private final JFrame frame;
    private final JDesktopPane desktop;
    private final JInternalFrame background;
    private final JPanel menuPanel;

    /**
     * Starts the application.
     */
    public MainGUI() {
        this.frame = new JFrame(ApplicationStrings.getApplicationTitle());
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.frame.addWindowListener(new WindowListener() {
            public void windowOpened(final WindowEvent e) {  }
            public void windowIconified(final WindowEvent e) {  }
            public void windowDeiconified(final WindowEvent e) {  }
            public void windowDeactivated(final WindowEvent e) {  }
            public void windowClosing(final WindowEvent e) {
                close();
            }
            public void windowClosed(final WindowEvent e) {
                close();
            }
            public void windowActivated(final WindowEvent e) {  }
        });
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setMinimumSize(
                new Dimension(screenSize.width / MINIMUM_FRAME_RATIO, screenSize.height / MINIMUM_FRAME_RATIO));
        this.frame.setBounds(PIXELS_FROM_SCREEN_BORDERS, PIXELS_FROM_SCREEN_BORDERS,
                screenSize.width - PIXELS_FROM_SCREEN_BORDERS * 2, screenSize.height - PIXELS_FROM_SCREEN_BORDERS * 2);
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
        final BasicInternalFrameTitlePane titlePane = (BasicInternalFrameTitlePane) basicInternalFrameUI.getNorthPane();
        this.background.remove(titlePane);
        this.background.setBorder(null);
        this.background.setVisible(true);
        //Change L&F
//        try {
//            javax.swing.UIManager.setLookAndFeel(
//                    javax.swing.UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//                | javax.swing.UnsupportedLookAndFeelException e) {
//        }
        //Start with MainMenu
        this.menuPanel = new MainMenu(this);
        setView(this.menuPanel);
        this.frame.setVisible(true);
    }

    /**
     * A method that changes the main view of the application (background).
     * @param viewPanel the panel that will be shown as main screen on the application desktop.
     */
    @Override
    public void setView(final JPanel viewPanel) {
        this.background.setContentPane(viewPanel);
    }

    /**
     * Method that closes the program, programmatically or called by user actions.
     */
    @Override
    public void close() {
        System.exit(0);
    }

    /**
     * This method pops up a {@link JInternalFrame} in a fixed position.
     * @param frame the frame that pops up
     */
    public void popUpFrame(final JInternalFrame frame) {
        final Dimension minDim = new Dimension(
                Math.max(frame.getMinimumSize().width, this.frame.getMinimumSize().width / MINIMUM_FRAME_RATIO),
                Math.max(frame.getMinimumSize().height, this.frame.getMinimumSize().height / MINIMUM_FRAME_RATIO));
        frame.setMinimumSize(minDim);
        frame.setSize(Math.max(minDim.width, frame.getWidth()), Math.max(minDim.height, frame.getHeight()));
        frame.setLocation((this.getCurrentWidth() - frame.getWidth()) / 2, this.desktop.getHeight() / INNER_FRAME_SCALE);
        frame.setVisible(true);
        this.desktop.add(frame);
        frame.setLayer(JDesktopPane.PALETTE_LAYER);
    }

    /**
     * This method closes all open {@link JInternalFrame} in the application.
     */
    public void closeFrames() {
        for (final JInternalFrame iframe : this.desktop.getAllFrames()) {
            iframe.doDefaultCloseAction();
        }
    }

    /**
     * Gets frame width.
     * @return current frame width
     */
    @Override
    public int getCurrentWidth() {
        return this.frame.getWidth();
    }

    /**
     * Gets frame height.
     * @return current frame height
     */
    @Override
    public int getCurrentHeight() {
        return this.frame.getHeight();
    }

    /**
     * Removes current view and replaces it with main menu.
     */
    @Override
    public void backToMainMenu() {
        setView(this.menuPanel);
    }

}
