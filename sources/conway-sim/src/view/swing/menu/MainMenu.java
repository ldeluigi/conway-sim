package view.swing.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import controller.io.ResourceLoader;
import view.swing.DesktopGUI;
import view.swing.sandbox.SandboxBuilder;

/**
 * This class displays the main menu. Pattern: Singleton.
 */
public final class MainMenu extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int BUTTON_TEXT_PLUS = 25;
    private static final int MINOR_BUTTON_TEXT_PLUS = 15;
    private static final int BUTTON_RATIO_Y = 10;
    private static final int BUTTON_RATIO_X = 5;
    private static final int MINOR_BUTTON_RATIO_X = 6;
    private static final int HORIZONTAL_TITLE_RATIO = 8;
    private static final int UPPER_TITLE_SPACE_RATIO = 4;
    private static final int TITLE_HEIGHT_RATIO = 6;

    /**
     * The constructor fills the panel.
     * 
     * @param mainGUI
     *            the main GUI
     */
    public MainMenu(final DesktopGUI mainGUI) {
        UIManager.put("Button.select", Color.LIGHT_GRAY);
        this.setLayout(new BorderLayout());
        final JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        this.add(center, BorderLayout.CENTER);
        final JLabel spaceForTitle = new JLabel();
        spaceForTitle.setBorder(new EmptyBorder((mainGUI.getCurrentHeight() * 2) / BUTTON_RATIO_Y, 0, 0, 0));
        this.add(spaceForTitle, BorderLayout.NORTH);
        final JPanel centralButtons = new JPanel(new GridBagLayout());
        centralButtons.setOpaque(false);
        final JButton sandbox = new JButton(ResourceLoader.loadString("main.sandbox"));
        setBackgroundAndBorder(sandbox);
        sandbox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() + BUTTON_TEXT_PLUS));
        sandbox.setPreferredSize(
                new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        sandbox.addActionListener(e -> {
            mainGUI.setView(new LoadingScreen());
            SwingUtilities.invokeLater(() -> {
                mainGUI.setView(SandboxBuilder.buildSandbox(mainGUI));
            });
        });
        sandbox.setToolTipText(ResourceLoader.loadString("main.tooltip.sandbox"));
        sandbox.setFocusPainted(false);
        final JButton levels = new JButton(ResourceLoader.loadString("main.levels"));
        setBackgroundAndBorder(levels);
        levels.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() + BUTTON_TEXT_PLUS));
        levels.setPreferredSize(
                new Dimension(mainGUI.getCurrentWidth() / BUTTON_RATIO_X, mainGUI.getCurrentHeight() / BUTTON_RATIO_Y));
        levels.addActionListener(e -> {
            mainGUI.setView(new LoadingScreen());
            SwingUtilities.invokeLater(() -> {
                mainGUI.setView(new LevelMenu(mainGUI));
            });
        });
        levels.setToolTipText(ResourceLoader.loadString("main.tooltip.levels"));
        levels.setFocusPainted(false);
        final JButton exit = new JButton(ResourceLoader.loadString("main.exit"));
        setBackgroundAndBorder(exit);
        final Dimension bottomCoupleDimension = new Dimension(mainGUI.getCurrentWidth() / MINOR_BUTTON_RATIO_X,
                mainGUI.getCurrentHeight() / (BUTTON_RATIO_Y * 2));
        exit.setPreferredSize(bottomCoupleDimension);
        exit.addActionListener(e -> {
            mainGUI.close();
        });
        exit.setFocusPainted(false);
        exit.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() + MINOR_BUTTON_TEXT_PLUS));
        final JButton settings = new JButton(ResourceLoader.loadString("main.settings"));
        setBackgroundAndBorder(settings);
        settings.setPreferredSize(bottomCoupleDimension);
        settings.addActionListener(e -> {
            mainGUI.setView(new MenuSettings(mainGUI));
        });
        settings.setFocusPainted(false);
        settings.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() + MINOR_BUTTON_TEXT_PLUS));
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(mainGUI.getCurrentHeight() / (BUTTON_RATIO_Y * BUTTON_RATIO_Y), 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        centralButtons.add(levels, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        centralButtons.add(sandbox, c);
        c.anchor = GridBagConstraints.EAST;
        c.insets.set(c.insets.top, 0, 0, mainGUI.getCurrentHeight() / (BUTTON_RATIO_Y * BUTTON_RATIO_Y));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        centralButtons.add(settings, c);
        c.anchor = GridBagConstraints.WEST;
        c.insets.set(c.insets.top, mainGUI.getCurrentHeight() / (BUTTON_RATIO_Y * BUTTON_RATIO_Y), 0, 0);
        c.gridx = 1;
        c.gridy = 2;
        centralButtons.add(exit, c);
        center.add(centralButtons);
        final JPanel lowerPanel = new JPanel();
        lowerPanel.setOpaque(false);
        lowerPanel.setLayout(new BorderLayout());
        final JLabel version = new JLabel(ResourceLoader.loadString("main.version"));
        final JLabel author = new JLabel(ResourceLoader.loadString("main.author"));
        version.setFont(
                new Font(version.getFont().getFontName(), version.getFont().getStyle(), MenuSettings.getFontSize()));
        author.setFont(
                new Font(author.getFont().getFontName(), author.getFont().getStyle(), MenuSettings.getFontSize()));
        lowerPanel.add(version, BorderLayout.EAST);
        lowerPanel.add(author, BorderLayout.WEST);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("main.background"), 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawImage(ResourceLoader.loadImage("main.title"), this.getWidth() / HORIZONTAL_TITLE_RATIO,
                this.getHeight() / UPPER_TITLE_SPACE_RATIO,
                this.getWidth() * (HORIZONTAL_TITLE_RATIO - 2) / HORIZONTAL_TITLE_RATIO,
                this.getHeight() / TITLE_HEIGHT_RATIO, this);
    }

    private void setBackgroundAndBorder(final JButton button) {
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, false));
        button.setForeground(Color.BLACK);
    }
}
