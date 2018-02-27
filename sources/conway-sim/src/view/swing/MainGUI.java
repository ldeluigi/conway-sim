package view.swing;

import java.awt.Dimension;
import java.awt.GridBagLayout;
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
public final class MainGUI {

    private static final int PIXELS_FROM_SCREEN_BORDERS = 50;
    private static final int MINIMUM_FRAME_RATIO = 2;
    private static final int INNER_FRAME_SCALE = 5;


    private final JFrame frame;
    private final JDesktopPane desktop;
    private final JPanel mainPanel;
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
        this.frame.setMinimumSize(new Dimension(screenSize.width / MINIMUM_FRAME_RATIO, screenSize.height / MINIMUM_FRAME_RATIO));
        this.frame.setBounds(PIXELS_FROM_SCREEN_BORDERS,
                    PIXELS_FROM_SCREEN_BORDERS,
                    screenSize.width - PIXELS_FROM_SCREEN_BORDERS * 2,
                    screenSize.height - PIXELS_FROM_SCREEN_BORDERS * 2);
        this.desktop = new JDesktopPane();
        this.frame.setContentPane(this.desktop);
        this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        this.mainPanel = new JPanel(new GridBagLayout());
        final JInternalFrame background = new JInternalFrame("", false, false, false);
        background.setContentPane(this.mainPanel);
        this.frame.getContentPane().add(background);
        try {
            background.setMaximum(true);
        } catch (PropertyVetoException e1) {
            throw new IllegalStateException("Background Internal Frame has to be full size");
        }
        background.setLayer(JDesktopPane.DEFAULT_LAYER);
        background.setEnabled(false);
        final BasicInternalFrameUI basicInternalFrameUI = ((BasicInternalFrameUI) background.getUI());
        final BasicInternalFrameTitlePane titlePane = (BasicInternalFrameTitlePane) basicInternalFrameUI.getNorthPane();
        background.remove(titlePane);
        background.setVisible(true);
        this.menuPanel = new MainMenu(this);
        setView(this.menuPanel);
        this.frame.setVisible(true);
    }

    /**
     * A method that changes the main view of the application.
     * @param viewPanel the panel that will be shown as main screen on the application desktop.
     */
    public void setView(final JPanel viewPanel) {
        this.mainPanel.removeAll();
        this.mainPanel.add(viewPanel);
    }

    /**
     * Method that closes the program, programmatically or called by user actions.
     */
    public void close() {
        System.exit(0);
    }

    /**
     * This method pops up a {@link JInternalFrame} in a fixed position.
     * @param frame the frame that pops up
     */
    public void popUpFrame(final JInternalFrame frame) {
        frame.setLocation(this.desktop.getWidth() / INNER_FRAME_SCALE, this.desktop.getHeight() / INNER_FRAME_SCALE);
        frame.setMinimumSize(new Dimension(this.desktop.getMinimumSize().width / MINIMUM_FRAME_RATIO, this.desktop.getMinimumSize().height / MINIMUM_FRAME_RATIO));
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
    public int getCurrentWidth() {
        return this.frame.getWidth();
    }

    /**
     * Gets frame height.
     * @return current frame height
     */
    public int getCurrentHeight() {
        return this.frame.getHeight();
    }

    /**
     * Removes current view and replaces it with main menu.
     */
    public void backToMainMenu() {
        setView(this.menuPanel);
    }

}
