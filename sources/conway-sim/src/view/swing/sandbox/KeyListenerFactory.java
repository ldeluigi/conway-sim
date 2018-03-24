package view.swing.sandbox;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * 
 */
public final class KeyListenerFactory {

    private KeyListenerFactory() { }

    /**
     * 
     * @param panel the panel that should contain the keyListener 
     * @param name the unique name of this listener
     * @param keyCode the int (KeyEvent.VK_*) that start the event
     * @param modifier a bitwise-ored combination of any modifiers
     * @param event the event
     */
    public static void addKeyListener(final JPanel panel, final String name, final int keyCode, final int modifier, final Runnable event) {
        final InputMap inputMap = panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final ActionMap actionMap = panel.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keyCode, modifier), name);
        actionMap.put(name, new AbstractAction() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

        @Override
           public void actionPerformed(final ActionEvent arg0) {
              event.run();
           }
        });
    }

    /**
     * 
     * @param panel the JPanel
     * @param name the name of this listener
     * @param keyCode the int (KeyEvent.VK_*) that start the event
     * @param event the event
     */
    public static void addKeyListener(final JPanel panel, final String name, final int keyCode, final Runnable event) {
        KeyListenerFactory.addKeyListener(panel, name, keyCode, 0, event);
    }

}
