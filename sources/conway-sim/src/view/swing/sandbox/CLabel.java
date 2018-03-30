package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * 
 *
 *
 */
public class CLabel extends JLabel {

    /**
     * 
     */
    private static final long serialVersionUID = 6660492274105056428L;

    /**
     * Is the constructor method which instantiates a new CLabel.
     * @param size is the {@link Dimension} that those labels will use to be shown
     * @param col is the {@link Color} that will be displayed
     */
    public CLabel(final Dimension size, final Color col) {
        super("");
        this.setSize(size);
        this.setPreferredSize(size);
        this.setBackground(col);
        this.setOpaque(true);
    }
}
