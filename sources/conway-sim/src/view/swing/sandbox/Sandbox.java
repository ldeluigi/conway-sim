package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.generation.GenerationController;
import controller.generation.GenerationControllerImpl;
import view.swing.DesktopGUI;
import view.swing.book.BookFrame;
import view.swing.menu.MenuSettings;

/**
 * 
 */
public class Sandbox extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -9015811419136279771L;
    private static final String BOOK_NAME = "BOOK";
    private static final int DEFAULT_SIZE = 100;

    private final GenerationPanel generationPanel;
    private final JButton bBook = new JButton(BOOK_NAME);
    private final DesktopGUI mainGUI;
    private final GridPanel grid;
    private final GenerationController genController;
    private BookFrame book;
    private final int fontSize = MenuSettings.getFontSize();
    /**
     * 
     * @param mainGUI the mainGui that call this SandBox
     */
    public Sandbox(final DesktopGUI mainGUI) {
        genController = new GenerationControllerImpl();
        this.generationPanel = new GenerationPanel(genController);
        genController.setView(this);
        this.mainGUI = mainGUI;
        grid = new GridPanel(Sandbox.DEFAULT_SIZE, Sandbox.DEFAULT_SIZE);
        this.setLayout(new BorderLayout());
        this.add(grid, BorderLayout.CENTER);

        this.add(generationPanel, BorderLayout.NORTH);

        final JButton bExit = new JButton("EXIT");

        final JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bBook.setFont(new Font(bBook.getFont().getFontName(), bBook.getFont().getStyle(), this.fontSize));
        bExit.setFont(new Font(bExit.getFont().getFontName(), bExit.getFont().getStyle(), this.fontSize));

        south.add(bBook);
        south.add(bExit);

        this.add(south, BorderLayout.SOUTH);

        bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());
        this.generationPanel.refreshView();
        this.grid.paintCells(this.genController.getCurrentGeneration().getAliveMatrix());

    }

    /**
     * refresh all the view.
     */
    public void refreshView() {
        this.generationPanel.refreshView();
        this.grid.paintCells(this.genController.getCurrentGeneration().getAliveMatrix());
    }

    private void callBook() {
        if (Objects.isNull(book)) {
            book = new BookFrame();
            this.mainGUI.popUpFrame(book);
        } else if (!book.isShowing()) {
            this.book = new BookFrame();
            this.mainGUI.popUpFrame(book);
        }
    }

    private void exit() {
        //TODO save option name
        final int result = JOptionPane.showOptionDialog(this, "Save before going back to menu?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        if (result == JOptionPane.YES_OPTION) {
            this.mainGUI.backToMainMenu();
        } else if (result == JOptionPane.NO_OPTION) {
            this.mainGUI.backToMainMenu();
        }
    }

}
