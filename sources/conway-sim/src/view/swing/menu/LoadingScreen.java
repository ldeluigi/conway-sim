package view.swing.menu;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.io.ResourceLoader;

/**
 * {@link JPanel} representing a simple loading screen.
 */
public final class LoadingScreen extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int TITLE_SIZE = 80;

	/**
	 * Constructor that builds the scene.
	 */
	public LoadingScreen() {
		super(new GridBagLayout());
		final JLabel loading = new JLabel(ResourceLoader.loadString("main.loading"));
		loading.setFont(new Font(Font.DIALOG, Font.ITALIC, TITLE_SIZE / 2 + MenuSettings.getFontSize()));
		this.add(loading);
	}

	@Override
	public void paintComponent(final Graphics g) {
		g.drawImage(ResourceLoader.loadImage("loading.background"), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
