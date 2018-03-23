package view.swing.sandbox;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import controller.generation.GenerationController;
import controller.generation.GenerationControllerImpl;
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
    private static final int MAX_SPEED = 10;

    private final JSlider speedSlider;
    private final JButton bStart;
    private final JButton bEnd;
    private final JButton bPause;
    private final JButton bNext;
    private final JButton bGoTo;
    private final JButton bPrev;
    private final JButton bPlay;
    private final JButton bClear;
    private final JProgressBar progresBar;

    private final GenerationController generationController;
    private final Sandbox view;

    private final int fontSize = MenuSettings.getFontSize();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 
     * @param view the controller of the generation
     */
    public GenerationPanel(final Sandbox view) {
        this.view = view;
        this.generationController = new GenerationControllerImpl(view);

        bStart = SandboxTools.newJButton("Start", "Start the game mode");
        bEnd = SandboxTools.newJButton("End", "End the current game");
        bPause = SandboxTools.newJButton("Pause", "Stop the time");
        bNext = SandboxTools.newJButton("Next", "Go to the next generation");
        bGoTo = SandboxTools.newJButton("Go to", "Go at the indicated generations");
        bPrev = SandboxTools.newJButton("Previous", "Go to the previous generation");
        bPlay = SandboxTools.newJButton("Play", "Play the current game");
        bClear = SandboxTools.getClearButton();
        progresBar = new JProgressBar();
        progresBar.setIndeterminate(true);
        progresBar.setVisible(false);

        this.setLayout(new GridLayout(2, 2));
        final JPanel northL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel northR = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JPanel southL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel southR = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.add(northL);
        this.add(northR);
        this.add(southL);
        this.add(southR);

        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 100000, 10);
        final JSpinner spinner = new JSpinner(spin);

        northL.add(bStart);

        //speed control
        speedSlider = new JSlider(MIN_SPEED, MAX_SPEED, 1);
        speedSlider.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        northL.add(speedSlider);
        //add button to the layout
        southL.add(bPlay);
        southL.add(bPause);
        southL.add(bEnd);
        southR.add(bPrev);
        southR.add(bNext);
        northR.add(bGoTo);
        northR.add(progresBar);

        northR.add(spinner); //to use the go to button

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setFont(new Font(this.getFont().getFontName(), this.getFont().getStyle(), this.fontSize));

        //Start conditions.
        bStart.setEnabled(true);
        bPlay.setEnabled(false);
        bPause.setEnabled(false);
        bEnd.setEnabled(false);
        bClear.setEnabled(true);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);

        speedSlider.addChangeListener(e -> this.speedControl());
        bStart.addActionListener(e -> this.start());
        bEnd.addActionListener(e -> this.end());
        bPlay.addActionListener(e -> this.resume());
        bPause.addActionListener(e -> this.pause());
        bGoTo.addActionListener(e -> this.goTo(Long.parseLong(spinner.getValue().toString())));
        bPrev.addActionListener(e -> this.goTo(this.generationController.getCurrentNumberGeneration() - 1L));
        bNext.addActionListener(e -> this.goTo(this.generationController.getCurrentNumberGeneration() + 1L));
        bClear.addActionListener(e -> this.clear());
        final KeyListenerFactory keyFactory = new KeyListenerFactory(this.view);
        keyFactory.addKeyListener("space", KeyEvent.VK_SPACE, () -> {
            if (bStart.isEnabled()) {
                start();
            } else if (bPlay.isEnabled()) {
                resume();
            } else if (bPause.isEnabled()) {
                pause();
            }
        });
        keyFactory.addKeyListener("end", KeyEvent.VK_ESCAPE, () -> {
            if (bEnd.isEnabled()) {
                end();
            }
        });
        keyFactory.addKeyListener("clear", KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK, () -> {
            if (bClear.isEnabled()) {
                clear();
            }
        });
        keyFactory.addKeyListener("next", KeyEvent.VK_RIGHT, () -> {
            if (bNext.isEnabled()) {
                goTo(this.generationController.getCurrentNumberGeneration() + 1L);
            }
        });
        keyFactory.addKeyListener("previous", KeyEvent.VK_LEFT, () -> {
            if (bNext.isEnabled()) {
                goTo(this.generationController.getCurrentNumberGeneration() - 1L);
            }
        });
        keyFactory.addKeyListener("speedUp", KeyEvent.VK_UP, () -> speedSlider.setValue(speedSlider.getValue() + 1));
        keyFactory.addKeyListener("speedDown", KeyEvent.VK_DOWN, () -> speedSlider.setValue(speedSlider.getValue() - 1));
    }

    private void clear() {
        this.view.getGridEditor().killThemAll();
    }

    /**
     * 
     * @return the current JSlider value
     */
    public int getCurrentSpeed() {
        return this.speedSlider.getValue();
    }

    private void speedControl() {
        this.generationController.setSpeed(this.speedSlider.getValue());
    }

    private void goTo(final Long value) {
        if (value < 0) {
            JOptionPane.showMessageDialog(this, "Impossible undo to " + value + " from " + this.generationController.getCurrentNumberGeneration());
        } else if (!value.equals(this.generationController.getCurrentNumberGeneration())) {
            this.bPlay.setEnabled(false);
            this.bEnd.setEnabled(false);
            this.setTimeButtonEnable(false);

            this.bGoTo.setVisible(false);
            this.progresBar.setVisible(true);

            final FutureTask<Generation> fTask = new FutureTask<>(() -> {

                this.generationController.loadGeneration(value);

                try {
                    SwingUtilities.invokeAndWait(() -> {
                        this.progresBar.setVisible(false);
                        this.bGoTo.setVisible(true);
                        this.bPlay.setEnabled(true);
                        this.bEnd.setEnabled(true);
                        this.setTimeButtonEnable(true);
                        this.refreshView();
                    });
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException();
                } catch (InterruptedException e) {
                }
            }, null);
            executor.execute(fTask);
        }
    }

    private void end() {
        this.view.getGridEditor().setEnabled(true);
        this.view.getBookButton().setEnabled(true);
        bStart.setEnabled(true);
        bPlay.setEnabled(false);
        bPause.setEnabled(false);
        bEnd.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);
        this.bClear.setEnabled(true);
    }

    private void start() {
        this.view.getGridEditor().setEnabled(false);
        this.generationController.newGame();
        SandboxTools.getbSetView().setEnabled(false);
        this.view.getBookButton().setEnabled(false);
        this.bStart.setEnabled(false);
        this.bPause.setEnabled(false);
        this.bPlay.setEnabled(true);
        this.bEnd.setEnabled(true);
        this.setTimeButtonEnable(true);
        this.bClear.setEnabled(false);
    }

    private void resume() {
        this.generationController.play();
        this.bPause.setEnabled(true);
        this.bPlay.setEnabled(false);
        this.bEnd.setEnabled(false);
        this.setTimeButtonEnable(false);
    }

    private void pause() {
        this.generationController.pause();
        this.generationController.pause();
        this.bPause.setEnabled(false);
        this.bPlay.setEnabled(true);
        this.bEnd.setEnabled(true);
        this.setTimeButtonEnable(true);
    }

    private void setTimeButtonEnable(final boolean flag) {
        this.bNext.setEnabled(flag);
        this.bPrev.setEnabled(flag);
        this.bGoTo.setEnabled(flag);
    }

    /**
     * 
     */
    public void refreshView() {
        SandboxTools.refreshStatistics(
                this.getCurrentSpeed(),
                this.generationController.getCurrentNumberGeneration().intValue(),
                (int) this.generationController.getCurrentGeneration().getAliveMatrix().stream().filter(cell -> cell).count()
                );
        this.view.getGridEditor().draw(this.generationController.getCurrentGeneration());
    }
}
