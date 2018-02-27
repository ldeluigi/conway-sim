package view.swing;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class displays the main menu. Pattern: Singleton.
 */
public final class MainMenu extends JPanel {

    private static final long serialVersionUID = 1L;
    /**
     * The main menu object.
     */
    public static final MainMenu JPANEL = new MainMenu();

    /**
     * The constructor fills the panel.
     */
    private MainMenu() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel(ApplicationStrings.getApplicationTitle()));
        this.add(new JButton("Test Button Main Menu"));
    }
}
