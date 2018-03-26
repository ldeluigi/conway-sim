package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import controller.editor.GridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.ResourceLoader;
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

    private static final int DEFAULT_SIZE = 100;

    private static final int CELL_SIZE_RATIO = 100;

    private final GenerationPanel generationPanel;
    private final JButton bBook;
    private final JButton bApply;
    private final JButton bClear;
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
        this.bClear = SandboxTools.newJButton("CLEAR", "Clear the grid");
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
        north.setOpaque(false);
        bApply = SandboxTools.newJButton("Apply", "Change the grid dimension");
        final JPanel gridOption = SandboxTools.newGridOptionDimension(this, bApply, this.getFont());
        gridOption.setOpaque(false);
        north.add(this.generationPanel, BorderLayout.AFTER_LINE_ENDS);
        north.add(new JLabel("SANDBOX MODE"), BorderLayout.BEFORE_FIRST_LINE);
        north.add(gridOption, BorderLayout.WEST);
        this.add(north, BorderLayout.NORTH);

        this.bBook = SandboxTools.newJButton("BOOK", "Open the book");
        final JButton bExit = SandboxTools.newJButton("EXIT", "Exit from this mode");

        final JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        final JPanel southRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRight.setOpaque(false);

        southRight.add(bClear);
        southRight.add(this.bBook);
        southRight.add(bExit);
        south.add(SandboxTools.newJPanelStatistics(this.getFont()), BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        this.bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());

        final InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
        this.bClear.addActionListener(e -> this.generationPanel.clear());
        KeyListenerFactory.addKeyListener(this, "clear", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK, () -> {
            if (bClear.isEnabled()) {
                this.generationPanel.clear();
            }
        });
        this.setOpaque(false);
        this.setBackground(Color.RED);
        this.generationPanel.refreshView();
    }

    @Override
    public Font getFont() {
        return new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize());
    }

    @Override
    public void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     *@param flag a boolean flag
     */
    public void setButtonClearEnabled(final boolean flag) {
        this.bClear.setEnabled(flag);
    }

    /**
     * 
     * @param flag a boolean flag
     */
    public void setButtonApplyEnabled(final boolean flag) {
        this.bApply.setEnabled(flag);
    }

    /**
     * 
     * @return the book button
     */
    public JButton getButtonBook() {
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
        this.grabFocus();
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
