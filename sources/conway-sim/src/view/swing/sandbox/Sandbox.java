package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controller.generation.GenerationController;
import controller.generation.GenerationControllerImpl;
import core.model.Status;
import view.swing.DesktopGUI;
import view.swing.book.BookFrame;
import view.swing.menu.MenuSettings;

/**
 * 
 */
public class Sandbox extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -9015811419136279771L;

    private static final String BOOK_NAME = "BOOK";
    private static final int DEFAULT_SIZE = 100;

    private final GenerationPanel generationPanel;
    private final JButton bBook = new JButton(BOOK_NAME);
    private final DesktopGUI mainGUI;
    private final GridPanel grid;
    private final GenerationController genController;
    private BookFrame book;
    private final int fontSize = MenuSettings.getFontSize();

    private JInternalFrame colorFrame;
    private Color alive = Color.BLACK;
    private Color dead = Color.WHITE;
    /**
     * 
     * @param mainGUI the mainGui that call this SandBox
     */
    public Sandbox(final DesktopGUI mainGUI) {
        Objects.requireNonNull(mainGUI);
        this.genController = new GenerationControllerImpl();
        this.generationPanel = new GenerationPanel(genController);
        this.genController.setView(this);
        this.mainGUI = mainGUI;
        this.grid = new GridPanel(Sandbox.DEFAULT_SIZE, Sandbox.DEFAULT_SIZE, mainGUI);
        this.setLayout(new BorderLayout());
        this.add(grid, BorderLayout.CENTER);

        final JPanel north = new JPanel(new BorderLayout());
        north.add(generationPanel, BorderLayout.AFTER_LINE_ENDS);
        north.add(new JLabel("SANDBOX MODE"), BorderLayout.BEFORE_FIRST_LINE);
        this.add(north, BorderLayout.NORTH);

        final JButton bExit = new JButton("EXIT");

        final JPanel south = new JPanel(new BorderLayout());
        this.bBook.setFont(new Font(bBook.getFont().getFontName(), bBook.getFont().getStyle(), this.fontSize));
        bExit.setFont(new Font(bExit.getFont().getFontName(), bExit.getFont().getStyle(), this.fontSize));
        final JPanel southLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel southRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        final JButton bColor = new JButton("Cell color");
        bColor.setFont(new Font(bColor.getFont().getFontName(), bColor.getFont().getStyle(), this.fontSize));
        southRight.add(bColor);
        bColor.addActionListener(e -> selectColorFromChooser());

        southLeft.add(new JLabel("Current generation number : "));
        southLeft.add(generationPanel.getNumGeneration());
        southRight.add(bBook);
        southRight.add(bExit);
        south.add(southLeft, BorderLayout.WEST);
        south.add(southRight, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        bBook.addActionListener(e -> callBook());
        bExit.addActionListener(e -> exit());
        this.generationPanel.refreshView();

    }

    private void selectColorFromChooser() {
        if (Objects.isNull(colorFrame)) {
            colorFrame = new JInternalFrame("Color selector", false, true);
            this.mainGUI.popUpFrame(colorFrame);
            final JPanel p = new JPanel(new BorderLayout());
            final JPanel buttonPanel = new JPanel(new FlowLayout());
            final JColorChooser colorChooser = new JColorChooser();
            p.add(colorChooser, BorderLayout.CENTER);
            colorFrame.setVisible(true);
            colorChooser.setVisible(true);
            final JButton bAlive = new JButton("Select alive cell");
            final JButton bDead = new JButton("Select dead cell");
            buttonPanel.add(bAlive);
            buttonPanel.add(bDead);
            p.add(buttonPanel, BorderLayout.SOUTH);
            colorFrame.add(p, BorderLayout.CENTER);
            bAlive.addActionListener(a -> {
                this.setColorAlive((Color) colorChooser.getColor());
                this.refreshView();
            });
            bDead.addActionListener(d -> {
                this.setColorDead((Color) colorChooser.getColor());
                this.refreshView();
            });
        } else {
            this.mainGUI.popUpFrame(colorFrame);
            colorFrame.show();
            colorFrame.setVisible(true);
        }
        colorFrame.pack();
    }

    private void setColorDead(final Color color) {
        this.dead = color;
    }

    private void setColorAlive(final Color color) {
        this.alive = color;
    }

    /**
     * refresh all the view.
     */
    public void refreshView() {
        this.generationPanel.refreshView();
        this.grid.paintGrid(this.genController.getCurrentGeneration().getCellMatrix().map(e -> e.getStatus() == Status.ALIVE ? alive : dead));
    }

    private void callBook() {
        if (Objects.isNull(book)) {
            book = new BookFrame();
            this.mainGUI.popUpFrame(book);
        } else if (!book.isShowing()) {
            this.book = new BookFrame();
            this.mainGUI.popUpFrame(book);
        }
    }

    private void exit() {
        //TODO save option name
        final int result = JOptionPane.showOptionDialog(this, "Save before going back to menu?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        if (result == JOptionPane.YES_OPTION) {
            this.mainGUI.backToMainMenu();
        } else if (result == JOptionPane.NO_OPTION) {
            this.mainGUI.backToMainMenu();
        }
    }

}
