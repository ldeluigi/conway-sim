package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import controller.generation.GenerationController;
import core.model.Generation;
import view.swing.menu.MenuSettings;

/**
 * This is the panel that contain all the Generation control for the application.
 */
public class GenerationPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9060069868596999045L;

    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 20;

    private final JSlider speedSlider;
    private final JButton bNew;
    private final JButton bEnd;
    private final JButton bPause;
    private final JButton bNext;
    private final JButton bGoTo;
    private final JButton bPrev;
    private final JButton bPlay;

    private final JLabel numGeneration;
    private final GenerationController generationController;

    private final int fontSize = MenuSettings.getFontSize();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 
     * @param controller the controller of the generation
     */
    public GenerationPanel(final GenerationController controller) {
        this.generationController = controller;

        bNew = this.newJButton("New", "New game");
        bEnd = this.newJButton("End", "End the current game");
        bPause = this.newJButton("Pause", "Stop the time");
        bNext = this.newJButton("Next", "Go to the next generation");
        bGoTo = this.newJButton("Go to", "Go back in time to N generations");
        bPrev = this.newJButton("Previous", "Go to the previous generation");
        bPlay = this.newJButton("Play", "Play the current game");

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 100000, 10);
        final JSpinner spinner = new JSpinner(spin);

        this.add(bNew);

        //speed control
        speedSlider = new JSlider(MIN_SPEED, MAX_SPEED, 1);
        speedSlider.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        this.add(speedSlider);

        //display for current generation
        final JLabel generationNumber = new JLabel("Generation number: ");
        generationNumber.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        numGeneration = new JLabel("0");
        numGeneration.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        this.add(generationNumber);
        this.add(numGeneration);

        this.add(bPlay);
        this.add(bPause);
        this.add(bEnd);
        this.add(bPrev);
        this.add(bNext);
        this.add(bGoTo);

        this.add(spinner);

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setFont(new Font(this.getFont().getFontName(), this.getFont().getStyle(), this.fontSize));

        /*
         * Start conditions.
         */
        bNew.setEnabled(true);
        bPlay.setEnabled(false);
        bPause.setEnabled(false);
        bEnd.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);

        this.setVisible(true);

        speedSlider.addChangeListener(e -> this.speedControl());
        bNew.addActionListener(e -> this.start());
        bEnd.addActionListener(e -> this.end());
        bPlay.addActionListener(e -> this.resume());
        bPause.addActionListener(e -> this.pause());
        bGoTo.addActionListener(e -> this.goTo(Long.parseLong(spinner.getValue().toString())));
        bPrev.addActionListener(e -> this.goTo(this.generationController.getCurrentNumberGeneration() - 1L));
        bNext.addActionListener(e -> this.next());

    }

    private void speedControl() {
        this.generationController.setSpeed(this.speedSlider.getValue());
    }

    private void next() {
        this.generationController.computeNextGeneration();
    }

    private void goTo(final Long value) {
        if (value < 0) {
            JOptionPane.showMessageDialog(this, "Impossible undo to " + value + " from " + this.generationController.getCurrentNumberGeneration());
        } else {
            final FutureTask<Generation> fTask = new FutureTask<>(() -> {
                bPlay.setEnabled(false);
                bEnd.setEnabled(false);
                this.setEableTimeOption(false);
                final JProgressBar pb = new JProgressBar();
                pb.setIndeterminate(true);
                final JPopupMenu menu = new JPopupMenu();
                menu.add(pb);
                pb.setComponentPopupMenu(menu);
                final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                menu.setLocation((int) dim.getWidth() / 2, (int) dim.getHeight() / 2);
                menu.setVisible(true);
                generationController.loadOldGeneration(value);
                menu.setVisible(false);
                this.setEableTimeOption(true);
                bPlay.setEnabled(true);
                bEnd.setEnabled(true);
                this.refreshView();
            }, null);
            executor.execute(fTask);
        }
    }

    private void end() {
        bNew.setEnabled(true);
        bPlay.setEnabled(false);
        bPause.setEnabled(false);
        bEnd.setEnabled(false);
        this.setEableTimeOption(false);
        this.generationController.reset();
    }

    private void start() {
        this.generationController.newGame();
        this.bNew.setEnabled(false);
        this.bPause.setEnabled(false);
        bPlay.setEnabled(true);
        bEnd.setEnabled(true);
        this.setEableTimeOption(true);
    }

    private void resume() {
        this.generationController.play();
        this.bPause.setEnabled(true);
        bPlay.setEnabled(false);
        bEnd.setEnabled(false);
        this.setEableTimeOption(false);
    }

    private void pause() {
        this.generationController.pause();
        this.generationController.pause();
        this.bPause.setEnabled(false);
        this.bPlay.setEnabled(true);
        this.bEnd.setEnabled(true);
        this.setEableTimeOption(true);
    }

    private void setEableTimeOption(final boolean flag) {
        this.bNext.setEnabled(flag);
        this.bPrev.setEnabled(flag);
        this.bGoTo.setEnabled(flag);
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
