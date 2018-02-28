package view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * This class displays the main menu. Pattern: Singleton.
 */
public final class MainMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TITLE_SIZE = 80;
    private static final int BUTTON_TEXT_SIZE = 40;
    private static final int BUTTON_RATIO_Y = 10;
    private static final int BUTTON_RATIO_X = 5;
    private static final int TITLE_OFFSET = 120;
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
        title.setBorder(new EmptyBorder(TITLE_OFFSET, 0, 0, 0));
        this.add(title, BorderLayout.NORTH);
        final JPanel centralButtons = new JPanel();
        centralButtons.setLayout(new GridLayout(2, 1, 0, mainGUI.getCurrentHeight() / (BUTTON_RATIO_Y * BUTTON_RATIO_Y)));
        final JButton sandbox = new JButton(ApplicationStrings.sandboxButtonText());
        sandbox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, BUTTON_TEXT_SIZE));
        sandbox.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        final JButton exit = new JButton(ApplicationStrings.exitButtonText());
        exit.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        exit.addActionListener(e -> {
            mainGUI.close();
        });
        sandbox.addActionListener(e -> {
            mainGUI.setView(new Sandbox());
        });
        exit.setFocusPainted(false);
        exit.setFont(new Font(Font.MONOSPACED, Font.PLAIN, BUTTON_TEXT_SIZE));
        sandbox.setToolTipText(ApplicationStrings.getHoverSandboxButton());
        sandbox.setFocusPainted(false);
        centralButtons.add(sandbox);
        centralButtons.add(exit);
        center.add(centralButtons);
        final JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(new JLabel(ApplicationStrings.getVersion()), BorderLayout.EAST);
        lowerPanel.add(new JLabel(ApplicationStrings.getInfo()), BorderLayout.WEST);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }
}
