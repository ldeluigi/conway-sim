package view.swing;

import javax.swing.JInternalFrame;

/**
 * Represents a GUI which allows to display and manage {@link JInternalFrame}.
 *
 */
public interface DesktopGUI extends GUI {

  /**
   * Shows the frame.
   * @param iFrame to be displayed
   */
  void popUpFrame(JInternalFrame iFrame);

  /**
   * Closes all open frames.
   */
  void closeFrames();
}
