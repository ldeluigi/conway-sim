package view;

import java.util.List;

import javax.swing.JInternalFrame;

/**
 * Represents a GUI which allows to display and manage {@link JInternalFrame}.
 */
public interface DesktopGUI extends GUI {

    /**
     * Shows the frame.
     * 
     * @param iFrame
     *            to be displayed
     */
    void popUpFrame(JInternalFrame iFrame);

    /**
     * Returns all {@link JInternalFrame} attached.
     * 
     * @return the list of all frames attached to the desktopGUI.
     */
    List<JInternalFrame> getAllFrames();

    /**
     * Detaches permanently a open or closed {@link JInternalFrame} from the DesktopGUI.
     * 
     * @param iFrame
     *            the {@link JInternalFrame} to detach
     */
    void detachFrame(JInternalFrame iFrame);
}