package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.metal.MetalButtonUI;

import controller.io.ResourceLoader;
import view.swing.menu.MenuSettings;

/**
 * An utility class for Sandbox.
 */
public final class SandboxTools {

    private static final int MAX_SIZE = 300;
    private static final int MIN_SIZE = 2;
    private static final int DEFAUL_SIZE = 100;

    private static JSpinner spinnerWidth;
    private static JSpinner spinnerHeight;

    private static final Icon BUTTON_ON = new ImageIcon(ResourceLoader.loadImage("sandbox.button.on"));

    private static JLabel numGenerationLabel;
    private static JLabel numSpeedLabel;
    private static JLabel aliveCell;

    private SandboxTools() { }

    /**
     * @param font the font
     * @return a panel with all the statistics of the game
     */
    public static JPanel newJPanelStatistics(final Font font) {
        final JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        statsPanel.setOpaque(false);
        // display for current speed
        numSpeedLabel = new JLabel(ResourceLoader.loadString("sandbox.label.speed") + "1" + "|");
        numSpeedLabel.setFont(font);
        // display for current generation
        numGenerationLabel = new JLabel(ResourceLoader.loadString("sandbox.label.generation") + "0" + "|");
        numGenerationLabel.setFont(font);
        // display the number of the alive cell
        aliveCell = new JLabel(ResourceLoader.loadString("sandbox.label.alivecell") + "0" + "|");
        aliveCell.setFont(font);
        statsPanel.add(numGenerationLabel);
        statsPanel.add(numSpeedLabel);
        statsPanel.add(aliveCell);
        return statsPanel;
    }

    /**
     * 
     * @param speedSlider the current speed
     * @param genNumber the current genNumber
     * @param aliveCell the current number of alive cell
     * @param font the new font
     */
    public static void refreshStatistics(final int speedSlider, final int genNumber, final int aliveCell, final Font font) {
        SandboxTools.numGenerationLabel.setText(ResourceLoader.loadString("sandbox.label.generation") + genNumber + "|");
        SandboxTools.numSpeedLabel.setText(ResourceLoader.loadString("sandbox.label.speed") + speedSlider + "|");
        SandboxTools.aliveCell.setText(ResourceLoader.loadString("sandbox.label.alivecell") + aliveCell + "|");
        SandboxTools.numGenerationLabel.setFont(font);
        SandboxTools.numSpeedLabel.setFont(font);
        SandboxTools.aliveCell.setFont(font);
    }

    /**
     * 
     * @param sandbox a
     * @param bApply a
     * @param font the font
     * @return a JPanel
     */
    public static JPanel newGridOptionDimension(final Sandbox sandbox, final JButton bApply, final Font font) {
        final JPanel gridOption = new JPanel(new GridLayout(2, 1));
        gridOption.setOpaque(false);
        final JPanel topGrid = new JPanel(new FlowLayout());
        topGrid.setOpaque(false);
        final JPanel bottomGrid = new JPanel(new FlowLayout());
        bottomGrid.setOpaque(false);
        gridOption.setFont(font);
        final JLabel gridText = new JLabel("Grid option ");
        gridText.setFont(font);
        topGrid.add(gridText);
        topGrid.add(bApply);
        gridOption.add(topGrid);
        spinnerWidth = new JSpinner(new SpinnerNumberModel(DEFAUL_SIZE, MIN_SIZE, MAX_SIZE, 1));
        spinnerWidth.setFont(font);
        final JLabel labelDimension = new JLabel("Dimension ");
        labelDimension.setFont(font);
        bottomGrid.add(labelDimension);
        bottomGrid.add(spinnerWidth);
        spinnerHeight = new JSpinner(new SpinnerNumberModel(DEFAUL_SIZE, MIN_SIZE, MAX_SIZE, 1));
        spinnerHeight.setFont(font);
        final JLabel division = new JLabel(" x ");
        division.setFont(font);
        bottomGrid.add(division);
        bottomGrid.add(spinnerHeight);
        gridOption.add(bottomGrid);
        bApply.addActionListener(e -> {
            if (Integer.valueOf(spinnerHeight.getValue().toString()) >= MIN_SIZE
                    && Integer.valueOf(spinnerHeight.getValue().toString()) <= MAX_SIZE
                    && Integer.valueOf(spinnerWidth.getValue().toString()) >= MIN_SIZE
                    && Integer.valueOf(spinnerWidth.getValue().toString()) <= MAX_SIZE) {
                sandbox.resetGrid();
            }
        });
        return gridOption;
    }

    /**
     * 
     * @return an int that represent the height value of the grid.
     */
    public static int getHeightSelect() {
        return (int) spinnerHeight.getValue();
    }

    /**
     * 
     * @return an int that represent the width value of the grid.
     */
    public static int getWidthSelect() {
        return (int) spinnerWidth.getValue();
    }

    /**
     * 
     * @param name the name of the button
     * @param tooltipText the tool tip of the button
     * @return a new button
     */
    public static JButton newJButton(final String name, final String tooltipText) {
        final JButton button = new JButton(name);
        button.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        button.setToolTipText(tooltipText);
        button.setFocusPainted(false);
//        button.setPreferredSize(new Dimension(120, 80));
//        button.setDisabledIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.off").getScaledInstance(120, 80, Image.SCALE_DEFAULT)));
//        button.setIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.on").getScaledInstance(120, 80, Image.SCALE_DEFAULT)));
//        button.setIconTextGap(-100);
        setEnabledBackgroundAndBorder(button);
        button.setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return Color.WHITE;
            }
        });
        button.addChangeListener(e -> {
            if (button.isEnabled()) {
                setEnabledBackgroundAndBorder(button);
            } else {
                setDisabledBackgroundAndBorder(button);
            }
        });
        return button;
    }

    /**
     * Sets default background and border of a button.
     * @param button to edit
     */
    public static void setEnabledBackgroundAndBorder(final JButton button) {
        setBackgroundAndBorder(button, Color.WHITE, Color.GRAY, Color.BLACK);
    }

    /**
     * Sets default background and border of a button.
     * @param button to edit
     */
    public static void setDisabledBackgroundAndBorder(final JButton button) {
        setBackgroundAndBorder(button, Color.LIGHT_GRAY, Color.GRAY, Color.BLACK);
    }

    /**
     * Sets background and border of a button.
     * @param button to edit
     * @param background color
     * @param border color
     * @param forground color
     */
    public static void setBackgroundAndBorder(final JButton button, final Color background, final Color border, final Color forground) {
        button.setBackground(background);
        button.setBorder(BorderFactory.createLineBorder(border, 2, false));
        button.setForeground(forground);
    }

    /**
     * 
     * @param name of the button
     * @param tooltipText of the button
     * @param background color
     * @param border color
     * @param forground color
     * @return a new JButton
     */
    public static JButton newJButtonWithBackgroundAndBorder(final String name, final String tooltipText, final Color background, final Color border, final Color forground) {
        final JButton newButton = newJButton(name, tooltipText);
        setBackgroundAndBorder(newButton, background, border, forground);
        return newButton;
    }
}
