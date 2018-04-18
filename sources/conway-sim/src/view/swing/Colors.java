package view.swing;

import java.awt.Color;
/**
 * Utility class for colors.
 */
public final class Colors {

    /**
     * Color for gold.
     */
    public static final Color GOLD = new Color(255, 215, 0);

    private Colors() {
    }

    /**
     * Blends two colors into a single one.
     * @param a the first color
     * @param b the second color
     * @return the result
     */
    public static Color blend(final Color a, final Color b) {
        return new Color(
                (a.getRed() * a.getAlpha() + b.getRed() * b.getAlpha())
                        / (a.getAlpha() + b.getAlpha()),
                (a.getGreen() * a.getAlpha() + b.getGreen() * b.getAlpha())
                        / (a.getAlpha() + b.getAlpha()),
                (a.getBlue() * a.getAlpha() + b.getBlue() * b.getAlpha())
                        / (a.getAlpha() + b.getAlpha()),
                (a.getAlpha() + b.getAlpha()) / 2);
    }
}
