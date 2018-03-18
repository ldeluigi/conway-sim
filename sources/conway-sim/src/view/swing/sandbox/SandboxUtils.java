/**
 * 
 */
package view.swing.sandbox;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * An utility class for Sandbox.
 */
public class SandboxUtils {

    private JSpinner spinnerWidth;
    private JSpinner spinnerHeight;

    private JLabel numGenerationLabel;
    private JLabel numSpeedLabel;
    private JLabel aliveCell;

    private final Font font;

    /**
     * 
     * @param font the font for all the panel
     */
    public SandboxUtils(final Font font) {
        this.font = font;
    }

    /**
     * 
     * @return a panel with all the statistics of the game
     */
    public JPanel newJPanelStatistics() {
        final JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        // display for current speed
        numSpeedLabel = new JLabel("|Speed value " + 1);
        numSpeedLabel.setFont(this.font);
        // display for current generation
        numGenerationLabel = new JLabel("|Current generation number:" + "0");
        numGenerationLabel.setFont(this.font);
        // display the number of the alive cell
        aliveCell = new JLabel("|Alive cell " + "0");
        aliveCell.setFont(this.font);
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
    public void refreshStatistics(final int speedSlider, final int genNumber, final int aliveCell) {
        this.numGenerationLabel.setText("|Current generation number:" + genNumber);
        this.numSpeedLabel.setText("|Speed value " + speedSlider);
        this.aliveCell.setText("|Alive cell " + aliveCell);
    }

    /**
     * 
     * @return a JPanel with the grid dimension option.
     */
    public JPanel newGridOptionDimension() {
        final JPanel gridOption = new JPanel(new GridLayout(2, 1));
        final JPanel topGrid = new JPanel(new FlowLayout());
        final JPanel bottomGrid = new JPanel(new FlowLayout());
        gridOption.setFont(this.font);
        final JButton bSetView = new JButton("Apply");
        topGrid.add(new JLabel("Grid dimension "));
        topGrid.add(bSetView);
        gridOption.add(topGrid);
        spinnerWidth = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 1));
        bottomGrid.add(spinnerWidth);
        spinnerHeight = new JSpinner(new SpinnerNumberModel(100, 0, 1000, 1));
        bottomGrid.add(spinnerHeight);
        gridOption.add(bottomGrid);
        return gridOption;
    }

    /**
     * 
     * @return an int that represent the height value of the grid.
     */
    public int getHeightSelect() {
        return (int) spinnerHeight.getValue();
    }

    /**
     * 
     * @return an int that represent the width value of the grid.
     */
    public int getWidthSelect() {
        return (int) spinnerWidth.getValue();
    }
}
