package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.editor.ExtendedGridEditor;
import controller.editor.ExtendedGridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.RLEConvert;
import controller.io.ResourceLoader;
import view.GridPanel;
import view.swing.DesktopGUI;
import view.swing.book.BookFrame;
import view.swing.menu.MenuSettings;

/**
 * This is a {@link AbstractSandbox sandbox} that allows grid resize, save and
 * free play.
 */
public class SimpleSandbox extends AbstractSandbox implements ResizableSandbox {

    private static final long serialVersionUID = -3566205153979731515L;
    private static final String SAVE_BASE_TEXT = "simpleSandbox.save.base";
    private final JButton bApply;
    private final DesktopGUI mainGUI;
    private final JButton bSave;
    private final JButton bCancel;
    private final JLabel instructionLabel;
    private ExtendedGridEditor extendedGridEditor;
    private BookFrame book;

    /**
     * Calls super and the adds the grid option panel.
     * 
     * @param mainGUI
     *            the main gui that displays this panel
     */
    public SimpleSandbox(final DesktopGUI mainGUI) {
        super(mainGUI);
        this.mainGUI = mainGUI;
        this.bApply = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.apply"),
                ResourceLoader.loadString("sandbox.apply.tooltip"));
        final JPanel gridOption = SandboxTools.newGridOptionDimension(this, bApply,
                new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        gridOption.setOpaque(false);
        super.getNorthPanel().add(gridOption, BorderLayout.WEST);
        this.bSave = SandboxTools.newJButton(ResourceLoader.loadString("simpleSandbox.button.save"),
                ResourceLoader.loadString("simpleSandbox.tooltip.save"));
        this.bCancel = SandboxTools.newJButton(ResourceLoader.loadString("simpleSandbox.button.cancel"),
                ResourceLoader.loadString("simpleSandbox.tooltip.cancel"));
        this.instructionLabel = new JLabel();
        this.instructionLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        this.instructionLabel.setOpaque(false);
        this.instructionLabel.setText(ResourceLoader.loadString(SAVE_BASE_TEXT));
        this.instructionLabel.setToolTipText(ResourceLoader.loadString(SAVE_BASE_TEXT));
        final FontMetrics metrics = this.instructionLabel.getFontMetrics(instructionLabel.getFont());
        final int width = metrics.stringWidth(ResourceLoader.loadString(SAVE_BASE_TEXT));
        final int height = metrics.getHeight();
        this.instructionLabel.setPreferredSize(new Dimension(width, height));
        this.bCancel.setEnabled(false);
        final JPanel mySouth = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        mySouth.setOpaque(false);
        mySouth.add(instructionLabel, c);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        mySouth.add(bSave, c);
        c.gridx = 1;
        c.gridy = 1;
        mySouth.add(bCancel, c);
        super.getSouthPanel().add(mySouth, BorderLayout.CENTER);
        this.bSave.addActionListener(e -> save());
        this.bCancel.addActionListener(e -> cancel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JGridPanel buildGrid(final int cellSize) {
        return new JGridPanel(ResourceLoader.loadConstantInt("sandbox.grid.size"),
                ResourceLoader.loadConstantInt("sandbox.grid.size"), cellSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PatternEditor buildEditor(final GridPanel grid) {
        this.extendedGridEditor = new ExtendedGridEditorImpl(grid);
        return this.extendedGridEditor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GenerationPanel buildGenerationPanel() {
        return new ExtendedGenerationPanel(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setButtonApplyEnabled(final boolean flag) {
        this.bApply.setEnabled(flag);
    }

    /**
     * 
     * @param flag
     *            set the save button enable or disable
     */
    public void setSaveEnable(final boolean flag) {
        if (flag) {
            this.instructionLabel.setText(ResourceLoader.loadString(SAVE_BASE_TEXT));
            this.instructionLabel.setToolTipText(ResourceLoader.loadString(SAVE_BASE_TEXT));
            this.cancel();
            this.bSave.setEnabled(true);
        } else {
            this.instructionLabel.setText(ResourceLoader.loadString("simpleSandbox.save.disable"));
            this.instructionLabel.setToolTipText(ResourceLoader.loadString("simpleSandbox.save.disable"));
            this.bCancel.setEnabled(false);
            this.bSave.setEnabled(false);
        }
    }

    private void save() {
        if (this.extendedGridEditor.isCutReady()) {
            this.mainGUI.popUpFrame(
                    new JInternalFrameSave(RLEConvert.convertMatrixStatusToString(extendedGridEditor.cutMatrix())),
                    false);
            this.cancel();
            this.instructionLabel.setText(ResourceLoader.loadString(SAVE_BASE_TEXT));
            this.instructionLabel.setToolTipText(ResourceLoader.loadString(SAVE_BASE_TEXT));
        } else {
            this.instructionLabel.setText(ResourceLoader.loadString("simpleSandbox.save.select"));
            this.instructionLabel.setToolTipText(ResourceLoader.loadString("simpleSandbox.save.select"));
            this.extendedGridEditor.setSelectMode(true);
            this.bCancel.setEnabled(true);
        }
    }

    private void cancel() {
        this.extendedGridEditor.cancelSelectMode();
        this.bCancel.setEnabled(false);
        this.instructionLabel.setText(ResourceLoader.loadString("simpleSandbox.save.base"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTitle() {
        return ResourceLoader.loadString("sandbox.mode");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JInternalFrame callBook() {
        if (Objects.isNull(this.book)) {
            this.book = new BookFrame(this.getGridEditor());
            this.mainGUI.popUpFrame(this.book, false);
        } else if (book.isClosed()) {
            this.mainGUI.detachFrame(this.book);
            this.book = new BookFrame(this.getGridEditor());
            this.mainGUI.popUpFrame(this.book, false);
        }
        return book;
    }
}
