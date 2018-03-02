package view.swing.sandbox;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import controller.generation.GenerationControllerImpl;

/**
 * This is the panel that contain all the Generation control for the application.
 */
public class GenerationPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9060069868596999045L;

    private static final String START = "START";
    private static final String STOP = "STOP";
    private static final String PAUSE = "PAUSE";
    private static final String UNDO = "UNDO";
    private static final String NEXT = "NEXT";

    private final GenerationControllerImpl generationController = new GenerationControllerImpl();

    /**
     * 
     */
    public GenerationPanel() {
        final JButton bStart = new JButton(START);
        final JButton bStop = new JButton(STOP);
        final JButton bPause = new JButton(PAUSE);
        final JButton bNext = new JButton(NEXT);
        final JButton bUndo = new JButton(UNDO);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(bStart);
        this.add(bPause);
        this.add(bStop);
        this.add(bNext);
        this.add(bUndo);

        final SpinnerNumberModel spin = new SpinnerNumberModel(0, 0, 1000, 10);
        final JSpinner spinner = new JSpinner(spin);

        spinner.getValue();
        this.add(spinner);
        final JLabel generationNumber = new JLabel("Generation number: ");
        final JLabel numGeneration = new JLabel("0");
        this.add(generationNumber);
        this.add(numGeneration);

        this.add(buttonPanel);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setVisible(true);

        bStart.addActionListener(e -> generationController.startGameWithGeneration());
        bStop.addActionListener(e -> generationController.end());
        bPause.addActionListener(e -> generationController.pause());
        bUndo.addActionListener(e -> generationController.loadOldGeneration(1L));
    }

}
