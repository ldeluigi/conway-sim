package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
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

    private static final Color BORDERD_COLOR = Color.BLACK;
    private static final Color DISABLE_TEXT_COLOR = Color.LIGHT_GRAY;
    private static final String FONT_NAME = Font.MONOSPACED;
    private static final int FONT_STYLE = Font.PLAIN;

    private static final int BUTTON_TEXT_SIZE_RAPPOR = 12 / 10;

    private static JSpinner spinnerWidth;
    private static JSpinner spinnerHeight;

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
        final Font font = new Font(FONT_NAME, FONT_STYLE, MenuSettings.getFontSize());

        final FontMetrics metrics = button.getFontMetrics(font); 
        final int width = metrics.stringWidth(name + " ");
        final int height = metrics.getHeight();
        final Dimension newDimension =  new Dimension(width * BUTTON_TEXT_SIZE_RAPPOR, height * BUTTON_TEXT_SIZE_RAPPOR);
        button.setPreferredSize(newDimension);

        button.setFont(font);
        button.setToolTipText(tooltipText);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BORDERD_COLOR, 2, false));
        button.setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return DISABLE_TEXT_COLOR;
            }
        });
        setIcon(button, newDimension);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }

    /**
     * 
     * @param name button name
     * @return a new JButton
     */
    public static JButton newJButton(final String name) {
        return newJButton(name, " ");
    }

    /**
     * @param name radio button name
     * @param dimension the button default dimension
     * @return the button
     */
    public static JRadioButton newJRadioButton(final String name, final Dimension dimension) {
        final JRadioButton rButton = new JRadioButton(name);
        rButton.setFocusPainted(false);
        rButton.setPreferredSize(dimension);
        return rButton;
    }

    /**
     * 
     * @param button the button
     * @param dim the button dimension
     */
    public static void setIcon(final AbstractButton button, final Dimension dim) {
        button.setIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.on").getScaledInstance(
                dim.width, dim.height, Image.SCALE_SMOOTH)));
        button.setDisabledIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.off").getScaledInstance(
                dim.width, dim.height, Image.SCALE_SMOOTH)));
        button.setPressedIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.pressed").getScaledInstance(
                dim.width, dim.height, Image.SCALE_SMOOTH)));
    }

}
