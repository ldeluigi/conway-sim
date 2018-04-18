package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.io.ResourceLoader;
import view.DesktopGUI;
import view.swing.menu.MenuSettings;

/**
 * 
 *
 */
public class ExtendedSandbox extends SimpleSandbox {

    /**
     * 
     */
    private static final long serialVersionUID = -1728786212569255750L;
    private final JButton bApply;

    /**
     * 
     * @param mainGUI the GUI that calls this Sandbox
     */
    public ExtendedSandbox(final DesktopGUI mainGUI) {
        super(mainGUI);
        this.bApply = SandboxTools.newJButton(ResourceLoader.loadString("sandbox.apply"),
                ResourceLoader.loadString("sandbox.apply.tooltip"));
        final JPanel gridOption = SandboxTools.newGridOptionDimension(this, bApply,
                new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        gridOption.setOpaque(false);
        super.getNorthPanel().add(gridOption, BorderLayout.WEST);
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

}
