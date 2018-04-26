package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.Objects;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.editor.PatternEditor;
import controller.io.ResourceLoader;
import view.Sandbox;
import view.swing.DesktopGUI;
import view.swing.GridPanel;
import view.swing.menu.LoadingScreen;
import view.swing.menu.MenuSettings;

/**
 * Abstract implementation of {@link Sandbox} using Swing {@link JPanel}.
 */
public abstract class AbstractSandbox extends JPanel implements Sandbox {

    private static final long serialVersionUID = -9015811419136279771L;
    private static final int CELL_SIZE_RATIO = 100;

    private final GenerationPanel generationPanel;
    private final JButton bBook;
    private final JButton bClear;
    private final DesktopGUI mainGUI;
    private final PatternEditor gridEditor;
    private final JGridPanel grid;
    private JPanel north;
    private JPanel south;

    /**
     * @param mainGUI
     *            the mainGui that call this Sandbox
     */
    public AbstractSandbox(final DesktopGUI mainGUI) {
        Objects.requireNonNull(mainGUI);
        this.mainGUI = mainGUI;
        this.setLayout(new BorderLayout());
        this.bClear = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.clear"),
                ResourceLoader.loadString("sandbox.clear.tooltip"));
        this.grid = buildGrid(Math.max(mainGUI.getScreenHeight(), mainGUI.getCurrentWidth()) / CELL_SIZE_RATIO);
        this.add(this.grid, BorderLayout.CENTER);
        this.gridEditor = buildEditor(this.grid);
        this.gridEditor.setEnabled(true);
        this.generationPanel = buildGenerationPanel();

        this.initializeNorth();

        this.bBook = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.book"),
                ResourceLoader.loadString("sandbox.book.tooltip"));
        this.bBook.addActionListener(e -> callBook());

        this.initializeSouth();

        // To ignore the space keyStroke
        final InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");

        // Button clear listener
        this.bClear.addActionListener(e -> this.gridEditor.clean());
        KeyListenerFactory.addKeyListener(this, "clear", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK,
                () -> bClear.doClick());
        KeyListenerFactory.addKeyListener(this, "book", KeyEvent.VK_B, () -> bBook.doClick());
        SwingUtilities.invokeLater(() -> {
            this.setFocusable(true);
            this.requestFocusInWindow();
        });
    }

    /**
     * This method is called by {@link AbstractSandbox} constructor to create the
     * JGridPanel used inside the editor.
     * 
     * @param cellSize
     *            the call of the method gives a suggested cellSize to start with
     * @return the custom created {@link JGridPanel} or one subclass
     */
    protected abstract JGridPanel buildGrid(int cellSize);

    /**
     * This method is called by {@link AbstractSandbox} constructor to create the
     * GenerationPanel used to manage generations.
     * 
     * @return the custom created {@link GenerationPanel} or one subclass.
     */
    protected abstract GenerationPanel buildGenerationPanel();

    /**
     * This method is called by {@link AbstractSandbox} constructor to create the
     * PatternEditor used as the editor.
     * 
     * @param gridp
     *            the call of the method gives a reference to the current
     *            {@link GridPanel} in use
     * @return the custom created {@link PatternEditor}
     */
    protected abstract PatternEditor buildEditor(GridPanel gridp);

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setButtonClearEnabled(final boolean flag) {
        this.bClear.setEnabled(flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setButtonBookEnable(final boolean flag) {
        this.bBook.setEnabled(flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PatternEditor getGridEditor() {
        return this.gridEditor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshView() {
        this.generationPanel.refreshView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scheduleGUIUpdate(final Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    /**
     * Before {@link BorderLayoit.BEFORE_FIRST_LINE first_line} and
     * {@link BorderLayoout.EAST east} are already used for this north panel.
     * 
     * @return the panel which is going to be added northern
     */
    protected final JPanel getNorthPanel() {
        return this.north;
    }

    /**
     * WEST {@link BorderLayoit.WEST west} and EAST {@link BorderLayoout.EAST east}
     * are already used for this north panel.
     * 
     * @return the panel which is going to be added souther
     */
    protected final JPanel getSouthPanel() {
        return this.south;
    }

    /**
     * Implement this method to change the displayed mode name.
     * 
     * @return the title of the mode
     */
    protected abstract String getTitle();

    /**
     * @return the created book.
     */
    protected abstract JInternalFrame callBook();

    private void initializeNorth() {
        this.north = new JPanel(new BorderLayout());
        this.north.setOpaque(false);
        this.north.add(this.generationPanel, BorderLayout.CENTER);
        this.north.add(this.generationPanel.getGenerationTimePanel(), BorderLayout.EAST);
        final JLabel mode = new JLabel(this.getTitle());
        mode.setFont(defaultFont());
        this.north.add(mode, BorderLayout.BEFORE_FIRST_LINE);
        this.add(this.north, BorderLayout.NORTH);
    }

    private void initializeSouth() {
        this.south = new JPanel(new BorderLayout());
        final JButton bExit = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.exit"),
                ResourceLoader.loadString("sandbox.exit.tooltip"));
        final Dimension newDim = bExit.getPreferredSize();
        newDim.setSize(newDim.getWidth() * 2, newDim.height);
        bExit.setPreferredSize(newDim);
        SandboxTools.setIcon(bExit, newDim);
        south.setOpaque(false);
        bExit.addActionListener(e -> exit());
        final JPanel southRight = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        southRight.setOpaque(false);
        southRight.add(this.bBook, c);
        c.gridx = 1;
        c.gridy = 0;
        southRight.add(this.bClear, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        southRight.add(bExit, c);
        south.add(SandboxTools.newJPanelStatistics(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize())),
                BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);
    }

    private void exit() {
        final int result = JOptionPane.showOptionDialog(
                this, ResourceLoader.loadString("option.exit"), ResourceLoader.loadString("option.exit.title"),
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {
                        ResourceLoader.loadString("option.exit.yes"), ResourceLoader.loadString("option.exit.no") },
                null);
        if (result == JOptionPane.YES_OPTION) {
            this.callBook().doDefaultCloseAction();
            this.mainGUI.backToMainMenu();
        }
    }

    private Font defaultFont() {
        return new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize());
    }
}
