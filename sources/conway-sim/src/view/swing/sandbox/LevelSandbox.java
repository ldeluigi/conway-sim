package view.swing.sandbox;


import controller.editor.LevelGridEditorImpl;
import controller.editor.PatternEditor;
import core.campaign.Level;
import core.campaign.LevelImplTest;
import view.DesktopGUI;

/**
 * 
 *
 */
public class LevelSandbox extends SimpleSandbox {

    private static final long serialVersionUID = -3566205153979731515L;
    private final Level level;


    /**
     * 
     * @param mainGUI the GUI that calls this Sandbox
     * @param levelIndex the number describing the level to be loaded
     */
    public LevelSandbox(final DesktopGUI mainGUI, final int levelIndex) {
        super(mainGUI);
        this.level = new LevelImplTest();

    }

    /**
     * Method which creates the manager for the grid, in this case is a GridEditorImpl.
     * 
     * @param grid is the GridPanel which has to be managed
     * @return a new manager for this grid
     */
    @Override
    protected PatternEditor buildEditor(final GridPanel grid) {
        return new LevelGridEditorImpl(grid, this.level);
    }


}
