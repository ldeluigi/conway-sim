package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Lightweight rectangular swing {@link JComponent}.
 */
public class JCell extends JComponent {

    private static final long serialVersionUID = 6660492274105056428L;

    /**
     * Is the constructor method which instantiates a new JCell.
     * 
     * @param size
     *            is the {@link Dimension} that those labels will use to be shown
     * @param col
     *            is the {@link Color} that will be displayed
     */
    public JCell(final Dimension size, final Color col) {
        super();
        this.setSize(size);
        this.setPreferredSize(size);
        this.setBackground(col);
        this.setOpaque(true);
    }

    /**
     * Sets {@link Graphics} color, draws a rectangle with the same color as background, calls
     * paintBorder on the {@link javax.swing.border.Border Border}.
     */
    @Override
    public void paintComponent(final Graphics g) {
        g.setColor(this.getBackground());
        if (this.isOpaque()) {
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        this.getBorder().paintBorder(this, g, 0, 0, this.getWidth(), this.getHeight());
    }
}
