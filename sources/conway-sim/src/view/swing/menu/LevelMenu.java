package view.swing.menu;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import controller.io.ResourceLoader;
import view.swing.DesktopGUI;
import view.swing.Log;
import view.swing.sandbox.GridPanel;
import view.swing.sandbox.KeyListenerFactory;
import view.swing.sandbox.SandboxTools;

/**
 * 
 */
public class LevelMenu extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -6668213230963613342L;
    private static final int INITIAL_GRID_SIZE = 50;
    private static final int GRID_TO_CELL_RATIO = 10;
    private static final int ELEMENT_FOR_PAGE = 9;
    private static final String VALUE = "XXX";
    private final JTextArea textArea = new JTextArea(ResourceLoader.loadString("level.default.text"));
    private final List<JButton> bList = new LinkedList<>();
    private Optional<JPanel> rightLeftButton = Optional.empty();
    private Optional<JPanel> gridLevel = Optional.empty();
    private int currentPage;
    private JButton pressedButton;

    /**
     * 
     * @param mainGUI the main gui
     */
    public LevelMenu(final DesktopGUI mainGUI) {
        this.setOpaque(false);
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() * 2));
        textArea.setBorder(new TitledBorder(ResourceLoader.loadString("level.description")));
        textArea.setEditable(false);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        IntStream.range(0, ResourceLoader.loadConstantInt("level.number"))
        .mapToObj(n -> SandboxTools.newJButton(
                String.valueOf(ResourceLoader.loadString("level.button").replaceAll(VALUE, String.valueOf(n))),
                this.getFont()))
        .forEach(b -> {
            b.setFont(this.getFont());
            bList.add(b);
            b.setFocusable(false);
            b.addActionListener(e -> {
                pressButton(b);
                try {
                    textArea.setText(
                            b.getText() + System.lineSeparator() + (ResourceLoader.loadString(b.getText())));
                } catch (java.util.MissingResourceException ex) {
                    textArea.setText(b.getText() + System.lineSeparator()
                            + (ResourceLoader.loadString("level.button.text")));
                }
            });
        });

        this.add(this.panelLevel(currentPage));
        this.add(getRightLeftButtonPanel());

        final JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(textArea);

        final GridPanel pg = new GridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE, INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        statusPanel.add(pg);
        this.add(statusPanel);

        final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        final JButton bStart = SandboxTools.newJButton("START", this.getFont());
        bStart.setFocusable(false);
        southPanel.add(bStart);

        final JButton bReturn = SandboxTools.newJButton("RETURN", this.getFont());
        bReturn.setFocusable(false);
        southPanel.add(bReturn);
        this.add(southPanel);

        bStart.addActionListener(e -> start());
        bReturn.addActionListener(e -> mainGUI.setView(new MainMenu(mainGUI)));
        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private JPanel getRightLeftButtonPanel() {
        if (!rightLeftButton.isPresent()) {
            this.rightLeftButton = Optional.of(new JPanel(new FlowLayout()));
            this.rightLeftButton.get().setOpaque(false);
            final JButton right = SandboxTools.newJButton("NEXT", this.getFont());
            right.setFocusPainted(false);
            final JButton left = SandboxTools.newJButton("PREV", this.getFont());
            left.setFocusable(false);
            this.rightLeftButton.get().add(left);
            this.rightLeftButton.get().add(right);

            KeyListenerFactory.addKeyListener(this, "right", KeyEvent.VK_RIGHT, () -> nextPage());
            left.addActionListener(e -> nextPage());
            KeyListenerFactory.addKeyListener(this, "left", KeyEvent.VK_LEFT, () -> previousPage());
            right.addActionListener(e -> this.previousPage());
        }
        return this.rightLeftButton.get();
    }

    private void previousPage() {
        if (this.currentPage > 0) {
            this.currentPage--;
            panelLevel(this.currentPage);
        }
    }

    private void nextPage() {
        if (this.currentPage < ResourceLoader.loadConstantInt("level.number") / (ELEMENT_FOR_PAGE)) {
            this.currentPage++;
            panelLevel(this.currentPage);
        }
    }

    private void start() {
        Log.logMessage(this.pressedButton.getText());
    }

    private void pressButton(final JButton button) {
        bList.stream().filter(b -> !b.isEnabled()).forEach(b -> b.setEnabled(true));
        button.setEnabled(false);
        this.pressedButton = button;
    }

    private JPanel panelLevel(final int pageNumber) {
        if (!gridLevel.isPresent()) {
            this.gridLevel = Optional.of(new JPanel());
            this.gridLevel.get().setLayout(new GridBagLayout());
            this.gridLevel.get().setOpaque(false);
        }
        this.gridLevel.get().setVisible(false);
        this.gridLevel.get().removeAll();
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridy = 0;
        c.gridx = -1;
        c.insets = new Insets(10, 10, 10, 10);
        for (int i = ELEMENT_FOR_PAGE * pageNumber; i < ELEMENT_FOR_PAGE * pageNumber + ELEMENT_FOR_PAGE; i++) {
            JButton b;
            if (i >= ResourceLoader.loadConstantInt("level.number")) {
                this.gridLevel.get().setVisible(true);
                return gridLevel.get();
            } else {
                b = bList.get(i);
            }
            c.gridx++;
            if (c.gridx >= 3) {
                c.gridx = 0;
                c.gridy++;
                if (c.gridy > 3) {
                    throw new IllegalStateException("Too much element!");
                }
            }
            this.gridLevel.get().add(b, c);
        }
        this.gridLevel.get().setVisible(true);
        return gridLevel.get();
    }
}
