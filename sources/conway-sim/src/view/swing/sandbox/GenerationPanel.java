package view.swing.sandbox;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

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
import controller.io.ResourceLoader;
import core.model.Generation;
import core.model.Status;
import view.swing.menu.MenuSettings;

/**
 * This is the panel that contain all the Generation control for the application.
 */
public class GenerationPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9060069868596999045L;

    private static final int MIN_SPEED = ResourceLoader.loadConstantInt("generation.MIN_SPEED");
    private static final int MAX_SPEED = ResourceLoader.loadConstantInt("generation.MAX_SPEED");

    private final JSlider speedSlider;
    private final JButton bStart;
    private final JButton bEnd;
    private final JButton bPause;
    private final JButton bNext;
    private final JButton bGoTo;
    private final JButton bPrev;
    private final JButton bPlay;
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
        this.setOpaque(false);
        this.generationController = new GenerationControllerImpl(view);

        bStart = SandboxTools.newJButton(ResourceLoader.loadString("generation.start"), ResourceLoader.loadString("generation.start.tooltip"));
        bEnd = SandboxTools.newJButton(ResourceLoader.loadString("generation.end"), ResourceLoader.loadString("generation.end.tooltip"));
        bPause = SandboxTools.newJButton(ResourceLoader.loadString("generation.pause"), ResourceLoader.loadString("generation.pause.tooltip"));
        bNext = SandboxTools.newJButton(ResourceLoader.loadString("generation.next"), ResourceLoader.loadString("generation.next.tooltip"));
        bGoTo = SandboxTools.newJButton(ResourceLoader.loadString("generation.goto"), ResourceLoader.loadString("generation.goto.tooltip"));
        bPrev = SandboxTools.newJButton(ResourceLoader.loadString("generation.previous"), ResourceLoader.loadString("generation.previous.tooltip"));
        bPlay = SandboxTools.newJButton(ResourceLoader.loadString("generation.play"), ResourceLoader.loadString("generation.play.tooltip"));
        progresBar = new JProgressBar();
        progresBar.setIndeterminate(true);
        progresBar.setVisible(false);

        this.setLayout(new GridLayout(2, 2));
        final JPanel northL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northL.setOpaque(false);
        final JPanel northR = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        northR.setOpaque(false);
        final JPanel southL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southL.setOpaque(false);
        final JPanel southR = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southR.setOpaque(false);
        this.add(northL);
        this.add(northR);
        this.add(southL);
        this.add(southR);

        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 1000000, 10);
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

        this.setFont(new Font(this.getFont().getFontName(), this.getFont().getStyle(), this.fontSize));

        //Start conditions.
        bStart.setEnabled(true);
        bPlay.setEnabled(false);
        bPause.setEnabled(false);
        bEnd.setEnabled(false);
        this.view.setButtonClearEnabled(true);
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
        KeyListenerFactory.addKeyListener(this.view, "space", KeyEvent.VK_SPACE, () -> {
            if (bStart.isEnabled()) {
                start();
            } else if (bPlay.isEnabled()) {
                resume();
            } else if (bPause.isEnabled()) {
                pause();
            }
        });
        KeyListenerFactory.addKeyListener(this.view, "end", KeyEvent.VK_ESCAPE, () -> bEnd.doClick());
        KeyListenerFactory.addKeyListener(this.view, "next", KeyEvent.VK_RIGHT, () -> bNext.doClick());
        KeyListenerFactory.addKeyListener(this.view, "previous", KeyEvent.VK_LEFT, () -> bPrev.doClick());
        KeyListenerFactory.addKeyListener(this.view, "goto", KeyEvent.VK_ENTER, () -> bGoTo.doClick());
        KeyListenerFactory.addKeyListener(this.view, "speedUp", KeyEvent.VK_UP, () -> speedSlider.setValue(speedSlider.getValue() + 1));
        KeyListenerFactory.addKeyListener(this.view, "speedDown", KeyEvent.VK_DOWN, () -> speedSlider.setValue(speedSlider.getValue() - 1));
        this.requestFocusInWindow();
    }

    /**
     * 
     */
    public void clear() {
        this.view.getGridEditor().clean();
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
            JOptionPane.showMessageDialog(this, ResourceLoader.loadString("generation.undo").replaceAll("start", value.toString()).replaceAll("end", this.generationController.getCurrentNumberGeneration().toString()));
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
        this.view.getButtonBook().setEnabled(true);
        bStart.setEnabled(true);
        bPlay.setEnabled(false);
        bPause.setEnabled(false);
        bEnd.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);
        this.view.setButtonApplyEnabled(true);
        this.view.setButtonClearEnabled(true);
    }

    private void start() {
        this.view.getGridEditor().setEnabled(false);
        this.generationController.newGame();
        this.view.setButtonApplyEnabled(false);
        this.view.getButtonBook().setEnabled(false);
        this.bStart.setEnabled(false);
        this.bPause.setEnabled(false);
        this.bPlay.setEnabled(true);
        this.bEnd.setEnabled(true);
        this.setTimeButtonEnable(true);
        this.view.setButtonClearEnabled(false);
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
    public void resetGrid() {
        this.generationController.reset();
    }

    /**
     * 
     */
    public void refreshView() {
        if (!this.view.getGridEditor().isEnabled()) {
            this.view.getGridEditor().draw(this.generationController.getCurrentGeneration());
        }
        final int aliveCell = (int) this.generationController.getCurrentGeneration().getCellMatrix().stream().filter(cell -> cell.getStatus().equals(Status.ALIVE)).count();
        SwingUtilities.invokeLater(() -> {
            SandboxTools.refreshStatistics(
                this.getCurrentSpeed(),
                this.generationController.getCurrentNumberGeneration().intValue(),
                aliveCell,
                this.view.getFont());
        });
    }
}
