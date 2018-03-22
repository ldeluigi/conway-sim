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
//TODO make class static
public class KeyListenerFactory {

    private final InputMap inputMap;
    private final ActionMap actionMap;

    /**
     * 
     * @param panel the panel that have to contain the key listener
     */
    public KeyListenerFactory(final JPanel panel) {
        this.inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.actionMap = panel.getActionMap();
    }

    /**
     * 
     * @param name the unique name of this listener
     * @param event the event
     * @param keyCode the KeyEvent that start the event
     * @param modifier a bitwise-ored combination of any modifiers
     */
    public void addKeyListener(final String name, final Runnable event, final int keyCode, final int modifier) {
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
     * @param name the name of this listener
     * @param event the event
     * @param keyCode the KeyEvent that start the event
     */
    public void addKeyListener(final String name, final Runnable event, final int keyCode) {
        this.addKeyListener(name, event, keyCode, 0);
    }
}
