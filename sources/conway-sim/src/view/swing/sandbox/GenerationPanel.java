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
import core.campaign.GameWinningCell;
import core.model.Generation;
import core.model.Status;
import view.swing.menu.MenuSettings;

/**
 * This is the panel that contain all the Generation control for the application. Button start,
 * play, pause, end and time management goto, next and previous and the speed option.
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

    private final GenerationController generationController;
    private final AbstractSandbox view;

    private final int fontSize = MenuSettings.getFontSize();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * This variable is used only in case of level mode,
     * level mode is usable only if a runnable is given in the constructor.
     */
    private static final int REPETITION_FOR_WIN = 3;
    private boolean isLevelMode;
    private int counterLevel;
    private Runnable runnable;
    private boolean isWin;

    /**
     * A panel that contain all the button for the start and control of the game.
     * 
     * @param view
     *            the controller of the generation
     */
    public GenerationPanel(final AbstractSandbox view) {
        this.view = view;
        this.setOpaque(false);
        this.generationController = new GenerationControllerImpl(view);

        bStart = SandboxTools.newJButton(ResourceLoader.loadString("generation.start"),
                ResourceLoader.loadString("generation.start.tooltip"));
        bEnd = SandboxTools.newJButton(ResourceLoader.loadString("generation.end"),
                ResourceLoader.loadString("generation.end.tooltip"));
        bPause = SandboxTools.newJButton(ResourceLoader.loadString("generation.pause"),
                ResourceLoader.loadString("generation.pause.tooltip"));
        bNext = SandboxTools.newJButton(ResourceLoader.loadString("generation.next"),
                ResourceLoader.loadString("generation.next.tooltip"));
        bGoTo = SandboxTools.newJButton(ResourceLoader.loadString("generation.goto"),
                ResourceLoader.loadString("generation.goto.tooltip"));
        bPrev = SandboxTools.newJButton(ResourceLoader.loadString("generation.previous"),
                ResourceLoader.loadString("generation.previous.tooltip"));
        bPlay = SandboxTools.newJButton(ResourceLoader.loadString("generation.play"),
                ResourceLoader.loadString("generation.play.tooltip"));
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

        // speed control
        speedSlider = new JSlider(MIN_SPEED, MAX_SPEED, 1);
        speedSlider.setFont(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        northL.add(speedSlider);

        // add button to the layout
        southL.add(bPlay);
        southL.add(bPause);
        southL.add(bEnd);
        southR.add(bPrev);
        southR.add(bNext);
        northR.add(bGoTo);
        northR.add(progresBar);

        northR.add(spinner); // to use the go to button

        this.setFont(
                new Font(this.getFont().getFontName(), this.getFont().getStyle(), this.fontSize));

        // Start conditions.
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
        bPrev.addActionListener(
                e -> this.goTo(this.generationController.getCurrentNumberElement() - 1L));
        bNext.addActionListener(
                e -> this.goTo(this.generationController.getCurrentNumberElement() + 1L));
        KeyListenerFactory.addKeyListener(this.view, "space", KeyEvent.VK_SPACE, () -> {
            if (bStart.isEnabled()) {
                bStart.doClick();
            } else if (bPlay.isEnabled()) {
                bPlay.doClick();
            } else if (bPause.isEnabled()) {
                bPause.doClick();
            }
        });

        // Key listener of the panel
        KeyListenerFactory.addKeyListener(this.view, "end", KeyEvent.VK_ESCAPE,
                () -> bEnd.doClick());
        KeyListenerFactory.addKeyListener(this.view, "next", KeyEvent.VK_RIGHT,
                () -> bNext.doClick());
        KeyListenerFactory.addKeyListener(this.view, "previous", KeyEvent.VK_LEFT,
                () -> bPrev.doClick());
        KeyListenerFactory.addKeyListener(this.view, "goto", KeyEvent.VK_ENTER,
                () -> bGoTo.doClick());
        KeyListenerFactory.addKeyListener(this.view, "speedUp", KeyEvent.VK_UP,
                () -> speedSlider.setValue(speedSlider.getValue() + 1));
        KeyListenerFactory.addKeyListener(this.view, "speedDown", KeyEvent.VK_DOWN,
                () -> speedSlider.setValue(speedSlider.getValue() - 1));
        this.requestFocusInWindow();
    }

    /**
     * 
     * @param view
     *            the controller of the generation
     * @param runnableVictory
     *            the {@link Runnable} that be lunch when all the {@link GameWinningCell} are dead
     *            for 3 consecutive gneeration
     *
     * If level is not declared, the game have no winning condition.
     */
    public GenerationPanel(final AbstractSandbox view, final Runnable runnableVictory) {
        this(view);
        this.runnable = runnableVictory;
        this.isLevelMode = true;
    }

    /**
     * Call the clean on the grid.
     */
    public void clear() {
        this.view.getGridEditor().clean();
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
        this.generationController.reset();
    }

    private synchronized void incGold() {
        gold++;
    }

    private synchronized int getGold() {
        return gold.intValue();
    }

    private int general;
    private Integer gold;
    /**
     * Refresh the view of this panel and reload the constant.
     */
    public void refreshView() {
        if (!this.view.getGridEditor().isEnabled()) {
            this.view.getGridEditor().draw(this.generationController.getCurrentElement());
        }
        general = 0;
        //LEVEL OPTION
        if (isLevelMode) {
            gold = 0;
            general = (int) this.generationController.getCurrentElement().getCellMatrix()
                    .stream()
                    .parallel()
                    .filter(cell -> cell.getStatus().equals(Status.ALIVE)).peek(e -> {
                        if (e.code() == GameWinningCell.GAME_WINNING_CODE) {
                            incGold();
                        }
                    }).count();
            this.counterLevel = getGold() == 0 ? this.counterLevel + 1 : 0;
            if (this.counterLevel >= REPETITION_FOR_WIN && !isWin) {
                isWin = true;
                this.view.scheduleGUIUpdate(() -> {
                    this.end();
                    this.runnable.run();
                });
            }
            //END LEVEL OPTION
        } else {
            general = this.generationController.getCurrentElement().getCellMatrix().stream()
            .filter(cell -> cell.getStatus().equals(Status.ALIVE)).mapToInt(e -> 1).sum();
        }
        final int general = this.general;
        this.view.scheduleGUIUpdate(() -> {
            SandboxTools.refreshStatistics(this.getCurrentSpeed(),
                    this.generationController.getCurrentNumberElement().intValue(), general,
                    new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        });
    }

    private void speedControl() {
        this.generationController.setSpeed(this.speedSlider.getValue());
    }

    private void goTo(final Long value) {
        if (value < 0) {
            JOptionPane.showMessageDialog(this, ResourceLoader.loadString("generation.undo")
                    .replaceAll("end", value.toString()).replaceAll("start",
                            this.generationController.getCurrentNumberElement().toString()));
        } else if (!value.equals(this.generationController.getCurrentNumberElement())) {
            this.bPlay.setEnabled(false);
            this.bEnd.setEnabled(false);
            this.setTimeButtonEnable(false);

            this.bGoTo.setVisible(false);
            this.progresBar.setVisible(true);

            final FutureTask<Generation> fTask = new FutureTask<>(() -> {

                this.generationController.loadOldElement(value);

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

    /**
     * This method terminate the execution of the generation and refresh the first status.
     * Override this and add eventual action that should be done with the end of the game mode and the beginning of the edit mode. 
     */
    protected void end() {
        this.view.getGridEditor().setEnabled(true);
        this.view.getButtonBook().setEnabled(true);
        bStart.setEnabled(true);
        bPlay.setEnabled(false);
        bEnd.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bGoTo.setEnabled(false);
        if (bPause.isEnabled()) {
            this.generationController.pause();
            bPause.setEnabled(false);
        }
        this.view.setButtonClearEnabled(true);
    }

    /**
     * This method start the execution of the generation and set button enable true/false.
     * Override this and add eventual action that should be done with the start of the game mode. 
     */
    protected void start() {
        this.view.getGridEditor().setEnabled(false);
        this.generationController.newGame();
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

}
