package view.swing.sandbox;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * An utility class to create KeyListeners easily.
 */
public final class KeyListenerFactory {

    private KeyListenerFactory() {
    }

    /**
     * 
     * @param component
     *            the external component that should contain the keyListener
     * @param name
     *            the unique name of this listener
     * @param keyCode
     *            the int (KeyEvent.VK_*) that should start the event
     * @param modifier
     *            a bitwise-ored combination of any modifiers
     * @param event
     *            the event to link with the keyCode
     */
    public static void addKeyListener(final JComponent component, final String name, final int keyCode,
            final int modifier, final Runnable event) {
        final InputMap inputMap = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final ActionMap actionMap = component.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keyCode, modifier), name);
        actionMap.remove(name);
        actionMap.put(name, new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                actionMap.remove(name);
                event.run();
                actionMap.put(name, this);
            }
        });
    }

    /**
     * 
     * @param component
     *            the external component
     * @param name
     *            the name of this listener
     * @param keyCode
     *            the int (KeyEvent.VK_*) that start the event
     * @param event
     *            the event to link with the keyCode
     */
    public static void addKeyListener(final JComponent component, final String name, final int keyCode,
            final Runnable event) {
        KeyListenerFactory.addKeyListener(component, name, keyCode, 0, event);
    }

}
