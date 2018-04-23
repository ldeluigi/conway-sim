package view.swing.sandbox;

import java.util.Objects;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import controller.editor.LevelGridEditorImpl;
import controller.editor.PatternEditor;
import controller.io.LevelLoader;
import controller.io.ResourceLoader;
import core.campaign.Level;
import view.swing.DesktopGUI;
import view.swing.GridPanel;
import view.swing.book.CampaignBookFrame;
import view.swing.level.LevelComplete;

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
        final LevelLoader levelLoader = new LevelLoader(level);
        final Level l = levelLoader.getLevel();
        final int h = l.getEnvironmentMatrix().getHeight();
        final int w = l.getEnvironmentMatrix().getWidth();
        return new AbstractSandbox(gui) {
            private static final long serialVersionUID = 1L;
            private CampaignBookFrame book;

            @Override
            protected JGridPanel buildGrid(final int cellSize) {
                return new JGridPanel(w, h, cellSize);
            }

            @Override
            protected GenerationPanel buildGenerationPanel() {
                return new GenerationPanel(this, () -> {
                    gui.popUpFrame(new LevelComplete(), false);
                });
            }

            @Override
            protected PatternEditor buildEditor(final GridPanel gridp) {
                return new LevelGridEditorImpl(gridp, l);
            }

            @Override
            protected String getTitle() {
                return ResourceLoader.loadString("level.button").replaceAll("XXX", Integer.toString(level));
            }

            @Override
            protected JInternalFrame callBook() {
                if (Objects.isNull(this.book)) {
                    this.book = new CampaignBookFrame(this.getGridEditor(), levelLoader);
                    gui.popUpFrame(this.book, false);
                } else if (book.isClosed()) {
                    gui.detachFrame(this.book);
                    this.book = new CampaignBookFrame(this.getGridEditor(), levelLoader);
                    gui.popUpFrame(this.book, false);
                }
                return this.book;
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
