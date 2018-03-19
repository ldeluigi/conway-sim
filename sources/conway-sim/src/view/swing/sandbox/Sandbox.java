package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controller.editor.GridEditorImpl;
import controller.editor.PatternEditor;
import controller.generation.GenerationController;
import controller.generation.GenerationControllerImpl;
import core.model.Generation;
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
    private final PatternEditor gridEditor;
    private final GenerationController genController;
    private BookFrame book;
    private final int fontSize = MenuSettings.getFontSize();
    private final SandboxTools sandboxTools;

    /**
     * 
     * @param mainGUI the mainGui that call this SandBox
     */
    public Sandbox(final DesktopGUI mainGUI) {
        this.sandboxTools = new SandboxTools();
        Objects.requireNonNull(mainGUI);
        this.mainGUI = mainGUI;
        GridSize gridSize = GridSize.gridSize();
        final GridPanel grid = new GridPanel(gridSize.getGridWidht(), gridSize.getGridHeight(), mainGUI);
        this.setLayout(new BorderLayout());
        this.add(grid, BorderLayout.CENTER);
        this.gridEditor = new GridEditorImpl(grid);
        this.gridEditor.setEnabled(false);
        this.genController = new GenerationControllerImpl(this);
        this.generationPanel = new GenerationPanel(this);

        final JPanel north = new JPanel(new BorderLayout());
        final JPanel gridOption = sandboxTools.newGridOptionDimension(this);
        north.add(generationPanel, BorderLayout.AFTER_LINE_ENDS);
        north.add(new JLabel("SANDBOX MODE"), BorderLayout.BEFORE_FIRST_LINE);
        north.add(gridOption, BorderLayout.WEST);
        this.add(north, BorderLayout.NORTH);

        final JButton bExit = new JButton("EXIT");

        final JPanel south = new JPanel(new BorderLayout());
        this.bBook.setFont(new Font(bBook.getFont().getFontName(), bBook.getFont().getStyle(), this.fontSize));
        bExit.setFont(new Font(bExit.getFont().getFontName(), bExit.getFont().getStyle(), this.fontSize));
        final JPanel southRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        southRight.add(bBook);
        southRight.add(bExit);
        south.add(sandboxTools.newJPanelStatistics(), BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());
        this.generationPanel.refreshView();
    }

    /**
     * 
     * @return the sandbox tools
     */
    public SandboxTools getSandboxTools() {
        return this.sandboxTools;
    }

    /**
     * 
     * @return the book button
     */
    public JButton getBookButton() {
        return this.bBook;
    }

    /**
     * 
     */
    public void reset() {
        this.mainGUI.setView(new Sandbox(mainGUI));
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
        final Generation gen = this.gridEditor.isEnabled() ? this.gridEditor.getGeneration() : this.genController.getCurrentGeneration();
        this.sandboxTools.refreshStatistics(
                this.generationPanel.getCurrentSpeed(),
                this.genController.getCurrentNumberGeneration().intValue(),
                (int) gen.getAliveMatrix().stream().filter(cell -> cell).count()
                );
        this.generationPanel.refreshView();
        this.gridEditor.draw(gen);
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
        final int result = JOptionPane.showOptionDialog(this, "Save before going back to menu?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        if (result == JOptionPane.YES_OPTION) {
            this.mainGUI.backToMainMenu();
        } else if (result == JOptionPane.NO_OPTION) {
            this.mainGUI.backToMainMenu();
        }
    }

    public GenerationController getGenerationController() {
        return this.genController;
    }

}
