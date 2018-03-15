package view.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import view.swing.menu.MainMenu;
/**
 * 
 *
 */
public final class MainGUI implements DesktopGUI {

    private static final int PIXELS_FROM_SCREEN_BORDERS = 50;
    private static final int MINIMUM_FRAME_RATIO = 2;
    private static final int INNER_FRAME_SCALE = 5;


    private final JFrame frame;
    private final JDesktopPane desktop;
    private final JInternalFrame background;

    /**
     * Starts the application.
     */
    public MainGUI() {
        this.frame = new JFrame("Conway's Game of Life");
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
        //Start with MainMenu
        final JPanel menuPanel = new MainMenu(this);
        setView(menuPanel);
        this.frame.setVisible(true);
    }

    /**
     * A method that changes the main view of the application (background).
     * @param viewPanel the panel that will be shown as main screen on the application desktop.
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
        System.exit(0);
    }

    /**
     * This method pops up a {@link JInternalFrame} in a fixed position.
     * @param iFrame the frame that pops up
     */
    @Override
    public void popUpFrame(final JInternalFrame iFrame) {
        final Dimension minDim = new Dimension(
                Math.max(iFrame.getMinimumSize().width, this.frame.getMinimumSize().width / MINIMUM_FRAME_RATIO),
                Math.max(iFrame.getMinimumSize().height, this.frame.getMinimumSize().height / MINIMUM_FRAME_RATIO));
        iFrame.setMinimumSize(minDim);
        iFrame.setSize(Math.max(minDim.width, iFrame.getWidth()), Math.max(minDim.height, iFrame.getHeight()));
        iFrame.setLocation((this.getCurrentWidth() - iFrame.getWidth()) / 2, this.desktop.getHeight() / INNER_FRAME_SCALE);
        iFrame.setVisible(true);
        this.desktop.add(iFrame);
        iFrame.setLayer(JDesktopPane.PALETTE_LAYER);
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

}
