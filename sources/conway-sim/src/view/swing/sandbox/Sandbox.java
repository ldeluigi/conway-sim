package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controller.editor.GridEditorImpl;
import controller.editor.PatternEditor;
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
    private final PatternEditor gridEditor;
    private final GenerationController genController;
    private BookFrame book;
    private final int fontSize = MenuSettings.getFontSize();
    private final SandboxUtils sandboxUtil;

    /**
     * 
     * @param mainGUI the mainGui that call this SandBox
     */
    public Sandbox(final DesktopGUI mainGUI) {
        this.sandboxUtil = new SandboxUtils(new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize));
        Objects.requireNonNull(mainGUI);
        this.genController = new GenerationControllerImpl();
        this.generationPanel = new GenerationPanel(genController);
        this.genController.setView(this);
        this.mainGUI = mainGUI;
        final GridPanel grid = new GridPanel(Sandbox.DEFAULT_SIZE, Sandbox.DEFAULT_SIZE, mainGUI);
        this.setLayout(new BorderLayout());
        this.add(grid, BorderLayout.CENTER);
        this.gridEditor = new GridEditorImpl(grid);

        final JPanel north = new JPanel(new BorderLayout());
        final JPanel gridOption = sandboxUtil.newGridOptionDimension();
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
        south.add(sandboxUtil.newJPanelStatistics(), BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());
        this.generationPanel.refreshView();

    }

    /**
     * refresh all the view.
     */
    public void refreshView() {
        this.sandboxUtil.refreshStatistics(
                this.generationPanel.getCurrentSpeed(),
                this.genController.getCurrentNumberGeneration().intValue(),
                (int) this.genController.getCurrentGeneration().getAliveMatrix().stream().filter(cell -> cell).count()
                );
        this.generationPanel.refreshView();
        this.gridEditor.draw(this.genController.getCurrentGeneration());
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

}
