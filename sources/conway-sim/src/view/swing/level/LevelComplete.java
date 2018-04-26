package view.swing.level;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import controller.io.ResourceLoader;
import view.swing.DesktopGUI;
import view.swing.sandbox.SandboxTools;

/**
 * An InternalFrame that displays a thumb up image in it.
 */
public class LevelComplete extends JInternalFrame {

    private static final long serialVersionUID = -895123634745639926L;
    private static final String IMAGE_RESOURCE_MAIN = "level.complete.background";
    private static final String IMAGE_RESOURCE_EGG = "level.complete.egg";
    private static final double EGG_PROB = 0.01;

    /**
     * Sets the image as main pane and displays it.
     * 
     * @param mainGUI
     *            the desktopGUI that call this frame.
     */
    public LevelComplete(final DesktopGUI mainGUI) {
        super(ResourceLoader.loadString("level.complete.frame.title"), false, true, true, false);
        final JPanel jp = new JPanel() {
            private static final long serialVersionUID = 7156522143177179412L;

            @Override
            public void paintComponent(final Graphics g) {
                g.drawImage(
                        ResourceLoader.loadImage(Math.random() > EGG_PROB ? IMAGE_RESOURCE_MAIN : IMAGE_RESOURCE_EGG),
                        0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        this.setContentPane(jp);
        final JButton bReturnToLevel = SandboxTools.newJButton("MENU");
        jp.setLayout(new BorderLayout());
        jp.add(bReturnToLevel, BorderLayout.AFTER_LAST_LINE);
        bReturnToLevel.addActionListener(e -> {
            mainGUI.setView(new LevelMenu(mainGUI));
            this.doDefaultCloseAction();
        });
    }
}
