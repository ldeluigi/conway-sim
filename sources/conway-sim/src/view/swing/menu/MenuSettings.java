package view.swing.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import view.swing.GUI;

/**
 * Settings view.
 */
public final class MenuSettings extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int BUTTON_RATIO_Y = 20;
    private static final int BUTTON_RATIO_X = 5;
    private static final int MINI_BUTTON_RATIO_Y = 30;
    private static final int MINI_BUTTON_RATIO_X = 25;
    private static final int INITIAL_FONT_SIZE = 15;
    private static final int GRID_WIDTH = 5;
    private static final int MAX_FONT_SIZE = 30;

    private static int fontSize = INITIAL_FONT_SIZE;
    private static boolean usingSystemLF;
    private final List<JComponent> toResize = new LinkedList<>();

    /**
     * This panel is a view of general settings.
     * @param mainGUI the GUI to return
     */
    public MenuSettings(final GUI mainGUI) {
        this.setLayout(new GridBagLayout());
        final JPanel centralButtons = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        final JCheckBox checkLookAndFeel = new JCheckBox();
        checkLookAndFeel.setSelected(isUsingSystemLF());
        checkLookAndFeel.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    setUsingSystemLF(true);
                } catch (Exception e1) {
                    final JLabel l = new JLabel("No System Look and Feel found.");
                    JOptionPane.showMessageDialog(this, l, "Unavailable", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    setUsingSystemLF(false);
                } catch (Exception e1) {
                    final JLabel l = new JLabel("No System Look and Feel found.");
                    JOptionPane.showMessageDialog(this, l, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            checkLookAndFeel.setSelected(isUsingSystemLF());
            this.repaint();
        });
        final JLabel checkLFLabel = new JLabel("Use System Look and Feel (if available)");
        checkLFLabel.setFont(MenuSettings.generateFont());
        final SpinnerModel fontSizeSelectorModel = new SpinnerNumberModel(MenuSettings.getFontSize(), 1, MAX_FONT_SIZE, 1);
        final JSpinner fontSizeSelector = new JSpinner(fontSizeSelectorModel);
        fontSizeSelector.setFont(generateFont());
        fontSizeSelector.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / MINI_BUTTON_RATIO_X,
                mainGUI.getCurrentHeight() / MINI_BUTTON_RATIO_Y));
        fontSizeSelectorModel.addChangeListener(e -> {
            setFontSize((int) fontSizeSelectorModel.getValue());
            resizeFonts();
        });
        ((DefaultEditor) fontSizeSelector.getEditor()).getTextField().setEditable(false);
        final JLabel fontLabel = new JLabel("Font dimension");
        fontLabel.setFont(MenuSettings.generateFont());
        c.insets = new Insets(mainGUI.getCurrentHeight() / (MINI_BUTTON_RATIO_Y * 2), 0,
                mainGUI.getCurrentHeight() / (MINI_BUTTON_RATIO_Y * 2), 0);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weightx = 0.5;
        c.weighty = 0.5;
        centralButtons.add(checkLFLabel, c);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        centralButtons.add(checkLookAndFeel, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        centralButtons.add(fontLabel, c);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        centralButtons.add(fontSizeSelector, c);
        final JButton ret = new JButton("Return");
        ret.setPreferredSize(
                new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        ret.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        ret.addActionListener(e -> {
            mainGUI.backToMainMenu();
        });
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = GRID_WIDTH;
        centralButtons.add(ret, c);
        this.add(centralButtons);
        this.toResize.add(checkLFLabel);
        this.toResize.add(fontLabel);
        this.toResize.add(((DefaultEditor) fontSizeSelector.getEditor()).getTextField());
        this.toResize.add(ret);
    }

    private void setFontSize(final int value) {
        MenuSettings.fontSize = value;
    }

    private void resizeFonts() {
        this.toResize.forEach(component -> {
            component.setFont(generateFont(component));
        });
    }

    private static Font generateFont() {
        return new Font(Font.SANS_SERIF, Font.BOLD, MenuSettings.getFontSize());
    }

    private static Font generateFont(final Component c) {
        return new Font(c.getFont().getFontName(), c.getFont().getStyle(), MenuSettings.getFontSize());
    }

    private boolean isUsingSystemLF() {
        return MenuSettings.usingSystemLF;
    }

    private void setUsingSystemLF(final boolean usingSystemLF) {
        MenuSettings.usingSystemLF = usingSystemLF;
    }

    /**
     * @return the fontSize
     */
    public static int getFontSize() {
        return fontSize;
    }
}
