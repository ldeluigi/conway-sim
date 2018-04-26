package view.swing.menu;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


import controller.io.ResourceLoader;
/**
 * 
 */
public class TutorialBox extends JInternalFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 
     * @param number s
     */
    public TutorialBox(final int number) {
        this.add(new JLabel(new ImageIcon(ResourceLoader.loadImage("tutorial.lvl" + number))));
    }

}
