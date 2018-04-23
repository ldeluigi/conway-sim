package view.swing.sandbox;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import controller.editor.CMouseListener;

/**
 * The Adapter which joins events from user interaction to the correct
 * implementation using Swing.
 *
 */
public class SwingMouseListener implements MouseListener {

    private final CMouseListener listener;

    /**
     * Constructor method for a new adapter which uses Swing.
     * 
     * @param l
     *            the listener to be joined to the real implementation
     */
    public SwingMouseListener(final CMouseListener l) {
        this.listener = l;
    }

    /**
     * This method is not supported.
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    /**
     * Is the method which notifies where and how the user interacted with the grid.
     * 
     * @param e
     *            the event generated as result of the interaction
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (e.isControlDown()) {
                this.listener.mousePressedIsLeftWithControl();
            } else {
                this.listener.mousePressedIsLeftWithoutControl();
            }
        } else {
            if (e.isControlDown()) {
                this.listener.mousePressedisRightWithControl();
            } else {
                this.listener.mousePressedisRightWithoutControl();
            }
        }
    }

    /**
     * Is the method which notifies when mouse's left button is released.
     * 
     * @param e
     *            the event generated as result of the interaction with the grid
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            this.listener.mousereleasedleft();
        }
    }

    /**
     * Is the method which notifies when the user's cursor enters a cell of the
     * grid.
     * 
     * @param e
     *            the event generated as result of the interaction
     */
    @Override
    public void mouseEntered(final MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            this.listener.mouseenteredwhilepressingleft();
        } else {
            this.listener.mouseenteredwhilepressingright();
        }
    }

    /**
     * This method is not supported.
     */
    @Override
    public void mouseExited(final MouseEvent e) {
    }

}
