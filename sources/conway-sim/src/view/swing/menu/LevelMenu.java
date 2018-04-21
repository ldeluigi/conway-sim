package view.swing.menu;

import java.awt.Color;
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
import core.campaign.CellType;
import core.campaign.Level;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.DesktopGUI;
import view.swing.Colors;
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
    private static final int GRID_TO_CELL_RATIO = 10;
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

        IntStream.rangeClosed(1, ResourceLoader.loadConstantInt("level.number")).forEach(n -> {
            final JButton b = SandboxTools.newJButton(
                    ResourceLoader.loadString("level.button").replaceAll(VALUE, String.valueOf(n)), this.getFont());
            b.setFont(this.getFont());
            bList.add(b);
            b.addActionListener(e -> {
                currentLevel = n;
                pressButton(b);
            });
        });

        cardPanel = new JTabbedPane();
        cardPanel.setOpaque(false);
        for (int i = 0; i < ResourceLoader.loadConstantInt("level.number") / LEVEL_FOR_PAGE
                + (ResourceLoader.loadConstantInt("level.number") % LEVEL_FOR_PAGE == 0 ? 0 : 1); i++) {
            cardPanel.addTab(ResourceLoader.loadString("level.page").replace(VALUE, String.valueOf(i)), panelLevel(i));
        }

        this.setLayout(new GridBagLayout());
        final JPanel central = new JPanel(new FlowLayout());
        central.setOpaque(false);
        final JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(cardPanel);
        rightPanel.add(buildRightLeftButtonPanel());

        final JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setOpaque(false);

        gridPanel = new JGridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE,
                INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        statusPanel.add(gridPanel);
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
        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        this.add(central, c);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        this.add(exitPanel, c);

        bStart.addActionListener(e -> start());
        bReturn.addActionListener(e -> mainGUI.backToMainMenu());
        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(),
                this.getHeight(), this);
    }

    private JPanel buildRightLeftButtonPanel() {
        JPanel rightLeftButton = new JPanel(new FlowLayout());
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
        cardPanel.setSelectedIndex(cardPanel.getSelectedIndex() - 1 < 0
                ? 0 : cardPanel.getSelectedIndex() - 1);
    }

    private void nextPage() {
        cardPanel.setSelectedIndex(cardPanel.getSelectedIndex() + 1 >= cardPanel.getComponentCount()
                ? cardPanel.getSelectedIndex() : cardPanel.getSelectedIndex() + 1);
    }

    private void start() {
        if (this.currentLevel != 0) {
            this.mainGUI.setView(SandboxBuilder.buildLevelSandbox(mainGUI, currentLevel));
        }
    }

    private void pressButton(final JButton button) {
        this.bList.stream().filter(b -> !b.isEnabled()).forEach(b -> b.setEnabled(true));
        button.setEnabled(false);
        gridPanel.setVisible(false);
        final LevelLoader lLoader = new LevelLoader(currentLevel);
        final Level level = lLoader.getLevel();
        gridPanel.changeGrid(level.getEnvironmentMatrix().getWidth(), level.getEnvironmentMatrix().getHeight());
        Matrix<Color> mc = new ListMatrix<>(level.getCellTypeMatrix().getWidth(),
                level.getCellTypeMatrix().getHeight(), () -> null);
        for (int row = 0; row < level.getCellTypeMatrix().getWidth(); row++) {
            for (int col = 0; col < level.getCellTypeMatrix().getWidth(); col++) {
                Color value;
                if (level.getCellTypeMatrix().get(row, col).equals(CellType.NORMAL)) {
                    value = level.getInitialStateMatrix().get(row, col).equals(Status.ALIVE) ? Color.BLACK
                            : Color.WHITE;
                } else if (level.getCellTypeMatrix().get(row, col)
                        .equals(CellType.GOLDEN)) {
                    value = Colors.blend(Colors.GOLD,
                            level.getInitialStateMatrix().get(row, col).equals(Status.ALIVE) ? Color.BLACK
                                    : Color.WHITE);
                } else if (level.getCellTypeMatrix().get(row, col)
                        .equals(CellType.WALL)) {
                    value = level.getInitialStateMatrix().get(row, col).equals(Status.ALIVE) ? Color.DARK_GRAY
                            : Color.LIGHT_GRAY;
                } else {
                    value = level.getInitialStateMatrix().get(row, col).equals(Status.ALIVE) ? Color.BLACK
                            : Color.WHITE;
                }
                mc.set(row, col, value);
            }
        }
        SwingUtilities.invokeLater(() -> gridPanel.paintGrid(0, 0, mc));
        gridPanel.setVisible(true);
    }

    private JPanel panelLevel(final int pageNumber) {
        JPanel gridLevel = new JPanel();
        gridLevel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.ipadx = 1;
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 0;
        gridLevel.setOpaque(false);
        for (int i = LEVEL_FOR_PAGE * pageNumber; i < LEVEL_FOR_PAGE * pageNumber
                + LEVEL_FOR_PAGE; i++) {
            if (i > ResourceLoader.loadConstantInt("level.number") - 1) {
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
