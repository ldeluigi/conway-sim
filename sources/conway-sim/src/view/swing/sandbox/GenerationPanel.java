package view.swing.sandbox;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import controller.generation.GenerationController;
import view.swing.menu.MenuSettings;

/**
 * This is the panel that contain all the Generation control for the application.
 */
public class GenerationPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9060069868596999045L;

    private final JButton bStart;
    private final JButton bStop;
    private final JButton bPause;
    private final JButton bNext;
    private final JButton bGoTo;
    private final JButton bPrev;
    private final JButton bRes;

    private final JLabel numGeneration;
    private GenerationController generationController;

    private final int fontSize = MenuSettings.getFontSize();

    /**
     * 
     * @param controller the controller of the generation
     */
    public GenerationPanel(final GenerationController controller) {
        this.generationController = controller;

        bStart = this.newJButton("Start", "Start the game");
        bStop = this.newJButton("Stop", "Reset the game mode");
        bPause = this.newJButton("Pause", "Stop the time");
        bNext = this.newJButton("Next", "Go to the next generation");
        bGoTo = this.newJButton("Go to", "Go back in time to N generations");
        bPrev = this.newJButton("Previous", "Go to the previous generation");
        bRes = this.newJButton("Resume", "Resume the current game");

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 100000, 10);
        final JSpinner spinner = new JSpinner(spin);

        this.add(bStart);

        final JLabel generationNumber = new JLabel("Generation number: ");
        generationNumber.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        numGeneration = new JLabel("0");
        numGeneration.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        this.add(generationNumber);
        this.add(numGeneration);

        this.add(bPause);
        this.add(bRes);
        this.add(bStop);
        this.add(bPrev);
        this.add(bNext);
        this.add(bGoTo);

        this.add(spinner);

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setFont(new Font(this.getFont().getFontName(), this.getFont().getStyle(), this.fontSize));

        /*
         * Start conditions.
         */
        bStart.setEnabled(true);
        bRes.setEnabled(false);
        bPause.setEnabled(false);
        bStop.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);

        this.setVisible(true);

        bStart.addActionListener(e -> this.start());
        bStop.addActionListener(e -> this.stop());
        bRes.addActionListener(e -> this.resume());
        bPause.addActionListener(e -> this.pause());
        bGoTo.addActionListener(e -> this.goTo(Long.parseLong(spinner.getValue().toString())));
        bPrev.addActionListener(e -> this.goTo(1L));
        bNext.addActionListener(e -> this.next());
    }

    private void next() {
        this.generationController.computeNextGeneration();
    }

    private void goTo(final Long value) {
        if (value < 0) {
            JOptionPane.showMessageDialog(this, "Impossible undo to " + value + " from " + this.generationController.getCurrentNumberGeneration());
        } else {
            this.generationController.loadOldGeneration(value);
            this.refreshView();
        }
    }

    private void stop() {
        if (JOptionPane.showOptionDialog(this, "Stop", "Stop this game?", JOptionPane.YES_NO_OPTION,  JOptionPane.INFORMATION_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
            bStart.setEnabled(true);
            bRes.setEnabled(false);
            bPause.setEnabled(false);
            bStop.setEnabled(false);
            bNext.setEnabled(false);
            bPrev.setEnabled(false);
            bGoTo.setEnabled(false);
            this.generationController.reset();
        }
    }

    private void start() {
        this.generationController.startGameWithGeneration();
        this.bStart.setEnabled(false);
        this.bPause.setEnabled(true);
        bRes.setEnabled(false);
        bStop.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);
    }

    private void resume() {
        this.generationController.resume();
        this.bPause.setEnabled(true);
        bRes.setEnabled(false);
        bStop.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);
    }

    private void pause() {
        this.generationController.pause();
        this.generationController.pause();
        this.bPause.setEnabled(false);
        this.bRes.setEnabled(true);
        this.bNext.setEnabled(true);
        this.bPrev.setEnabled(true);
        this.bGoTo.setEnabled(true);
        this.bStop.setEnabled(true);
    }

    /**
     * 
     */
    public void refreshView() {
        this.numGeneration.setText(this.generationController.getCurrentNumberGeneration().toString());
    }

    private JButton newJButton(final String name, final String tooltipText) {
        final JButton button = new JButton(name);
        button.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        button.setToolTipText(tooltipText);
        button.setFocusPainted(false);
        return button;
    }
}
