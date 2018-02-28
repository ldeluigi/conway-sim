package view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class displays the main menu. Pattern: Singleton.
 */
public final class MainMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TITLE_SIZE = 50;
    private static final int TEXT_RATIO = 2;
    private static final int BUTTON_RATIO_Y = 10;
    private static final int BUTTON_RATIO_X = 5;
    /**
     * The constructor fills the panel.
     * @param mainGUI the main GUI
     */
    public MainMenu(final GUI mainGUI) {
        this.setLayout(new BorderLayout());
        final JPanel center = new JPanel(new GridBagLayout());
        this.add(center, BorderLayout.CENTER);
        final JLabel title = new JLabel(ApplicationStrings.getApplicationTitle());
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, TITLE_SIZE));
        title.setHorizontalAlignment(JLabel.CENTER);
        this.add(title, BorderLayout.NORTH);
        final JPanel centralButtons = new JPanel();
        centralButtons.setLayout(new GridLayout(2, 1, 0, mainGUI.getCurrentHeight() / (BUTTON_RATIO_Y * BUTTON_RATIO_Y)));
        final JButton b1 = new JButton(ApplicationStrings.sandboxButtonText());
        b1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, TITLE_SIZE / TEXT_RATIO));
        b1.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        final JButton exit = new JButton(ApplicationStrings.exitButtonText());
        exit.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        exit.addActionListener(e -> {
            mainGUI.close();
        });
        exit.setFocusPainted(false);
        exit.setFont(new Font(Font.MONOSPACED, Font.PLAIN, TITLE_SIZE / TEXT_RATIO));
        b1.setToolTipText(ApplicationStrings.getHoverSandboxButton());
        b1.setFocusPainted(false);
        centralButtons.add(b1);
        centralButtons.add(exit);
        center.add(centralButtons);
        final JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(new JLabel(ApplicationStrings.getVersion()), BorderLayout.EAST);
        lowerPanel.add(new JLabel(ApplicationStrings.getInfo()), BorderLayout.WEST);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }
}
