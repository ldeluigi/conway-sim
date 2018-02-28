package view.swing;

import javax.swing.JInternalFrame;

public interface DesktopGUI extends GUI {

  void popUpFrame(JInternalFrame iFrame);

  void closeFrames();
}
