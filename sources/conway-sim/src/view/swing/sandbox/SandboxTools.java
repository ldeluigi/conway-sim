package view.swing.sandbox;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import view.swing.menu.MenuSettings;

/**
 * An utility class for Sandbox.
 */
//TODO make class static-final
public final class SandboxTools {

    private static JSpinner spinnerWidth;
    private static JSpinner spinnerHeight;

    private static JButton bApply;
    private static JButton bClear;


    private static JLabel numGenerationLabel;
    private static JLabel numSpeedLabel;
    private static JLabel aliveCell;

    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize());

    private SandboxTools() { }

    /**
     * 
     * @return a panel with all the statistics of the game
     */
    public static JPanel newJPanelStatistics() {
        final JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        // display for current speed
        numSpeedLabel = new JLabel("|Speed value " + 1);
        numSpeedLabel.setFont(SandboxTools.FONT);
        // display for current generation
        numGenerationLabel = new JLabel("|Current generation number:" + "0");
        numGenerationLabel.setFont(SandboxTools.FONT);
        // display the number of the alive cell
        aliveCell = new JLabel("|Alive cell " + "0");
        aliveCell.setFont(SandboxTools.FONT);
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
     */
    public static void refreshStatistics(final int speedSlider, final int genNumber, final int aliveCell) {
        SandboxTools.numGenerationLabel.setText("|Current generation number:" + genNumber);
        SandboxTools.numSpeedLabel.setText("|Speed value " + speedSlider);
        SandboxTools.aliveCell.setText("|Alive cell " + aliveCell);
    }

    /**
     * 
     * @param sandbox the sandbox
     * @return a JPanel with the grid dimension option.
     */
    public static JPanel newGridOptionDimension(final Sandbox sandbox) {
        final JPanel gridOption = new JPanel(new GridLayout(2, 1));
        final JPanel topGrid = new JPanel(new FlowLayout());
        final JPanel bottomGrid = new JPanel(new FlowLayout());
        gridOption.setFont(SandboxTools.FONT);
        bApply = SandboxTools.newJButton("Apply", "Change the grid dimension");
        bApply.setFont(SandboxTools.FONT);
        final JLabel gridText = new JLabel("Grid dimension ");
        gridText.setFont(SandboxTools.FONT);
        topGrid.add(gridText);
        topGrid.add(bApply);
        gridOption.add(topGrid);
        spinnerWidth = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 1));
        spinnerWidth.setFont(SandboxTools.FONT);
        final JLabel labelWidth = new JLabel("Dimension");
        labelWidth.setFont(SandboxTools.FONT);
        bottomGrid.add(labelWidth);
        bottomGrid.add(spinnerWidth);
        spinnerHeight = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 1));
        spinnerHeight.setFont(SandboxTools.FONT);
        final JLabel labelHeight = new JLabel(" x ");
        labelHeight.setFont(SandboxTools.FONT);
        bottomGrid.add(labelHeight);
        bottomGrid.add(spinnerHeight);
        gridOption.add(bottomGrid);
        bApply.addActionListener(e -> {
        });
        return gridOption;
    }

    /**
     * 
     * @return the Clear JButton
     */
    public static JButton getClearButton() {
        if (Objects.isNull(bClear)) {
            bClear = newJButton("Clear", "Clear the grid");
        }
        return bClear;
    }

    /**
     * 
     * @return the setView button
     */
    public static JButton getbSetView() {
        return bApply;
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
        return button;
    }
}
