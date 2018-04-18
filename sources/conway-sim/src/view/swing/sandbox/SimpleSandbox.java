package view.swing.sandbox;


import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.editor.GridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.ResourceLoader;
import view.DesktopGUI;
import view.swing.menu.MenuSettings;

/**
 * 
 *
 */
public class SimpleSandbox extends AbstractSandbox {

    private static final long serialVersionUID = -3566205153979731515L;
    private final JButton bApply;

    /**
     * 
     * @param mainGUI a
     */
    public SimpleSandbox(final DesktopGUI mainGUI) {
        super(mainGUI);
        this.bApply = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.apply"),
                ResourceLoader.loadString("sandbox.apply.tooltip"));
        final JPanel gridOption = SandboxTools.newGridOptionDimension(this, bApply,
                new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        gridOption.setOpaque(false);
        super.getNorthPanel().add(gridOption, BorderLayout.WEST);
    }

    /**
     * 
     */
    @Override
    protected PatternEditor buildEditor(final GridPanel grid) {
        PatternEditor ge = new GridEditorImpl(super.getGrid());
        return ge;
    }

    /**
     * Enables "apply" button to be clicked by the user.
     * 
     * @param flag
     *            a boolean flag
     */
    public void setButtonApplyEnabled(final boolean flag) {
        this.bApply.setEnabled(flag);
    }

    /**
     * 
     */
    @Override
    protected GenerationPanel buildGenerationPanel(final AbstractSandbox abstractSandbox) {
        return new SandboxGenerationPanel(abstractSandbox);
    }

    @Override
    protected GridPanelImpl buildGrid(int cellSize) {
        return new GridPanelImpl(ResourceLoader.loadConstantInt("sandbox.grid.size"), ResourceLoader.loadConstantInt("sandbox.grid.size"), cellSize);
    }
}
