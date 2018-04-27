package view.swing.menu;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import controller.io.ResourceLoader;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JButton;
/**
 * Tutorial JInternalFrame with gifs and a button next.
 */
public class TutorialBox extends JInternalFrame {

    private static final String TUTORIAL_RESOURCE = "tutorial.lvl";
    private static final long serialVersionUID = 1L;
    private static final int MAX = 3;
    private int current;

    /**
     * Main constructor.
     */
    public TutorialBox() {
        super("Tutorial", true, true, true, true);
        this.setLayout(new BorderLayout());
        final Icon image = ResourceLoader.loadImageIcon(TUTORIAL_RESOURCE + this.current);
        final JLabel jl = new JLabel(image);
        this.add(jl, BorderLayout.CENTER);
        final JButton next = new JButton(ResourceLoader.loadString("tutorial.next"));
        next.addActionListener(e -> {
            this.current = (this.current + 1) % MAX;
            jl.setIcon(ResourceLoader.loadImageIcon(TUTORIAL_RESOURCE + this.current));
        });
        this.add(next, BorderLayout.SOUTH);
    }

}
