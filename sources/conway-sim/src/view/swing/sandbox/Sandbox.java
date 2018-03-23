package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controller.editor.GridEditorImpl;
import controller.editor.PatternEditor;
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

    private static final int DEFAULT_SIZE = 100;

    private static final int CELL_SIZE_RATIO = 100;

    private final GenerationPanel generationPanel;
    private final JButton bBook;
    private final DesktopGUI mainGUI;
    private final PatternEditor gridEditor;
    private BookFrame book;

    /**
     * 
     * @param mainGUI the mainGui that call this SandBox
     */
    public Sandbox(final DesktopGUI mainGUI) {
        Objects.requireNonNull(mainGUI);
        this.mainGUI = mainGUI;
        final GridPanel grid = new GridPanel(DEFAULT_SIZE, DEFAULT_SIZE, Math.max(
                mainGUI.getScreenHeight(),
                mainGUI.getScreenWidth())
                / CELL_SIZE_RATIO);
        this.setLayout(new BorderLayout());
        this.add(grid, BorderLayout.CENTER);
        this.gridEditor = new GridEditorImpl(grid);
        this.gridEditor.setEnabled(true);
        this.generationPanel = new GenerationPanel(this);

        final JPanel north = new JPanel(new BorderLayout());
        final JPanel gridOption = SandboxTools.newGridOptionDimension(this);
        north.add(this.generationPanel, BorderLayout.AFTER_LINE_ENDS);
        north.add(new JLabel("SANDBOX MODE"), BorderLayout.BEFORE_FIRST_LINE);
        north.add(gridOption, BorderLayout.WEST);
        this.add(north, BorderLayout.NORTH);

        this.bBook = SandboxTools.newJButton("BOOK", "Open the book");
        final JButton bExit = SandboxTools.newJButton("EXIT", "Exit from this mode");

        final JPanel south = new JPanel(new BorderLayout());
        final JPanel southRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        southRight.add(SandboxTools.getClearButton());
        southRight.add(this.bBook);
        southRight.add(bExit);
        south.add(SandboxTools.newJPanelStatistics(), BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        this.bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());
        this.generationPanel.refreshView();
    }

    /**
     * 
     * @return the book button
     */
    public JButton getBookButton() {
        return this.bBook;
    }

    /**
     * @return the gridEtitor
     */
    public PatternEditor getGridEditor() {
        return this.gridEditor;
    }

    /**
     * refresh all the view.
     */
    public void refreshView() {
        this.generationPanel.refreshView();
    }

    private void callBook() {
        if (Objects.isNull(this.book)) {
            this.book = new BookFrame(this.gridEditor);
            this.mainGUI.popUpFrame(this.book);
        } else if (book.isClosed()) {
            this.mainGUI.detachFrame(this.book);
            this.book = new BookFrame(this.gridEditor);
            this.mainGUI.popUpFrame(this.book);
        }
    }

    private void exit() {
        //TODO save option name
        final int result = JOptionPane.showOptionDialog(this, "Going back to menu?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        if (result == JOptionPane.YES_OPTION) {
            this.mainGUI.backToMainMenu();
        }
    }
}
