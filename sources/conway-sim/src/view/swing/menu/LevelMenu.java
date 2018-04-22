package view.swing.menu;

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
import java.util.Objects;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import controller.io.LevelLoader;
import controller.io.ResourceLoader;
import core.campaign.Editable;
import core.campaign.Level;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.swing.Colors;
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
    private static final int INITIAL_GRID_SIZE = 40;
    private static final int GRID_TO_CELL_RATIO = 5;
    private static final double MEDIUM_FONT = 0.7;
    private static final double MIN_FONT = 0.6;
    private static final String LEVEL_NUMBER = "level.number";
    private static final String BUTTON_NAME = "level.button";
    private static final String VALUE = "XXX";
    private static final int LEVEL_FOR_PAGE = 4;
    private static final int GRID_RAPPORT_WITH_FRAME = 3;
    private final List<JButton> bList = new LinkedList<>();
    private final DesktopGUI mainGUI;
    private final JGridPanel gridPanel;
    private final JTabbedPane cardPanel;
    private final JButton bStart;
    private final JButton bReturn;
    private JButton left;
    private JButton right;
    private int currentLevel;

    /**
     * 
     * @param mainGUI
     *            the main DesktopGUI that calls this LevelMenu
     */
    public LevelMenu(final DesktopGUI mainGUI) {
        this.setOpaque(false);
        this.mainGUI = mainGUI;
        this.setPreferredSize(new Dimension(this.mainGUI.getCurrentWidth(), this.mainGUI.getCurrentHeight()));
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontModifier()));
        this.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;
        c.weighty = 0;

        IntStream.rangeClosed(1, ResourceLoader.loadConstantInt(LEVEL_NUMBER)).forEach(n -> {
            final JButton b = SandboxTools.newJButton(
                    ResourceLoader.loadString(BUTTON_NAME).replaceAll(VALUE, String.valueOf(n)), this.getFont());
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
            this.cardPanel.addTab(ResourceLoader.loadString("level.page").replace(VALUE, String.valueOf(i)),
                    panelLevel(i));
        }

        // GRID PANEL
        this.gridPanel = new JGridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE, INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        this.gridPanel.setPreferredSize(new Dimension(this.mainGUI.getCurrentWidth() / GRID_RAPPORT_WITH_FRAME,
                this.mainGUI.getCurrentHeight() / GRID_RAPPORT_WITH_FRAME));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.5;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(gridPanel, c);
        final JPanel grid = new JPanel();
        grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
        grid.setOpaque(false);
        grid.add(this.cardPanel);
        grid.add(buildRightLeftButtonPanel());
        c.gridx = 1;
        c.gridy = 0;
        this.add(grid, c);
        final JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setOpaque(false);
        bStart = SandboxTools.newJButton(ResourceLoader.loadString("level.button.start"), this.getFont());
        bStart.setFocusable(false);
        exitPanel.add(bStart);

        bReturn = SandboxTools.newJButton(ResourceLoader.loadString("level.button.return"),
                this.getFont());
        bReturn.setFocusable(false);
        exitPanel.add(bReturn);
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        this.add(exitPanel, c);

        bStart.addActionListener(e -> start());
        bReturn.addActionListener(e -> mainGUI.backToMainMenu());
        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
        KeyListenerFactory.addKeyListener(this, "start", KeyEvent.VK_ENTER, () -> bStart.doClick()); 
        this.addAncestorListener(new AncestorListener() {

            @Override
            public void ancestorRemoved(final AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(final AncestorEvent event) {
                LevelMenu.this.validate();
            }

            @Override
            public void ancestorAdded(final AncestorEvent event) {
            }
        });
    }

    private int fontModifier() {
        if (Math.min(this.mainGUI.getCurrentHeight() / this.mainGUI.getScreenHeight(), this.mainGUI.getCurrentWidth() / this.mainGUI.getScreenWidth()) < MIN_FONT) {
            return MenuSettings.getFontSize() * 4 / 3;
        } else if (Math.min(this.mainGUI.getCurrentHeight() / this.mainGUI.getScreenHeight(), this.mainGUI.getCurrentWidth() / this.mainGUI.getScreenWidth()) < MEDIUM_FONT) {
            return MenuSettings.getFontSize() * 3 / 2;
        } else {
            return MenuSettings.getFontSize() * 2;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        final Font oldFont = this.getFont();
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontModifier()));
        this.gridPanel.setPreferredSize(new Dimension(this.mainGUI.getCurrentWidth() / GRID_RAPPORT_WITH_FRAME,
                this.mainGUI.getCurrentHeight() / GRID_RAPPORT_WITH_FRAME));
        if (oldFont.getSize() != this.getFont().getSize()) {
            SwingUtilities.invokeLater(() -> {
                bList.forEach(b -> SandboxTools.resizeButton(b, this.getFont()));
                SandboxTools.resizeButton(this.bStart, this.getFont());
                SandboxTools.resizeButton(this.bReturn, this.getFont());
                if (!Objects.isNull(this.right) && !Objects.isNull(this.left)) {
                    SandboxTools.resizeButton(this.right, this.getFont());
                    SandboxTools.resizeButton(this.left, this.getFont());
                }
            });
        }
        super.doLayout();
    }

    @Override
    public final void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private JPanel buildRightLeftButtonPanel() {
        final JPanel rightLeftButton = new JPanel(new FlowLayout());
        rightLeftButton.setOpaque(false);
        this.right = SandboxTools.newJButton(ResourceLoader.loadString("level.button.right"), this.getFont());
        right.setFocusPainted(false);
        this.left = SandboxTools.newJButton(ResourceLoader.loadString("level.button.left"), this.getFont());
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
        cardPanel.setSelectedIndex(cardPanel.getSelectedIndex() - 1 < 0 ? 0 : cardPanel.getSelectedIndex() - 1);
    }

    private void nextPage() {
        cardPanel.setSelectedIndex(
                cardPanel.getSelectedIndex() + 1 >= cardPanel.getComponentCount() ? cardPanel.getSelectedIndex()
                        : cardPanel.getSelectedIndex() + 1);
    }

    private void start() {
        if (this.currentLevel != 0) {
            this.mainGUI.setView(SandboxBuilder.buildLevelSandbox(this.mainGUI, this.currentLevel));
        }
    }

    private void pressButton(final JButton button) {
        this.bList.stream().filter(b -> !b.isEnabled()).forEach(b -> b.setEnabled(true));
        button.setEnabled(false);
        this.gridPanel.setVisible(false);
        final LevelLoader lLoader = new LevelLoader(this.currentLevel);
        final Level level = lLoader.getLevel();
        this.gridPanel.changeGrid(level.getEnvironmentMatrix().getWidth(), level.getEnvironmentMatrix().getHeight());
        final Matrix<Color> mc = new ListMatrix<>(level.getCellTypeMatrix().getWidth(),
                level.getCellTypeMatrix().getHeight(), () -> null);
        for (int row = 0; row < level.getCellTypeMatrix().getWidth(); row++) {
            for (int col = 0; col < level.getCellTypeMatrix().getWidth(); col++) {
                mc.set(row, col, Colors.cellColor(Editable.EDITABLE, level.getCellTypeMatrix().get(row, col),
                        level.getInitialStateMatrix().get(row, col)));
            }
        }
        SwingUtilities.invokeLater(() -> this.gridPanel.paintGrid(0, 0, mc));
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
        for (int i = LEVEL_FOR_PAGE * pageNumber; i < LEVEL_FOR_PAGE * pageNumber + LEVEL_FOR_PAGE; i++) {
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
