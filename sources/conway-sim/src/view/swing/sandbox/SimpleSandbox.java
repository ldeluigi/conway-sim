package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import controller.editor.ExtendedGridEditor;
import controller.editor.ExtendedGridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.ResourceLoader;
import view.DesktopGUI;
import view.swing.menu.MenuSettings;

/**
 * This is a {@link AbstractSandbox sandbox} that allows grid resize and free play.
 */
public class SimpleSandbox extends AbstractSandbox implements ResizableSandbox {

    private static final long serialVersionUID = -3566205153979731515L;
    private final JButton bApply;
    private final DesktopGUI mainGUI;
    private final JButton bSave;
    private final JButton bCancel;
    private ExtendedGridEditor extendedGridEditor;

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
        this.bSave = SandboxTools.newJButton("SAVE", "Save a part of the grid like standard RLE");
        this.bCancel = SandboxTools.newJButton("Cancel", "Exit from save mode");
        this.bCancel.setEnabled(false);
        final JPanel mySouth = new JPanel(new FlowLayout());
        mySouth.add(bSave);
        mySouth.add(bCancel);
        super.getSouthPanel().add(mySouth, BorderLayout.CENTER);
        this.bSave.addActionListener(e -> save());
        this.bCancel.addActionListener(e -> cancel());
    }

    /**
     * Creates a {@link JGridPanel} as GridPanel.
     */
    @Override
    protected JGridPanel buildGrid(final int cellSize) {
        return new JGridPanel(ResourceLoader.loadConstantInt("sandbox.grid.size"),
                ResourceLoader.loadConstantInt("sandbox.grid.size"), cellSize);
    }

    /**
     * Creates a {@link GridEditorImpl} as editor.
     */
    @Override
    protected PatternEditor buildEditor(final GridPanel grid) {
        this.extendedGridEditor = new ExtendedGridEditorImpl(grid);
        return this.extendedGridEditor;
    }

    /**
     * Creates a {@link ExtendedGenerationPanel} as generation panel.
     */
    @Override
    protected GenerationPanel buildGenerationPanel() {
        return new ExtendedGenerationPanel(this);
    }

    /**
     * Sets the enabled property of the button apply to the given flag.
     * 
     * @param flag
     *            a boolean flag
     */
    @Override
    public void setButtonApplyEnabled(final boolean flag) {
        this.bApply.setEnabled(flag);
    }

    /**
     * 
     * @param flag set the save button enable or disable
     */
    public void setSaveEnable(final boolean flag) {
        if (flag) {
            this.cancel();
            this.bSave.setEnabled(true);
        } else {
            this.bCancel.setEnabled(false);
            this.bSave.setEnabled(false);
        }
    }

    private void save() {
        if (this.extendedGridEditor.isCutReady()) {
            this.setVisible(false);
            this.mainGUI.popUpFrame(new JInternalFrameSave(() -> {
                this.setVisible(true);
                this.cancel();
            }, this.extendedGridEditor));
        } else {
            this.extendedGridEditor.selectMode(true);
            this.bCancel.setEnabled(true);
        }
    }

    private void cancel() {
        this.extendedGridEditor.cancelSelectMode();
        this.bCancel.setEnabled(false);
    }
}
