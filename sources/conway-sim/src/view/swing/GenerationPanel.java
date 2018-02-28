package view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.generation.GenerationController;
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
    private static final List<Long> DEFAULT_GENERATION = Arrays.asList(1L, 10L, 20L, 50L, 100L, 200L);

    private final GenerationController generationController = new GenerationControllerImpl();
    private final JButton bStart;
    private final JButton bStop;
    private final JButton bPause;
    private final JButton bUndo;
    private final JComboBox<String> selectorUndo;

    /**
     * 
     */
    public GenerationPanel() {
        this.bStart = new JButton(START);
        this.bStop = new JButton(STOP);
        this.bPause = new JButton(PAUSE);
        this.bUndo = new JButton(UNDO);
        this.selectorUndo = new JComboBox<>();

        this.setLayout(new FlowLayout());
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JPanel undoPanel = new JPanel(new GridLayout(1, 2));
        this.add(bStart);
        this.add(bPause);
        this.add(bStop);
        this.add(bUndo);

        final JComboBox<Long> defaulValue = new JComboBox<>();
        DEFAULT_GENERATION.forEach(l -> defaulValue.addItem(l));

        undoPanel.add(defaulValue);
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
//    public static void main(final String[] s) {
//        final JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new GenerationPanelFactory());
//        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        frame.setVisible(true);
//    }
    
}
