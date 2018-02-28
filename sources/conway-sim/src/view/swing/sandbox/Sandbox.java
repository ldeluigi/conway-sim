package view.swing.sandbox;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.swing.GUI;

/**
 * 
 */
public class Sandbox extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -9015811419136279771L;

    private final JLabel label = new JLabel("this is Sandbox area");
    private final GenerationPanel generationPanel = new GenerationPanel();
    private final JButton bBook = new JButton("book");

    /**
     * 
     * @param maingui the mainGui that call this SandBox
     */
    public Sandbox(final GUI maingui) {
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
        this.add(bBook, BorderLayout.EAST);
        this.generationPanel.setLayoutSize(this.getSize(), 10);
        this.add(generationPanel, BorderLayout.NORTH);
        final JButton bExit = new JButton("EXIT");
        bExit.addActionListener(e -> maingui.backToMainMenu());
        this.add(bExit, BorderLayout.SOUTH);
    }
}
