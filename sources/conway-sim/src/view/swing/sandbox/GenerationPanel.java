package view.swing.sandbox;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
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
import controller.generation.GenerationObserver;
import controller.generation.GenerationControllerImpl;
import controller.io.ResourceLoader;
import core.campaign.GameWinningCell;
import core.model.Status;
import view.swing.menu.MenuSettings;

/**
 * This is the panel that contain all the Generation control for the
 * application. Button start, play, pause, end and time management goto, next
 * and previous and the speed option.
 * 
 */
public class GenerationPanel extends JPanel {

    private static final long serialVersionUID = 9060069868596999045L;

    /**
     * Speed option, editable in ConstantBundle.properties.
     */
    private static final int MIN_SPEED = ResourceLoader.loadConstantInt("generation.MIN_SPEED");
    private static final int MAX_SPEED = ResourceLoader.loadConstantInt("generation.MAX_SPEED");

    /**
     * JButton of the panel.
     */
    private final JSlider speedSlider;
    private final JButton bStart;
    private final JButton bEnd;
    private final JButton bPause;
    private final JButton bNext;
    private final JButton bGoTo;
    private final JButton bPrev;
    private final JButton bPlay;
    private final JProgressBar progresBar;

    private final JPanel generationJumpPanel;

    private final GenerationObserver generationController;
    private final AbstractSandbox view;

    private final int fontSize = MenuSettings.getFontSize();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * This variable is used only in case of level mode, level mode is usable only
     * if a runnable is given in the constructor.
     */
    private static final int REPETITION_FOR_WIN = 3;
    private boolean isLevelMode;
    private int counterLevel;
    private Runnable runnable;
    private boolean isWin;

    /**
     * A panel that contains all the buttons for the start and control of the game.
     * 
     * @param view
     *            the controller of the generation
     */
    public GenerationPanel(final AbstractSandbox view) {
        this.view = view;
        this.setOpaque(false);
        this.generationController = new GenerationControllerImpl(view);

        this.bStart = SandboxTools.newJButton(ResourceLoader.loadString("generation.start"),
                ResourceLoader.loadString("generation.start.tooltip"));
        this.bEnd = SandboxTools.newJButton(ResourceLoader.loadString("generation.end"),
                ResourceLoader.loadString("generation.end.tooltip"));
        this.bPause = SandboxTools.newJButton(ResourceLoader.loadString("generation.pause"),
                ResourceLoader.loadString("generation.pause.tooltip"));
        this.bNext = SandboxTools.newJButton(ResourceLoader.loadString("generation.next"),
                ResourceLoader.loadString("generation.next.tooltip"));
        this.bGoTo = SandboxTools.newJButton(ResourceLoader.loadString("generation.goto"),
                ResourceLoader.loadString("generation.goto.tooltip"));
        this.bPrev = SandboxTools.newJButton(ResourceLoader.loadString("generation.previous"),
                ResourceLoader.loadString("generation.previous.tooltip"));
        this.bPlay = SandboxTools.newJButton(ResourceLoader.loadString("generation.play"),
                ResourceLoader.loadString("generation.play.tooltip"));
        this.progresBar = new JProgressBar();
        this.progresBar.setIndeterminate(true);
        this.progresBar.setVisible(false);

        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        final GridBagConstraints c1 = new GridBagConstraints();

        // start, play, pause, stop
        c1.gridx = 0;
        c1.gridy = 0;
        this.add(this.bStart, c1);
        c1.gridx = 0;
        c1.gridy = 1;
        this.add(this.bPlay, c1);
        c1.gridx = 1;
        c1.gridy = 1;
        this.add(this.bPause, c1);
        c1.gridx = 2;
        c1.gridy = 1;
        this.add(this.bEnd, c1);

        // speed control
        this.speedSlider = new JSlider(MIN_SPEED, MAX_SPEED, 1);
        this.speedSlider.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        c1.gridwidth = 2;
        c1.gridx = 1;
        c1.gridy = 0;
        this.add(speedSlider, c1);

        // generation jump panel
        this.generationJumpPanel = new JPanel(new GridBagLayout());
        this.generationJumpPanel.setOpaque(false);
        final GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 1;
        this.generationJumpPanel.add(this.bPrev, c2);
        c2.gridx = 1;
        c2.gridy = 1;
        this.generationJumpPanel.add(this.bNext, c2);
        c2.gridx = 0;
        c2.gridy = 0;
        this.generationJumpPanel.add(this.bGoTo, c2);
        this.generationJumpPanel.add(this.progresBar, c2);
        c2.gridx = 1;
        c2.gridy = 0;
        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 1000000, 10);
        final JSpinner spinner = new JSpinner(spin);
        this.generationJumpPanel.add(spinner, c2); // to use the go to button

        this.setFont(new Font(this.getFont().getFontName(), this.getFont().getStyle(), this.fontSize));

        // Start conditions.
        this.bStart.setEnabled(true);
        this.bPlay.setEnabled(false);
        this.bPause.setEnabled(false);
        this.bEnd.setEnabled(false);
        this.view.setButtonClearEnabled(true);
        this.bNext.setEnabled(false);
        this.bPrev.setEnabled(false);
        this.bGoTo.setEnabled(false);

        // listener
        this.speedSlider.addChangeListener(e -> this.speedControl());
        this.bStart.addActionListener(e -> this.start());
        this.bEnd.addActionListener(e -> this.end());
        this.bPlay.addActionListener(e -> this.resume());
        this.bPause.addActionListener(e -> this.pause());
        this.bGoTo.addActionListener(e -> this.goTo(Long.parseLong(spinner.getValue().toString())));
        this.bPrev.addActionListener(e -> this.goTo(this.generationController.getCurrentNumberElement() - 1L));
        this.bNext.addActionListener(e -> this.goTo(this.generationController.getCurrentNumberElement() + 1L));
        KeyListenerFactory.addKeyListener(this.view, "space", KeyEvent.VK_SPACE, () -> {
            if (this.bStart.isEnabled()) {
                this.bStart.doClick();
            } else if (this.bPlay.isEnabled()) {
                this.bPlay.doClick();
            } else if (this.bPause.isEnabled()) {
                this.bPause.doClick();
            }
        });

        // Key listener of the panel
        KeyListenerFactory.addKeyListener(this.view, "end", KeyEvent.VK_ESCAPE, () -> this.bEnd.doClick());
        KeyListenerFactory.addKeyListener(this.view, "next", KeyEvent.VK_RIGHT, () -> this.bNext.doClick());
        KeyListenerFactory.addKeyListener(this.view, "previous", KeyEvent.VK_LEFT, () -> this.bPrev.doClick());
        KeyListenerFactory.addKeyListener(this.view, "goto", KeyEvent.VK_ENTER, () -> this.bGoTo.doClick());
        KeyListenerFactory.addKeyListener(this.view, "speedUp", KeyEvent.VK_UP,
                () -> this.speedSlider.setValue(speedSlider.getValue() + 1));
        KeyListenerFactory.addKeyListener(this.view, "speedDown", KeyEvent.VK_DOWN,
                () -> this.speedSlider.setValue(this.speedSlider.getValue() - 1));
        this.requestFocusInWindow();
    }

    /**
     * 
     * @param view
     *            the controller of the generation
     * @param runnableVictory
     *            the {@link Runnable} that is started when all the
     *            {@link GameWinningCell} are dead for 3 consecutive generations
     *
     *            If level is not declared, the game have no winning condition.
     */
    public GenerationPanel(final AbstractSandbox view, final Runnable runnableVictory) {
        this(view);
        this.runnable = runnableVictory;
        this.isLevelMode = true;
    }

    /**
     * 
     * @return the panel with the generation jump control.
     */
    public JPanel getGenerationJumpPanel() {
        return this.generationJumpPanel;
    }

    /**
     * 
     * @return the current speed value
     */
    public int getCurrentSpeed() {
        return this.speedSlider.getValue();
    }

    /**
     * Reset the current game to the original status.
     */
    public void resetGrid() {
        this.generationController.pause();
        this.end();
        this.generationController.reset();
    }

    /**
     * Refresh the view of this panel and reload the constant.
     */
    public void refreshView() {
        if (!this.view.getGridEditor().isEnabled()) {
            this.view.getGridEditor().draw(this.generationController.getCurrentElement());
        }
        int general = 0;
        // LEVEL OPTION
        if (this.isLevelMode) {
            final int gold = this.generationController.getCurrentElement().getCellMatrix().stream().parallel().mapToInt(
                    e -> e.getStatus().equals(Status.ALIVE) && e.code() == GameWinningCell.GAME_WINNING_CODE ? 1 : 0)
                    .sum();
            general = this.generationController.getCurrentElement().getCellMatrix().stream().parallel()
                    .mapToInt(e -> e.getStatus().equals(Status.ALIVE) ? 1 : 0).sum();
            this.counterLevel = gold == 0 ? this.counterLevel + 1 : 0;
            if (this.counterLevel >= REPETITION_FOR_WIN && !isWin) {
                this.isWin = true;
                this.view.scheduleGUIUpdate(() -> {
                    this.resetGrid();
                    this.runnable.run();
                });
                this.counterLevel = 0;
            }
            // END LEVEL OPTION
        } else {
            this.isWin = false;
            general = this.generationController.getCurrentElement().getCellMatrix().stream()
                    .filter(cell -> cell.getStatus().equals(Status.ALIVE)).mapToInt(e -> 1).sum();
        }
        final int generalF = general;
        this.view.scheduleGUIUpdate(() -> {
            SandboxTools.refreshStatistics(this.getCurrentSpeed(),
                    this.generationController.getCurrentNumberElement().intValue(), generalF,
                    new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        });
    }

    private void speedControl() {
        this.generationController.setSpeed(this.speedSlider.getValue());
    }

    private void goTo(final Long value) {
        if (value < 0) {
            JOptionPane.showMessageDialog(this,
                    ResourceLoader.loadString("generation.undo").replaceAll("end", value.toString()).replaceAll("start",
                            this.generationController.getCurrentNumberElement().toString()));
        } else if (!value.equals(this.generationController.getCurrentNumberElement())) {
            this.bPlay.setEnabled(false);
            this.bEnd.setEnabled(false);
            this.setTimeButtonEnable(false);
            this.progresBar.setVisible(true);
            //FutureTast return is ignored
            final FutureTask<Object> fTask = new FutureTask<>(() -> {

                this.generationController.loadGeneration(value);

                    this.view.scheduleGUIUpdate(() -> {
                        this.progresBar.setVisible(false);
                        this.bPlay.setEnabled(true);
                        this.bEnd.setEnabled(true);
                        this.setTimeButtonEnable(true);
                        this.refreshView();
                    });
            }, null);
            this.executor.execute(fTask);
        }
    }

    /**
     * This method terminate the execution of the generation and refresh the first
     * status. Override this and add eventual action that should be done with the
     * end of the game mode and the beginning of the edit mode.
     */
    protected void end() {
        this.view.getGridEditor().setEnabled(true);
        this.view.setButtonBookEnable(true);
        this.bStart.setEnabled(true);
        this.bPlay.setEnabled(false);
        this.bEnd.setEnabled(false);
        this.bNext.setEnabled(false);
        this.bPrev.setEnabled(false);
        this.bGoTo.setEnabled(false);
        if (this.bPause.isEnabled()) {
            this.bPause.setEnabled(false);
        }
        if (this.isLevelMode) {
            this.isWin = false;
        }
        this.view.setButtonClearEnabled(true);
    }

    /**
     * This method start the execution of the generation and set button enable
     * true/false. Override this and add eventual action that should be done with
     * the start of the game mode.
     */
    protected void start() {
        this.view.getGridEditor().setEnabled(false);
        this.generationController.newGame();
        this.view.setButtonBookEnable(false);
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
}
