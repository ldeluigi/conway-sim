package view.swing;

import java.awt.Color;

public class Colors {

	public static Color blend(Color a, Color b) {
		return new Color((a.getRed() * a.getAlpha() + b.getRed() * b.getAlpha()) / (a.getAlpha() + b.getAlpha()),
				(a.getGreen() * a.getAlpha() + b.getGreen() * b.getAlpha()) / (a.getAlpha() + b.getAlpha()),
				(a.getBlue() * a.getAlpha() + b.getBlue() * b.getAlpha()) / (a.getAlpha() + b.getAlpha()),
				(a.getAlpha() + b.getAlpha()) / 2);
	}
}
