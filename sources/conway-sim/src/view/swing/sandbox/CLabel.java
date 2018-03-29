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
     * 
     */
    public CLabel(final Dimension size, final Color col, final String t) {
        super(t);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setBackground(col);
        this.setOpaque(true);
    }
}
