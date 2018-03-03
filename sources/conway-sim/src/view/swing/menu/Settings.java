package view.swing.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import view.swing.GUI;

/**
 * Settings view.
 */
public final class Settings extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int BUTTON_RATIO_Y = 20;
    private static final int BUTTON_RATIO_X = 5;
    private static final int INITIAL_FONT_SIZE = 15;
    /**
     * User selected font size.
     */
    private static int fontSize = INITIAL_FONT_SIZE;

    private static boolean usingSystemLF;

    /**
     * This panel is a view of general settings.
     * @param mainGUI the GUI to return
     */
    public Settings(final GUI mainGUI) {
        this.setLayout(new GridBagLayout());
        final JPanel centralButtons = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        final JCheckBox checkLookAndFeel = new JCheckBox(MenuStrings.lookAndFeelCheck());
        checkLookAndFeel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Settings.getFontSize()));
        checkLookAndFeel.setSelected(isUsingSystemLF());
        checkLookAndFeel.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !isUsingSystemLF()) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    setUsingSystemLF(true);
                    this.repaint();
                } catch (Exception e1) {
                    final JLabel l = new JLabel(MenuStrings.noLookAndFeelAvailable());
                    JOptionPane.showInternalMessageDialog(this, l, "Unavailable", JOptionPane.ERROR_MESSAGE);
                    checkLookAndFeel.setSelected(false);
                }
            } else if (e.getStateChange() == ItemEvent.DESELECTED && isUsingSystemLF()) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    setUsingSystemLF(false);
                    this.repaint();
                } catch (Exception e1) {
                    final JLabel l = new JLabel(MenuStrings.noCrossPlatformLookAndFeel());
                    JOptionPane.showInternalMessageDialog(this, l, "Error", JOptionPane.ERROR_MESSAGE);
                    checkLookAndFeel.setSelected(true);
                }
            }
        });
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        centralButtons.add(checkLookAndFeel, c);
        final JButton ret = new JButton(MenuStrings.returnToMainMenuText());
        ret.setPreferredSize(
                new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        ret.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Settings.getFontSize()));
        ret.addActionListener(e -> {
            mainGUI.backToMainMenu();
        });
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 8;
        c.gridwidth = 4;
        centralButtons.add(ret, c);
        this.add(centralButtons);
    }

    private boolean isUsingSystemLF() {
        return Settings.usingSystemLF;
    }

    private void setUsingSystemLF(final boolean usingSystemLF) {
        Settings.usingSystemLF = usingSystemLF;
    }

    /**
     * @return the fontSize
     */
    public static int getFontSize() {
        return fontSize;
    }
}
