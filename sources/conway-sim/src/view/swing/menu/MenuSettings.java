package view.swing.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;

import controller.io.InformationManager;
import controller.io.ResourceLoader;
import view.swing.GUI;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

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
    private static final int GRIDBAG_WIDTH = 5;
    private static final int MAX_FONT_SIZE = 30;
    private static final int BUTTON_FONT_PLUS = 10;
    private static final int MIN_FONT_SIZE = 1;

    private static int fontSize = INITIAL_FONT_SIZE;
    private static boolean usingSystemLF;
    private static boolean instantTransitions = true;

    private final List<JComponent> toResize = new LinkedList<>();

    /**
     * This panel is a view of general settings.
     * 
     * @param mainGUI
     *            the GUI to return
     */
    public MenuSettings(final GUI mainGUI) {
        this.setLayout(new GridBagLayout());
        final JPanel centralButtons = new JPanel(new GridBagLayout());
        centralButtons.setOpaque(false);
        final GridBagConstraints c = new GridBagConstraints();
        final JCheckBox checkLookAndFeel = new JCheckBox();
        checkLookAndFeel.setOpaque(false);
        checkLookAndFeel.setSelected(isUsingSystemLF());
        checkLookAndFeel.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !setUsingSystemLF(true)) {
                final JLabel l = new JLabel(ResourceLoader.loadString("settings.error.SysLookAndFeelNotFound"));
                JOptionPane.showMessageDialog(this, l, ResourceLoader.loadString("error.unavailable"),
                        JOptionPane.ERROR_MESSAGE);
            } else if (e.getStateChange() == ItemEvent.DESELECTED && !setUsingSystemLF(false)) {
                final JLabel l = new JLabel(ResourceLoader.loadString("settings.error.CrossLookAndFeelNotFound"));
                JOptionPane.showMessageDialog(this, l, ResourceLoader.loadString("settings.error.unavailable"),
                        JOptionPane.ERROR_MESSAGE);
            }
            checkLookAndFeel.setSelected(isUsingSystemLF());
            this.repaint();
        });
        final JLabel checkLFLabel = new JLabel(ResourceLoader.loadString("settings.sysLF"));
        checkLFLabel.setFont(MenuSettings.generateFont());
        final SpinnerModel fontSizeSelectorModel = new SpinnerNumberModel(MenuSettings.getFontSize(), MIN_FONT_SIZE,
                MAX_FONT_SIZE, 1);
        final JSpinner fontSizeSelector = new JSpinner(fontSizeSelectorModel);
        fontSizeSelector.setOpaque(false);
        fontSizeSelector.setFont(generateFont());
        fontSizeSelector.setPreferredSize(new Dimension(mainGUI.getCurrentWidth() / MINI_BUTTON_RATIO_X,
                mainGUI.getCurrentHeight() / MINI_BUTTON_RATIO_Y));
        fontSizeSelectorModel.addChangeListener(e -> {
            setFontSize((int) fontSizeSelectorModel.getValue());
            resizeFonts();
        });
        ((DefaultEditor) fontSizeSelector.getEditor()).getTextField().setEditable(false);
        final JLabel fontLabel = new JLabel(ResourceLoader.loadString("settings.fontDimension"));
        fontLabel.setFont(MenuSettings.generateFont());
        final JCheckBox checkInstantAnimations = new JCheckBox();
        checkInstantAnimations.setOpaque(false);
        checkInstantAnimations.setSelected(areTransitionsInstant());
        checkInstantAnimations.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setInstantTransitions(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                setInstantTransitions(false);
            }
        });
        final JLabel checkInstantAnimationsLabel = new JLabel(ResourceLoader.loadString("settings.instantTransitions"));
        checkInstantAnimationsLabel.setFont(MenuSettings.generateFont());
        final JLabel languageLabel = new JLabel(ResourceLoader.loadString("settings.language.label"));
        languageLabel.setFont(MenuSettings.generateFont());
        final Locale[] listLocales = ResourceLoader.getLocales();
        final JComboBox<Locale> languageComboBox = new JComboBox<>(listLocales);
        languageComboBox.setSelectedItem(Locale.getDefault());
        languageComboBox.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(final ActionEvent e) {
                Locale.setDefault((Locale) ((JComboBox<Locale>) e.getSource()).getSelectedItem());
            }
        });
        c.insets = new Insets(mainGUI.getCurrentHeight() / (MINI_BUTTON_RATIO_Y * 2), 0,
                mainGUI.getCurrentHeight() / (MINI_BUTTON_RATIO_Y * 2), 0);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        centralButtons.add(checkLFLabel, c);
        addToCenter(GridBagConstraints.WEST, 0, 0, 3, checkLFLabel, c, centralButtons);
        addToCenter(GridBagConstraints.EAST, 4, 0, 1, checkLookAndFeel, c, centralButtons);
        addToCenter(GridBagConstraints.WEST, 0, 1, 3, checkInstantAnimationsLabel, c, centralButtons);
        addToCenter(GridBagConstraints.EAST, 4, 1, 1, checkInstantAnimations, c, centralButtons);
        addToCenter(GridBagConstraints.WEST, 0, 2, 3, fontLabel, c, centralButtons);
        addToCenter(GridBagConstraints.EAST, 4, 2, 1, fontSizeSelector, c, centralButtons);
        addToCenter(GridBagConstraints.WEST, 0, 3, 3, languageLabel, c, centralButtons);
        addToCenter(GridBagConstraints.EAST, 4, 3, 1, languageComboBox, c, centralButtons);
        final JButton ret = new JButton(ResourceLoader.loadString("settings.return"));
        ret.setBackground(Color.WHITE);
        ret.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, false));
        ret.setFocusPainted(false);
        ret.setPreferredSize(
                new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        ret.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() + BUTTON_FONT_PLUS));
        ret.addActionListener(e -> {
            saveSettings();
            mainGUI.backToMainMenu();
        });
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = GRIDBAG_WIDTH;
        centralButtons.add(ret, c);
        this.add(centralButtons);
        this.toResize.add(checkLFLabel);
        this.toResize.add(fontLabel);
        this.toResize.add(((DefaultEditor) fontSizeSelector.getEditor()).getTextField());
        this.toResize.add(checkInstantAnimationsLabel);
        this.toResize.add(languageLabel);
        this.toResize.add(languageComboBox);
        this.toResize.add(ret);
    }

    /**
     * Tries to set and apply all the settings saved with {@link InformationManager}.
     */
    public static void tryLoadSettings() {
        final List<Integer> settings = InformationManager.loadSettings();
        setUsingSystemLF(!settings.isEmpty() ? settings.get(0).equals(1) : isUsingSystemLF());
        setInstantTransitions(settings.size() >= 2 ? settings.get(1).equals(1) : areTransitionsInstant());
        setFontSize(
                settings.size() >= 3
                        ? settings.get(2) < MIN_FONT_SIZE ? MIN_FONT_SIZE
                                : settings.get(2) > MAX_FONT_SIZE ? MAX_FONT_SIZE : settings.get(2)
                        : getFontSize());
        Locale.setDefault(
                settings.size() >= 4 && settings.get(3) >= 0 && settings.get(3) < ResourceLoader.getLocales().length
                        ? ResourceLoader.getLocales()[settings.get(3)]
                        : Locale.getDefault());
    }

    private void saveSettings() {
        final List<Integer> settings = new LinkedList<>();
        settings.add(0, isUsingSystemLF() ? 1 : 0);
        settings.add(1, areTransitionsInstant() ? 1 : 0);
        settings.add(2, getFontSize());
        settings.add(3, Arrays.asList(ResourceLoader.getLocales()).indexOf(Locale.getDefault()));
        InformationManager.saveSettings(settings);
    }

    /**
     * @return the fontSize selected by user
     */
    public static synchronized int getFontSize() {
        return fontSize;
    }

    /**
     * @return true if transitions should be instantaneous
     */
    public static synchronized boolean areTransitionsInstant() {
        return instantTransitions;
    }

    @Override
    public void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("settings.background"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void addToCenter(final int anchor, final int gridx, final int gridy, final int gridwidth,
            final Component comp, final GridBagConstraints c, final Container dest) {
        c.anchor = anchor;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        dest.add(comp, c);
    }

    private static synchronized void setInstantTransitions(final boolean instantTransitions) {
        MenuSettings.instantTransitions = instantTransitions;
    }

    private static synchronized void setFontSize(final int value) {
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
        return new Font(c.getFont().getFontName(), c.getFont().getStyle(),
                MenuSettings.getFontSize() + (c instanceof JButton ? BUTTON_FONT_PLUS : 0));
    }

    private static boolean isUsingSystemLF() {
        return MenuSettings.usingSystemLF;
    }

    private static boolean setUsingSystemLF(final boolean usingSystemLF) {
        if (usingSystemLF) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                return false;
            }
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e1) {
                return false;
            }
        }
        MenuSettings.usingSystemLF = usingSystemLF;
        return true;
    }
}
