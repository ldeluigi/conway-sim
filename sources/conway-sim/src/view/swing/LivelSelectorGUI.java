package view.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import controller.io.ResourceLoader;
import view.swing.menu.LoadingScreen;
import view.swing.menu.MenuSettings;
import view.swing.sandbox.Sandbox;
import view.swing.sandbox.SandboxTools;

/**
 * 
 */
public class LivelSelectorGUI extends JPanel {

    private static final int BUTTON_PERCENT_DISTANT = 30;
    private static final int BUTTON_PERCENT_SIZE = 10;
    private static final int SCROLL_PANE_WIDTH = 6;
    private static final int SCROLL_PANE_HEIGHT = 2;
    private static final String VALUE = "XXX";
    private final ButtonGroup group = new ButtonGroup();
    private final DesktopGUI mainGUI;
    /**
     * 
     */
    private static final long serialVersionUID = -6668213230963613342L;

    /**
     * 
     * @param mainGUI the main gui
     */
    public LivelSelectorGUI(final DesktopGUI mainGUI) {
        this.mainGUI = mainGUI;
        final int h = mainGUI.getCurrentHeight();
        final int w = mainGUI.getCurrentWidth();
        final Dimension buttonDim = new Dimension(w / BUTTON_PERCENT_SIZE, h / BUTTON_PERCENT_SIZE);
        final JButton bStart = new JButton(ResourceLoader.loadString("level.start"));
        final JRadioButton bSandbox = SandboxTools.newJRadioButton(ResourceLoader.loadString("level.sandbox"), buttonDim);
        group.add(bSandbox);
        final JRadioButton bLiv1 = SandboxTools.newJRadioButton(ResourceLoader.loadString("level.button").replaceAll(VALUE, "1"), buttonDim);
        group.add(bLiv1);
        final JRadioButton bLiv2 = SandboxTools.newJRadioButton(ResourceLoader.loadString("level.button").replaceAll(VALUE, "2"), buttonDim);
        group.add(bLiv2);
        final JRadioButton bLiv3 = SandboxTools.newJRadioButton(ResourceLoader.loadString("level.button").replaceAll(VALUE, "3"), buttonDim);
        group.add(bLiv3);
        this.setLayout(new FlowLayout());

        final JPanel pEastInternal = new JPanel(new GridBagLayout());
        pEastInternal.setBorder(new TitledBorder(" Level selector "));
        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.gridy = 0;
        cnst.insets = new Insets(
                buttonDim.height / BUTTON_PERCENT_DISTANT,
                buttonDim.width / BUTTON_PERCENT_DISTANT,
                buttonDim.height / BUTTON_PERCENT_DISTANT,
                buttonDim.width / BUTTON_PERCENT_DISTANT);
        cnst.fill = GridBagConstraints.HORIZONTAL;
        pEastInternal.add(bSandbox, cnst);
        cnst.gridy++;
        pEastInternal.add(bLiv1, cnst);
        cnst.gridy++;
        pEastInternal.add(bLiv2, cnst);
        cnst.gridy++;
        pEastInternal.add(bLiv3, cnst);

        final JScrollPane scrollPane = new JScrollPane(pEastInternal);
        scrollPane.setPreferredSize(new Dimension(w / SCROLL_PANE_WIDTH, h / SCROLL_PANE_HEIGHT));
        this.add(scrollPane);

        final JTextPane textArea = new JTextPane();
        textArea.setBorder(new TitledBorder(" Description "));
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(w / 3, h / 2));
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        textArea.setText(ResourceLoader.loadString("level.default.text"));
        this.add(textArea);
        this.add(bStart);

        bSandbox.addActionListener(e -> textArea.setText("Sandbox mode" + System.lineSeparator() + "A free mode."));
        bLiv1.addActionListener(e -> textArea.setText(ResourceLoader.loadString("level.button.text").replaceAll(VALUE, "1")));
        bLiv2.addActionListener(e -> textArea.setText(ResourceLoader.loadString("level.button.text").replaceAll(VALUE, "2")));
        bLiv3.addActionListener(e -> textArea.setText(ResourceLoader.loadString("level.button.text").replaceAll(VALUE, "3")));
        bStart.addActionListener(e -> start());
    }

    private void start() {
        final Enumeration<AbstractButton> enumeration = this.group.getElements();
        AbstractButton button = new JButton();
        while (enumeration.hasMoreElements()) {
            final AbstractButton b = enumeration.nextElement();
            if (b.isSelected()) {
                button = b;
            }
        }
        if (button.getText().equals(ResourceLoader.loadString("level.sandbox"))) {
            mainGUI.setView(new LoadingScreen());
            SwingUtilities.invokeLater(() -> {
                mainGUI.setView(new Sandbox(mainGUI));
            });
        }
    }

}
