package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.swing.GUI;
import view.swing.book.BookFrame;

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
    private final BookFrame book = new BookFrame();

    /**
     * 
     * @param maingui the mainGui that call this SandBox
     */
    public Sandbox(final GUI maingui) {

        final JPanel eastButtonPanel = new JPanel(new FlowLayout());
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
        this.generationPanel.setLayoutSize(this.getSize(), 10);
        eastButtonPanel.add(generationPanel);
        eastButtonPanel.add(bBook);
        this.add(eastButtonPanel, BorderLayout.EAST);
        bBook.addActionListener(e -> book());

        final JButton bExit = new JButton("EXIT");
        this.add(bExit, BorderLayout.SOUTH);
        bExit.addActionListener(e -> maingui.backToMainMenu());
    }

	private void book() {
		
	}
}
