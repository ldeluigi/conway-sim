package view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
    private static final int BUTTON_RATIO = 10;
    /**
     * The constructor fills the panel.
     * @param mainGUI the main GUI
     */
    public MainMenu(final MainGUI mainGUI) {
        final JLabel title = new JLabel(ApplicationStrings.getApplicationTitle());
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, TITLE_SIZE));
        title.setHorizontalAlignment(JLabel.CENTER);
        this.setLayout(new BorderLayout(0, TITLE_SIZE));
        this.add(title, BorderLayout.NORTH);
        final JPanel centralButtons = new JPanel();
        centralButtons.setLayout(new GridLayout(2, 1, 0, mainGUI.getCurrentHeight() / (BUTTON_RATIO * BUTTON_RATIO)));
        final JButton b1 = new JButton(ApplicationStrings.sandboxButtonText());
        b1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, TITLE_SIZE / TEXT_RATIO));
        b1.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO, mainGUI.getCurrentHeight() / BUTTON_RATIO));
        final JButton exit = new JButton(ApplicationStrings.exitButtonText());
        exit.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO, mainGUI.getCurrentHeight() / BUTTON_RATIO));
        exit.addActionListener(e -> {
            mainGUI.close();
        });
        exit.setFocusPainted(false);
        exit.setFont(new Font(Font.MONOSPACED, Font.PLAIN, TITLE_SIZE / TEXT_RATIO));
        b1.setToolTipText(ApplicationStrings.getHoverSandboxButton());
        b1.setFocusPainted(false);
        centralButtons.add(b1);
        centralButtons.add(exit);
        this.add(centralButtons, BorderLayout.CENTER);
        final JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(new JLabel(ApplicationStrings.getVersion()), BorderLayout.EAST);
        lowerPanel.add(new JLabel(ApplicationStrings.getInfo()), BorderLayout.WEST);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }
}
