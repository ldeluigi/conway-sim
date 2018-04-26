package view.swing.level;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import controller.io.InformationManager;
import controller.io.LevelLoader;
import controller.io.ResourceLoader;
import core.campaign.Editable;
import core.campaign.Level;
import core.model.StandardCellEnvironments;
import core.utils.ListMatrix;
import core.utils.Matrix;
import view.Colors;
import view.swing.DesktopGUI;
import view.swing.menu.LoadingScreen;
import view.swing.menu.MenuSettings;
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
    private static final int GRID_TO_CELL_RATIO = 4;
    private static final String LEVEL_NUMBER = "level.number";
    private static final String VALUE = "XXX";
    private static final int LEVEL_FOR_PAGE = 4;
    private final List<JButton> bList = new LinkedList<>();
    private final List<JButton> bListUnReacedLevel = new LinkedList<>();
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
        final int currentProgress = InformationManager.loadProgress();
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));

        IntStream.rangeClosed(1, ResourceLoader.loadConstantInt(LEVEL_NUMBER)).forEach(n -> {
            final JButton b = SandboxTools.newJButton(
                    ResourceLoader.loadString("level.button").replaceAll(VALUE, String.valueOf(n)), this.getFont());
            final Dimension newDim = b.getPreferredSize();
            newDim.setSize(newDim.width, newDim.height * 2);
            b.setPreferredSize(newDim);
            SandboxTools.setIcon(b, newDim);
            if (currentProgress == 0 && n == 1) {
                b.setEnabled(true);
                this.bList.add(b);
            } else if (currentProgress < n) {
                b.setEnabled(false);
                this.bListUnReacedLevel.add(b);
            } else {
                b.setEnabled(true);
                this.bList.add(b);
            }
            b.setFont(this.getFont());
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
        this.gridPanel = new JGridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE, INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        this.setLayout(new BorderLayout());
        this.add(this.gridPanel, BorderLayout.CENTER);
        final JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel cpWrapper = new JPanel(new GridBagLayout());
        cpWrapper.setOpaque(false);
        cpWrapper.add(this.cardPanel);
        c.ipady = this.mainGUI.getCurrentHeight() / 3;
        c.gridx = 0;
        c.gridy = 0;
        right.add(cpWrapper, c);
        c.ipady = 0;
        c.gridy = 1;
        right.add(buildRightLeftButtonPanel(), c);

        final JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setOpaque(false);
        final JButton bStart = SandboxTools.newJButton(ResourceLoader.loadString("level.button.start"), this.getFont());
        bStart.setFocusable(false);
        exitPanel.add(bStart);

        final JButton bReturn = SandboxTools.newJButton(ResourceLoader.loadString("level.button.return"),
                this.getFont());
        bReturn.setFocusable(false);
        exitPanel.add(bReturn);
        c.gridy = 2;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        right.add(exitPanel, c);
        this.add(right, BorderLayout.EAST);

        bStart.addActionListener(e -> start());
        bReturn.addActionListener(e -> mainGUI.backToMainMenu());
        KeyListenerFactory.addKeyListener(this, "start", KeyEvent.VK_ENTER, () -> bStart.doClick());
        KeyListenerFactory.addKeyListener(this, "return", KeyEvent.VK_ESCAPE, () -> bReturn.doClick());
        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        g.drawImage(ResourceLoader.loadImage("sandbox.background1"), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private JPanel buildRightLeftButtonPanel() {
        final JPanel rightLeftButton = new JPanel(new FlowLayout());
        rightLeftButton.setOpaque(false);
        final JButton right = SandboxTools.newJButton(ResourceLoader.loadString("level.button.right"), this.getFont());
        final Dimension newDimR = right.getPreferredSize();
        newDimR.setSize(right.getPreferredSize().width / 2, right.getPreferredSize().height);
        right.setPreferredSize(newDimR);
        right.setIcon(new ImageIcon(ResourceLoader.loadImage("menu.arrow.right.on").getScaledInstance(
                (int) right.getPreferredSize().getWidth(), (int) right.getPreferredSize().getHeight(),
                Image.SCALE_SMOOTH)));
        right.setDisabledIcon(new ImageIcon(ResourceLoader.loadImage("menu.arrow.right.off").getScaledInstance(
                (int) right.getPreferredSize().getWidth(), (int) right.getPreferredSize().getHeight(),
                Image.SCALE_SMOOTH)));
        right.setPressedIcon(new ImageIcon(ResourceLoader.loadImage("menu.arrow.right.pressed").getScaledInstance(
                (int) right.getPreferredSize().getWidth(), (int) right.getPreferredSize().getHeight(),
                Image.SCALE_SMOOTH)));
        right.setText("");
        final JButton left = SandboxTools.newJButton(ResourceLoader.loadString("level.button.left"), this.getFont());
        left.setFocusable(false);
        final Dimension newDimL = left.getPreferredSize();
        newDimL.setSize(left.getPreferredSize().width / 2, left.getPreferredSize().height);
        left.setPreferredSize(newDimL);
        left.setIcon(new ImageIcon(ResourceLoader.loadImage("menu.arrow.left.on").getScaledInstance(
                (int) left.getPreferredSize().getWidth(), (int) left.getPreferredSize().getHeight(),
                Image.SCALE_SMOOTH)));
        left.setDisabledIcon(new ImageIcon(ResourceLoader.loadImage("menu.arrow.left.off").getScaledInstance(
                (int) left.getPreferredSize().getWidth(), (int) left.getPreferredSize().getHeight(),
                Image.SCALE_SMOOTH)));
        left.setPressedIcon(new ImageIcon(ResourceLoader.loadImage("menu.arrow.left.pressed").getScaledInstance(
                (int) left.getPreferredSize().getWidth(), (int) left.getPreferredSize().getHeight(),
                Image.SCALE_SMOOTH)));
        left.setText("");
        rightLeftButton.add(left);
        rightLeftButton.add(right);

        KeyListenerFactory.addKeyListener(this, "right", KeyEvent.VK_RIGHT, () -> nextPage());
        right.addActionListener(e -> this.nextPage());
        KeyListenerFactory.addKeyListener(this, "left", KeyEvent.VK_LEFT, () -> previousPage());
        left.addActionListener(e -> this.previousPage());
        return rightLeftButton;
    }

    private void previousPage() {
        this.cardPanel
                .setSelectedIndex(cardPanel.getSelectedIndex() - 1 < 0 ? 0 : this.cardPanel.getSelectedIndex() - 1);
    }

    private void nextPage() {
        this.cardPanel.setSelectedIndex(this.cardPanel.getSelectedIndex() + 1 >= this.cardPanel.getComponentCount()
                ? this.cardPanel.getSelectedIndex()
                : this.cardPanel.getSelectedIndex() + 1);
    }

    private void start() {
        if (this.currentLevel != 0) {
            this.mainGUI.setView(new LoadingScreen());
            SwingUtilities
                    .invokeLater(() -> this.mainGUI.setView(SandboxBuilder.buildLevelSandbox(mainGUI, currentLevel)));
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
                colorMatrix.set(row, col,
                        Colors.cellColor(Editable.EDITABLE, level.getCellTypeMatrix().get(row, col),
                                level.getInitialStateMatrix().get(row, col),
                                (StandardCellEnvironments) level.getEnvironmentMatrix().getCellEnvironment(row, col)));
            }
        }
        SwingUtilities.invokeLater(() -> this.gridPanel.paintGrid(0, 0, Colors.colorMatrix(level)));
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
        for (int i = LEVEL_FOR_PAGE * pageNumber; i < LEVEL_FOR_PAGE * pageNumber + LEVEL_FOR_PAGE; i++) {
            if (i > ResourceLoader.loadConstantInt(LEVEL_NUMBER) - 1) {
                return gridLevel;
            } else {
                if (bList.size() + bListUnReacedLevel.size() > i) {
                    c.gridx = i % LEVEL_FOR_PAGE % 2 == 0 ? 0 : 1;
                    c.gridy = i % LEVEL_FOR_PAGE / 2;
                    gridLevel.add(Stream.concat(bList.stream(), bListUnReacedLevel.stream())
                            .collect(Collectors.toList()).get(i), c);
                }
            }
        }
        return gridLevel;
    }
}
