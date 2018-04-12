package view.swing.level;

import java.awt.Graphics;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import controller.io.ResourceLoader;

public class LevelComplete extends JInternalFrame {

	private static final long serialVersionUID = -895123634745639926L;

	public LevelComplete() {
		super(ResourceLoader.loadString("level.complete.frame.title"), false, true, true, false);
		final JPanel jp = new JPanel() {
			private static final long serialVersionUID = 7156522143177179412L;
			@Override
		    public void paintComponent(final Graphics g) {
		        g.drawImage(ResourceLoader.loadImage("level.complete.background"), 0, 0, this.getWidth(), this.getHeight(), this);
		    }
		};
		this.setContentPane(jp);
	}
}
