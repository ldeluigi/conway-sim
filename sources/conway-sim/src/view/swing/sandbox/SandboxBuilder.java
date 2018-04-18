package view.swing.sandbox;

import controller.editor.LevelGridEditorImpl;
import controller.editor.PatternEditor;
import core.campaign.Level;
import core.campaign.LevelImplTest;
import view.DesktopGUI;
import view.Sandbox;

public final class SandboxBuilder {

    private SandboxBuilder() {
    }
    
    public static AbstractSandbox build(DesktopGUI gui, int level) {
        Level l = new LevelImplTest();
        int h = l.getEnvironmentMatrix().getHeight();
        int w = l.getEnvironmentMatrix().getWidth();
        return new AbstractSandbox(gui) {
            private static final long serialVersionUID = 1L;

            @Override
            protected GridPanelImpl buildGrid(int cellSize) {
                return new GridPanelImpl(w, h, cellSize);
            }
            
            @Override
            protected GenerationPanel buildGenerationPanel(AbstractSandbox abstractSandbox) {
                return new GenerationPanel(abstractSandbox);
            }
            
            @Override
            protected PatternEditor buildEditor(GridPanel gridp) {
                return new LevelGridEditorImpl(gridp, l);
            }
        };
    }

}
