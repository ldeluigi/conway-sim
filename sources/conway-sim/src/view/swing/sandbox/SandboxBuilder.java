package view.swing.sandbox;

import javax.swing.JPanel;

import controller.editor.LevelGridEditorImpl;
import controller.editor.PatternEditor;
import core.campaign.Level;
import core.campaign.LevelImplTest;
import view.DesktopGUI;

/**
 * Factory that creates sandbox JPanels.
 */
public final class SandboxBuilder {

    private SandboxBuilder() {
    }

    /**
     * Creates the sandbox for level mode.
     * 
     * @param gui
     *            the gui that hosts the sandbox
     * @param level
     *            the integer corresponding to the level to load
     * @return the builded sandbox as JPanel on success
     */
    public static JPanel buildLevelSandbox(final DesktopGUI gui, final int level) {
        final Level l = new LevelImplTest(); // TODO LEVEL LOADER
        final int h = l.getEnvironmentMatrix().getHeight();
        final int w = l.getEnvironmentMatrix().getWidth();
        return new AbstractSandbox(gui) {
            private static final long serialVersionUID = 1L;

            @Override
            protected JGridPanel buildGrid(final int cellSize) {
                return new JGridPanel(w, h, cellSize);
            }

            @Override
            protected GenerationPanel buildGenerationPanel() {
                return new GenerationPanel(this);
            }

            @Override
            protected PatternEditor buildEditor(final GridPanel gridp) {
                return new LevelGridEditorImpl(gridp, l);
            }
        };
    }

    /**
     * Creates the sandbox for free mode.
     * 
     * @param gui
     *            the gui that hosts the sandbox
     * @return the builded sandbox as JPanel on success
     */
    public static JPanel buildSandbox(final DesktopGUI gui) {
        return new SimpleSandbox(gui);
    }

}
