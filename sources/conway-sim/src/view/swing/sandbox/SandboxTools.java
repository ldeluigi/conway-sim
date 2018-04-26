package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.metal.MetalButtonUI;

import controller.io.ResourceLoader;
import view.swing.menu.MenuSettings;

/**
 * An utility class for SandboxImpl.
 */
public final class SandboxTools {

    private static final int MAX_SIZE = 300;
    private static final int MIN_SIZE = 2;
    private static final int DEFAUL_SIZE = 100;

    private static final Color DISABLE_TEXT_COLOR = Color.LIGHT_GRAY;
    private static final String FONT_NAME = Font.MONOSPACED;
    private static final int FONT_STYLE = Font.PLAIN;

    private static final String NO_TOOLTIP = "none";

    private static JSpinner spinnerWidth;
    private static JSpinner spinnerHeight;

    private static JLabel numGenerationLabel;
    private static JLabel numSpeedLabel;
    private static JLabel aliveCell;

    private SandboxTools() {
    }

    /**
     * @param font
     *            the font
     * @return a panel with all the statistics of the game
     */
    public static JPanel newJPanelStatistics(final Font font) {
        final JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.setOpaque(false);
        // display for current speed
        numSpeedLabel = new JLabel(ResourceLoader.loadString("sandbox.label.speed") + "1");
        numSpeedLabel.setFont(font);
        // display for current generation
        numGenerationLabel = new JLabel(ResourceLoader.loadString("sandbox.label.generation") + "0");
        numGenerationLabel.setFont(font);
        // display the number of the alive cell
        aliveCell = new JLabel(ResourceLoader.loadString("sandbox.label.alivecell") + "0");
        aliveCell.setFont(font);
        statsPanel.add(numGenerationLabel);
        statsPanel.add(numSpeedLabel);
        statsPanel.add(aliveCell);
        return statsPanel;
    }

    /**
     * @param speedSlider
     *            the current speed
     * @param genNumber
     *            the current genNumber
     * @param aliveCell
     *            the current number of alive cell
     * @param font
     *            the new font
     */
    public static void refreshStatistics(final int speedSlider, final int genNumber, final int aliveCell,
            final Font font) {
        SandboxTools.numGenerationLabel
                .setText(ResourceLoader.loadString("sandbox.label.generation") + genNumber);
        SandboxTools.numSpeedLabel.setText(ResourceLoader.loadString("sandbox.label.speed") + speedSlider);
        SandboxTools.aliveCell.setText(ResourceLoader.loadString("sandbox.label.alivecell") + aliveCell);
        SandboxTools.numGenerationLabel.setFont(font);
        SandboxTools.numSpeedLabel.setFont(font);
        SandboxTools.aliveCell.setFont(font);
    }

    /**
     * @param sandboxImpl
     *            a
     * @param bApply
     *            a
     * @param font
     *            the font
     * @return a JPanel
     */
    public static JPanel newGridOptionDimension(final AbstractSandbox sandboxImpl, final JButton bApply,
            final Font font) {
        final JPanel gridOption = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        gridOption.setOpaque(false);
        gridOption.setFont(font);
        final JLabel gridText = new JLabel(ResourceLoader.loadString("simpleSandbox.resize"));
        gridText.setFont(font);
        // name
        c.gridx = 0;
        c.gridy = 0;
        gridOption.add(gridText, c);
        // button apply
        c.gridy = 0;
        c.gridx = 1;
        c.gridwidth = 3;
        gridOption.add(bApply, c);
        c.gridwidth = 1;

        // second line Dimension : X x Y
        final JLabel labelDimension = new JLabel(ResourceLoader.loadString("simpleSandbox.dimension"));
        labelDimension.setFont(font);
        c.gridx = 0;
        c.gridy = 1;
        gridOption.add(labelDimension, c);

        spinnerWidth = new JSpinner(new SpinnerNumberModel(DEFAUL_SIZE, MIN_SIZE, MAX_SIZE, 1));
        spinnerWidth.setFont(font);
        c.gridx = 1;
        c.gridy = 1;
        gridOption.add(spinnerWidth, c);

        spinnerHeight = new JSpinner(new SpinnerNumberModel(DEFAUL_SIZE, MIN_SIZE, MAX_SIZE, 1));
        spinnerHeight.setFont(font);

        final JLabel division = new JLabel(" x ");
        division.setFont(font);
        c.gridx = 2;
        c.gridy = 1;
        gridOption.add(division, c);

        c.gridx = 3;
        c.gridy = 1;
        gridOption.add(spinnerHeight, c);

        bApply.addActionListener(e -> {
            if (Integer.valueOf(spinnerHeight.getValue().toString()) >= MIN_SIZE
                    && Integer.valueOf(spinnerHeight.getValue().toString()) <= MAX_SIZE
                    && Integer.valueOf(spinnerWidth.getValue().toString()) >= MIN_SIZE
                    && Integer.valueOf(spinnerWidth.getValue().toString()) <= MAX_SIZE) {
                sandboxImpl.resetGrid();
            }
        });
        return gridOption;
    }

    /**
     * @return an int that represent the height value of the grid.
     */
    public static int getHeightSelect() {
        return (int) spinnerHeight.getValue();
    }

    /**
     * @return an int that represent the width value of the grid.
     */
    public static int getWidthSelect() {
        return (int) spinnerWidth.getValue();
    }

    /**
     * @param text
     *            JButton text
     * @return a new JButton
     */
    public static JButton newJButton(final String text) {
        return newJButton(text, NO_TOOLTIP);
    }

    /**
     * @param text
     *            the text of the button
     * @param tooltipText
     *            the tool tip of the button
     * @return a new button
     */
    public static JButton newJButton(final String text, final String tooltipText) {
        final Font font = new Font(FONT_NAME, FONT_STYLE, MenuSettings.getFontSize());
        return newJButton(text, tooltipText, font);
    }

    /**
     * Create a new JButton with the specific font.
     * 
     * @param text
     *            JButton text
     * @param font
     *            the specific font
     * @return a new JButton
     */
    public static JButton newJButton(final String text, final Font font) {
        return newJButton(text, NO_TOOLTIP, font);
    }

    /**
     * Create a new JButton with the specific font.
     * 
     * @param text
     *            JButton text
     * @param tooltipText
     *            the tool tip text
     * @param font
     *            the specific font
     * @return a new JButton
     */
    public static JButton newJButton(final String text, final String tooltipText, final Font font) {
        final JButton button = new JButton(text);

        final FontMetrics metrics = button.getFontMetrics(font);
        final int width = Toolkit.getDefaultToolkit().getScreenSize().width / 12;
        final int height = metrics.getHeight();
        final Dimension newDimension = new Dimension(width, height);
        button.setPreferredSize(newDimension);
        button.setMaximumSize(newDimension);

        button.setFont(font);
        if (!tooltipText.equals(NO_TOOLTIP)) {
            button.setToolTipText(tooltipText);
        }
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
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
     * This method apply the default icon to the button,
     * To have expected result, set the preferred size of the button to dim.
     * @param button
     *            the button
     * @param dim
     *            the button dimension
     */
    public static void setIcon(final JButton button, final Dimension dim) {
        button.setIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.on").getScaledInstance(dim.width,
                dim.height, Image.SCALE_SMOOTH)));
        button.setDisabledIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.off").getScaledInstance(dim.width,
                dim.height, Image.SCALE_SMOOTH)));
        button.setPressedIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.pressed")
                .getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH)));
    }
}
