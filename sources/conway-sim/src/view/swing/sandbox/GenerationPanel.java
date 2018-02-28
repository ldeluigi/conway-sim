package view.swing.sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import controller.generation.GenerationControllerImpl;

/**
 * This is the panel that contain all the Generation control for the application.
 */
public class GenerationPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9060069868596999045L;

    private static final int RELATIONSHIP_STANDARD = 6;
    private static final String START = "START";
    private static final String STOP = "STOP";
    private static final String PAUSE = "PAUSE";
    private static final String UNDO = "UNDO";

    private final GenerationControllerImpl generationController = new GenerationControllerImpl();

    /**
     * 
     */
    public GenerationPanel() {
        final JButton bStart = new JButton(START);
        final JButton bStop = new JButton(STOP);
        final JButton bPause = new JButton(PAUSE);
        final JButton bUndo = new JButton(UNDO);

        this.setLayout(new FlowLayout());
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JPanel undoPanel = new JPanel(new GridLayout(2, 2));
        this.add(bStart);
        this.add(bPause);
        this.add(bStop);
        this.add(bUndo);


        final JTextComponent undoField = new JTextField();
        undoField.setEditable(true);
        undoField.setText("1234123");

        this.add(undoField);
        this.add(buttonPanel);
        this.add(undoPanel);
        this.setLayoutSize(Toolkit.getDefaultToolkit().getScreenSize(), RELATIONSHIP_STANDARD);

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setVisible(true);

        bStart.addActionListener(e -> generationController.startGameWithGeneration());
        bStop.addActionListener(e -> generationController.end());
        bPause.addActionListener(e -> generationController.pause());
        bUndo.addActionListener(e -> generationController.loadOldGeneration(1L));
    }

    /**
     * 
     * @param currentExternalDimension set the dimension of the panel with a relationship standard 
     */
    public void setLayoutSize(final Dimension currentExternalDimension) {
        this.setLayoutSize(currentExternalDimension, RELATIONSHIP_STANDARD);
    }

    /**
     * 
     * @param currentExternalDimension set the dimension of the panel with a relationship of relationship
     * @param relationship between the panel and the external frame
     */
    public void setLayoutSize(final Dimension currentExternalDimension, final int relationship) {
        if (relationship <= 0) {
            throw new IllegalArgumentException();
        }
        this.setSize((int) currentExternalDimension.getWidth() / relationship, (int) currentExternalDimension.getHeight() / relationship);
    }

    /* 
     * Personal test don't remove
     */
    public static void main(final String[] s) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GenerationPanel());
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }

}
