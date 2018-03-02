package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import view.swing.DesktopGUI;
import view.swing.book.BookFrame;

/**
 * 
 */
public class Sandbox extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -9015811419136279771L;
    private static final String BOOK_NAME = "BOOK";

    private final GenerationPanel generationPanel = new GenerationPanel();
    private final JButton bBook = new JButton(BOOK_NAME);
    private final DesktopGUI mainGUI;

    /**
     * 
     * @param mainGUI the mainGui that call this SandBox
     */
    public Sandbox(final DesktopGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.setLayout(new BorderLayout());
        this.add(new GridPanel(), BorderLayout.CENTER);

        this.add(generationPanel, BorderLayout.NORTH);

        final JButton bExit = new JButton("EXIT");

        final JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        south.add(bBook);
        south.add(bExit);

        this.add(south, BorderLayout.SOUTH);

        bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());
    }

    private void callBook() {

        this.mainGUI.popUpFrame(new BookFrame());
    }

    private void exit() {
        final int result = JOptionPane.showOptionDialog(this, "Save before going back to menu?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        if (result == JOptionPane.YES_OPTION) {
        //TODO save option
            this.mainGUI.backToMainMenu();
        } else if (result == JOptionPane.NO_OPTION) {
            this.mainGUI.backToMainMenu();
        }
    }
}
