package view.swing.sandbox;

/**
 * Static class to get string for GenerationPanel.
 */
public final class GenerationPanelString {

    private GenerationPanelString() { }

    /**
     * 
     * @param attribute the attribute required
     * @return the attribute String of the button Start
     */
    public static String buttonStart(final ButtonAttributeString attribute) {
        if (attribute == ButtonAttributeString.TEXT) {
            return "Start";
        } else if (attribute == ButtonAttributeString.TOOL_TIP_TEXT) {
            return "Start the game";
        }
        throw new IllegalArgumentException();
    }

    /**
     * 
     * @param attribute the attribute required
     * @return the attribute String of the button Stop
     */
    public static String buttonStop(final ButtonAttributeString attribute) {
        if (attribute == ButtonAttributeString.TEXT) {
            return "Stop";
        } else if (attribute == ButtonAttributeString.TOOL_TIP_TEXT) {
            return "Stop the game and clear the view";
        }
        throw new IllegalArgumentException();
    }

    /**
     * enumeration for attribute of button String.
     * 
     */
    public enum ButtonAttributeString {

        /**
         * text of the button.
         */
        TEXT,

        /**
         * text of the tooltip of the button.
         */
        TOOL_TIP_TEXT;
    }
}
