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
 * This is a {@link AbstractSandbox sandbox} that allows grid resize and free play.
 */
public class SimpleSandbox extends AbstractSandbox implements ResizableSandbox {

    private static final long serialVersionUID = -3566205153979731515L;
    private final JButton bApply;

    /**
     * Calls super and the adds the grid option panel.
     * 
     * @param mainGUI
     *            the main gui that displays this panel
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
        return new GridEditorImpl(grid);
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
}
