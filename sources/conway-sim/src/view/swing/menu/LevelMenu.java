package view.swing.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import controller.io.LevelLoader;
import controller.io.ResourceLoader;
import core.campaign.Editable;
import core.campaign.Level;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.Colors;
import view.swing.DesktopGUI;
import view.swing.sandbox.JGridPanel;
import view.swing.sandbox.KeyListenerFactory;
import view.swing.sandbox.SandboxBuilder;
import view.swing.sandbox.SandboxTools;

/**
 * JPanel with level list.
 */
public class LevelMenu extends JPanel {

    private static final long serialVersionUID = -6668213230963613342L;
    private static final int INITIAL_GRID_SIZE = 50;
    private static final int GRID_TO_CELL_RATIO = 7;
    private static final String LEVEL_NUMBER = "level.number";
    private static final String VALUE = "XXX";
    private static final int LEVEL_FOR_PAGE = 4;
    private final List<JButton> bList = new LinkedList<>();
    private final DesktopGUI mainGUI;
    private final JGridPanel gridPanel;
    private final JTabbedPane cardPanel;
    private int currentLevel;

    /**
     * 
     * @param mainGUI
     *            the main DesktopGUI that calls this LevelMenu
     */
    public LevelMenu(final DesktopGUI mainGUI) {
        this.setOpaque(false);
        this.mainGUI = mainGUI;
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize() * 2));

        IntStream.rangeClosed(1, ResourceLoader.loadConstantInt(LEVEL_NUMBER)).forEach(n -> {
            final JButton b = SandboxTools.newJButton(
                    ResourceLoader.loadString("level.button").replaceAll(VALUE, String.valueOf(n)), this.getFont());
            b.setFont(this.getFont());
            this.bList.add(b);
            b.addActionListener(e -> {
                this.currentLevel = n;
                pressButton(b);
            });
        });

        this.cardPanel = new JTabbedPane();
        this.cardPanel.setOpaque(false);
        for (int i = 0; i < ResourceLoader.loadConstantInt(LEVEL_NUMBER) / LEVEL_FOR_PAGE
                + (ResourceLoader.loadConstantInt(LEVEL_NUMBER) % LEVEL_FOR_PAGE == 0 ? 0 : 1); i++) {
            this.cardPanel.addTab(ResourceLoader.loadString("level.page").replace(VALUE, String.valueOf(i)), panelLevel(i));
        }

        this.setLayout(new BorderLayout());
        final JPanel central = new JPanel(new FlowLayout());
        central.setOpaque(false);
        final JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(cardPanel);
        rightPanel.add(buildRightLeftButtonPanel());

        final JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setOpaque(false);

        this.gridPanel = new JGridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE,
                INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        statusPanel.add(this.gridPanel);
        this.gridPanel.setPreferredSize(new Dimension(this.mainGUI.getCurrentWidth() / 4, this.mainGUI.getCurrentHeight() / 4));
        central.add(statusPanel);
        central.add(rightPanel);

        final JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setOpaque(false);
        final JButton bStart = SandboxTools
                .newJButton(ResourceLoader.loadString("level.button.start"), this.getFont());
        bStart.setFocusable(false);
        exitPanel.add(bStart);

        final JButton bReturn = SandboxTools
                .newJButton(ResourceLoader.loadString("level.button.return"), this.getFont());
        bReturn.setFocusable(false);
        exitPanel.add(bReturn);
        this.add(central, BorderLayout.CENTER);
        this.add(exitPanel, BorderLayout.AFTER_LAST_LINE);

        bStart.addActionListener(e -> start());
        bReturn.addActionListener(e -> mainGUI.backToMainMenu());
        KeyListenerFactory.addKeyListener(this, "start", KeyEvent.VK_ENTER, () -> bStart.doClick());
        KeyListenerFactory.addKeyListener(this, "return", KeyEvent.VK_ESCAPE, () -> bReturn.doClick());
        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(),
                this.getHeight(), this);
    }

    private JPanel buildRightLeftButtonPanel() {
        final JPanel rightLeftButton = new JPanel(new FlowLayout());
        rightLeftButton.setOpaque(false);
        final JButton right = SandboxTools.newJButton(ResourceLoader.loadString("level.button.right"), this.getFont());
        right.setFocusPainted(false);
        final JButton left = SandboxTools.newJButton(ResourceLoader.loadString("level.button.left"), this.getFont());
        left.setFocusable(false);
        rightLeftButton.add(left);
        rightLeftButton.add(right);

        KeyListenerFactory.addKeyListener(this, "right", KeyEvent.VK_RIGHT, () -> nextPage());
        right.addActionListener(e -> this.nextPage());
        KeyListenerFactory.addKeyListener(this, "left", KeyEvent.VK_LEFT, () -> previousPage());
        left.addActionListener(e -> this.previousPage());
        return rightLeftButton;
    }

    private void previousPage() {
        this.cardPanel.setSelectedIndex(cardPanel.getSelectedIndex() - 1 < 0
                ? 0 : this.cardPanel.getSelectedIndex() - 1);
    }

    private void nextPage() {
        this.cardPanel.setSelectedIndex(this.cardPanel.getSelectedIndex() + 1 >= this.cardPanel.getComponentCount()
                ? this.cardPanel.getSelectedIndex() : this.cardPanel.getSelectedIndex() + 1);
    }

    private void start() {
        if (this.currentLevel != 0) {
            this.mainGUI.setView(new LoadingScreen());
            SwingUtilities.invokeLater(() -> this.mainGUI.setView(SandboxBuilder.buildLevelSandbox(this.mainGUI, this.currentLevel)));
        }
    }

    private void pressButton(final JButton button) {
        this.bList.stream().filter(b -> !b.isEnabled()).forEach(b -> b.setEnabled(true));
        button.setEnabled(false);
        this.gridPanel.setVisible(false);
        final LevelLoader lLoader = new LevelLoader(this.currentLevel);
        final Level level = lLoader.getLevel();
        this.gridPanel.changeGrid(level.getEnvironmentMatrix().getWidth(), level.getEnvironmentMatrix().getHeight());
        final Matrix<Color> colorMatrix = new ListMatrix<>(level.getCellTypeMatrix().getWidth(),
                level.getCellTypeMatrix().getHeight(), () -> null);
        for (int row = 0; row < level.getEnvironmentMatrix().getHeight(); row++) {
            for (int col = 0; col < level.getEnvironmentMatrix().getWidth(); col++) {
                colorMatrix.set(row, col, Colors.cellColor(Editable.EDITABLE, level.getCellTypeMatrix().get(row, col), 
                        level.getInitialStateMatrix().get(row, col)));
            }
        }
        SwingUtilities.invokeLater(() -> this.gridPanel.paintGrid(0, 0, colorMatrix));
        this.gridPanel.setVisible(true);
    }

    private JPanel panelLevel(final int pageNumber) {
        final JPanel gridLevel = new JPanel();
        gridLevel.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.ipadx = 1;
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 0;
        gridLevel.setOpaque(false);
        for (int i = LEVEL_FOR_PAGE * pageNumber; i < LEVEL_FOR_PAGE * pageNumber
                + LEVEL_FOR_PAGE; i++) {
            if (i > ResourceLoader.loadConstantInt(LEVEL_NUMBER) - 1) {
                return gridLevel;
            } else {
                if (bList.size() > i) {
                    c.gridx = i % LEVEL_FOR_PAGE % 2 == 0 ? 0 : 1;
                    c.gridy = i % LEVEL_FOR_PAGE / 2;
                    gridLevel.add(bList.get(i), c);
                }
            }
        }
        return gridLevel;
    }
}
