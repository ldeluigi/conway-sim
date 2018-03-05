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

    private static final String STA = "Start";
    private static final String STO = "Stop";
    private static final String PAU = "Pause";
    private static final String UND = "Undo";
    private static final String NEX = "Next";
    private static final String PRE = "Previous";
    private static final String RES = "Resume";

    private final JButton bStart;
    private final JButton bStop;
    private final JButton bPause;
    private final JButton bNext;
    private final JButton bUndo;
    private final JButton bPrev;
    private final JButton bRes;

    private final JLabel numGeneration;
    private final GenerationController generationController;
    private final JPanel superPanel;

    private final int fontSize = MenuSettings.getFontSize();

    /**
     * 
     * @param controller the controller of the generation
     * @param superPanel the panel that call this one
     */
    public GenerationPanel(final GenerationController controller, final JPanel superPanel) {
        this.generationController = controller;
        this.superPanel = superPanel;

        bStart = this.newJButton(STA);
        bStart.setToolTipText("Start the game");
        bStop = this.newJButton(STO);
        bStop.setToolTipText("Reset the game mode");
        bPause = this.newJButton(PAU);
        bPause.setToolTipText("Stop the time");
        bNext = this.newJButton(NEX);
        bNext.setToolTipText("Go to the next generation");
        bUndo = this.newJButton(UND);
        bUndo.setToolTipText("Go back in time of N generations");
        bPrev = this.newJButton(PRE);
        bPrev.setToolTipText("Go to the previous generation");
        bRes = this.newJButton(RES);
        bRes.setToolTipText("Resume the current game");

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 1000, 10);
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
        this.add(bNext);
        this.add(bPrev);
        this.add(bUndo);

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
        bUndo.setEnabled(false);

        this.setVisible(true);

        bStart.addActionListener(e -> this.start());
        bStop.addActionListener(e -> this.stop());
        bRes.addActionListener(e -> this.resume());
        bPause.addActionListener(e -> this.pause());
        bUndo.addActionListener(e -> this.undo(Long.parseLong(spinner.getValue().toString())));
        bPrev.addActionListener(e -> this.undo(1L));
        bNext.addActionListener(e -> this.next());
    }

    private void next() {
        this.generationController.computeNextGeneration();
    }

    private void undo(final Long value) {
        if (this.generationController.getCurrentNumberGeneration() - value < 0) {
            // TODO error of generation undo
            JOptionPane.showMessageDialog(this, "Impossible undo of " + value + " from " + this.generationController.getCurrentNumberGeneration());
        }
        this.generationController.loadOldGeneration(this.generationController.getCurrentNumberGeneration() - value);
        this.refreshView();
    }

    private void stop() {
        if (JOptionPane.showOptionDialog(this, "Stop", "Stop this game?", JOptionPane.YES_NO_OPTION,  JOptionPane.INFORMATION_MESSAGE, null, null, null) == JOptionPane.YES_OPTION) {
            bStart.setEnabled(true);
            bRes.setEnabled(false);
            bPause.setEnabled(false);
            bStop.setEnabled(false);
            bNext.setEnabled(false);
            bPrev.setEnabled(false);
            bUndo.setEnabled(false);
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
        bUndo.setEnabled(false);
    }

    private void resume() {
        this.generationController.resume();
        this.bPause.setEnabled(true);
        bRes.setEnabled(false);
        bStop.setEnabled(false);
        bNext.setEnabled(false);
        bPrev.setEnabled(false);
        bUndo.setEnabled(false);
    }

    private void pause() {
        this.generationController.pause();
        this.generationController.pause();
        this.bPause.setEnabled(false);
        this.bRes.setEnabled(true);
        this.bNext.setEnabled(true);
        this.bPrev.setEnabled(true);
        this.bUndo.setEnabled(true);
        this.bStop.setEnabled(true);
    }

    /**
     * 
     */
    public void refreshView() {
        this.numGeneration.setText(this.generationController.getCurrentNumberGeneration().toString());
    }

    private JButton newJButton(final String name) {
        final JButton button = new JButton(name);
        button.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        return button;
    }
}
