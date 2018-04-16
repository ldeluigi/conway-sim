package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.editor.ExtensionGridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.RLEConvert;
import controller.io.RLETranslator;
import controller.io.ResourceLoader;
import core.utils.ListMatrix;
import core.utils.Matrices;
import view.DesktopGUI;
import view.swing.Sandbox;
import view.swing.book.BookFrame;
import view.swing.menu.LoadingScreen;
import view.swing.menu.MenuSettings;

/**
 * 
 */
public class SandboxImpl extends JPanel implements Sandbox {

    private static final long serialVersionUID = -9015811419136279771L;
    private static final int DEFAULT_SIZE = 100;
    private static final int CELL_SIZE_RATIO = 100;

    private final GenerationPanel generationPanel;
    private final JButton bBook;
    private final JButton bApply;
    private final JButton bClear;
    private final JButton bSave;
    private final JButton bCancel;
    private final JTextField saveGuide;
    private final DesktopGUI mainGUI;
    private final ExtensionGridEditorImpl gridEditor;
    private final GridPanelImpl grid;
    private BookFrame book;

    /**
     * 
     * @param mainGUI
     *            the mainGui that call this SandBox
     */
    public SandboxImpl(final DesktopGUI mainGUI) {
        Objects.requireNonNull(mainGUI);
        this.mainGUI = mainGUI;
        this.bClear = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.clear"),
                ResourceLoader.loadString("sandbox.clear.tooltip"));
        this.grid = new GridPanelImpl(DEFAULT_SIZE, DEFAULT_SIZE,
                Math.max(mainGUI.getScreenHeight(), mainGUI.getScreenWidth()) / CELL_SIZE_RATIO);
        this.setLayout(new BorderLayout());
        this.add(this.grid, BorderLayout.CENTER);
        this.gridEditor = new ExtensionGridEditorImpl(grid);
        this.gridEditor.setEnabled(true);
        this.generationPanel = new GenerationPanel(this);

        final JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        this.bApply = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.apply"),
                ResourceLoader.loadString("sandbox.apply.tooltip"));
        final JPanel gridOption = SandboxTools.newGridOptionDimension(this, bApply,
                new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        gridOption.setOpaque(false);
        north.add(this.generationPanel, BorderLayout.EAST);
        final JLabel mode = new JLabel(ResourceLoader.loadString("sandbox.mode"));
        mode.setFont(getFont());
        north.add(mode, BorderLayout.BEFORE_FIRST_LINE);
        north.add(gridOption, BorderLayout.WEST);
        this.add(north, BorderLayout.NORTH);

        this.bBook = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.book"),
                ResourceLoader.loadString("sandbox.book.tooltip"));
        final JButton bExit = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.exit"),
                ResourceLoader.loadString("sandbox.exit.tooltip"));

        final JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        final JPanel southRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southRight.setOpaque(false);
        //TEST
        this.saveGuide = new JTextField("Premere save");
        this.saveGuide.setEditable(false);
        this.saveGuide.setFont(getFont());
        this.saveGuide.setOpaque(false);
        southRight.add(saveGuide);
        this.bSave = SandboxTools.newJButton("SAVE", "Select and save a pattern.");
        southRight.add(bSave);
        bCancel = SandboxTools.newJButton("Cancel");
        bCancel.setVisible(false);
        bCancel.addActionListener(e -> this.cancel());
        bSave.addActionListener(e -> this.save());
        southRight.add(bCancel);

        southRight.add(bClear);
        southRight.add(this.bBook);
        southRight.add(bExit);
        south.add(SandboxTools.newJPanelStatistics(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize())),
                BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        this.bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());

        // To ignore the space keyStroke
        final InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
        this.bClear.addActionListener(e -> this.generationPanel.clear());

        // Button clear keylistener
        KeyListenerFactory.addKeyListener(this, "clear", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK,
                () -> bClear.doClick());
        KeyListenerFactory.addKeyListener(this, "book", KeyEvent.VK_B, () -> bBook.doClick());
        SwingUtilities.invokeLater(() -> {
            this.setFocusable(true);
            this.requestFocusInWindow();
        });
    }

    /**
     * 
     */
    @Override
    public void setButtonSaveEnable(final boolean flag) {
        this.bSave.setEnabled(flag);
    }

    private void cancel() {
        this.gridEditor.cancelSelectMode();
        this.bCancel.setVisible(false);
        this.bSave.setEnabled(true);
        this.generationPanel.setButtonStartEnable(true);
        this.bBook.setEnabled(true);
        this.bClear.setEnabled(true);
        this.bApply.setEnabled(true);
    }

    private JInternalFrame fileSave() {
        final JInternalFrame jif = new JInternalFrame("Select Title", true, false, false);
        final JPanel panel = new JPanel(new FlowLayout());
        final JButton bSave = SandboxTools.newJButton("SAVE");
        final JTextField text = new JTextField("File name");
        jif.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
        panel.add(text);
        panel.add(bSave);
        jif.add(panel);
        jif.pack();
        jif.setVisible(true);
        bSave.addActionListener(e -> {
            String stringMatrix = RLEConvert.write(this.gridEditor.cutMatrix());
            try (BufferedWriter b = new BufferedWriter(new FileWriter(new File("PatternBook" + "/" + text.getText() + ".rle")));) {
                b.write(stringMatrix);
                System.out.println(stringMatrix);
                b.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.saveGuide.setText("File saved");
            this.gridEditor.selectMode(false);
            this.bSave.setEnabled(true);
            this.generationPanel.setButtonStartEnable(true);
            this.bBook.setEnabled(true);
            this.bClear.setEnabled(true);
            this.bApply.setEnabled(true);
            try {
                jif.setClosed(true);
            } catch (PropertyVetoException e1) {
                e1.printStackTrace();
            }
        });
        return jif;
    }

    private void save() {
        if (this.gridEditor.isCutReady()) {
            this.bSave.setEnabled(false);
            this.bCancel.setVisible(false);
            this.mainGUI.popUpFrame(fileSave(), false);
        } else {
            this.generationPanel.setButtonStartEnable(false);
            this.bBook.setEnabled(false);
            this.bClear.setEnabled(false);
            this.bApply.setEnabled(false);
            this.gridEditor.selectMode(true);
            this.bCancel.setVisible(true);
            this.saveGuide.setText("Select on the grid");
        }
    }

    /**
     * Applies size changes to the grid.
     */
    public void resetGrid() {
        this.setVisible(false);
        this.grid.setVisible(false);
        this.remove(grid);
        final JPanel loading = new LoadingScreen();
        this.add(loading, BorderLayout.CENTER);
        this.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            this.gridEditor.changeSizes(SandboxTools.getWidthSelect(), SandboxTools.getHeightSelect());
            this.remove(loading);
            this.add(grid, BorderLayout.CENTER);
            this.grid.setVisible(true);
        });
    }

    @Override
    public final Font getFont() {
        return new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * Enables "clear" button to be clicked by the user.
     * @param flag
     *            a boolean flag
     */
    public void setButtonClearEnabled(final boolean flag) {
        this.bClear.setEnabled(flag);
    }

    /**
     * Enables "apply" button to be clicked by the user.
     * @param flag
     *            a boolean flag
     */
    public void setButtonApplyEnabled(final boolean flag) {
        this.bApply.setEnabled(flag);
    }

    /**
     * Gets the button used to call the recipe book and returns it.
     * @return the book button
     */
    public JButton getButtonBook() {
        return this.bBook;
    }

    /**
     * Gets the editor used to handle the grid and returns it.
     * @return the gridEtitor
     */
    public PatternEditor getGridEditor() {
        return this.gridEditor;
    }

    /**
     * Refreshes generation panel.
     */
    public void refreshView() {
        this.generationPanel.refreshView();
    }

    private void callBook() {
        if (Objects.isNull(this.book)) {
            this.book = new BookFrame(this.gridEditor);
            this.mainGUI.popUpFrame(this.book, false);
        } else if (book.isClosed()) {
            this.mainGUI.detachFrame(this.book);
            this.book = new BookFrame(this.gridEditor);
            this.mainGUI.popUpFrame(this.book, false);
        }
    }

    private void exit() {
        final int result = JOptionPane.showOptionDialog(
                this, ResourceLoader.loadString("option.exit"), ResourceLoader.loadString("option.exit.title"),
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {
                        ResourceLoader.loadString("option.exit.yes"), ResourceLoader.loadString("option.exit.no") },
                null);
        if (result == JOptionPane.YES_OPTION) {
            if (!Objects.isNull(book) && this.book.isVisible()) {
                this.book.doDefaultCloseAction();
            }
            this.mainGUI.backToMainMenu();
        }
    }
}
