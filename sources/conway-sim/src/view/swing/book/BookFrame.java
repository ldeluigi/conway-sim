package view.swing.book;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import controller.book.Recipe;
import controller.editor.PatternEditor;
import controller.io.RLEConvert;
import controller.io.RecipeLoader;
import controller.io.ResourceLoader;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanelImpl;
import view.swing.sandbox.SandboxTools;

/**
 * This Frame displays the lists of pattern to be placed into the grid.
 *
 */

public class BookFrame extends JInternalFrame {
    private static final long serialVersionUID = -1045414565623185058L;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 400;
    private static final int INITIAL_GRID_SIZE = 50;
    private static final int GRID_TO_CELL_RATIO = 8;
    private static final String DEFAULT = "DEFAULT";
    private static final String CUSTOM = "CUSTOM";
    private String selectedItem;
    private String selectedList = DEFAULT;

    /**
     * The constructor shows and fills the JList(s) with the default patterns and
     * the user added ones.
     * 
     * @param patternE
     *            the {@link PatternEditor}
     * 
     */
    public BookFrame(final PatternEditor patternE) {
        super("Book", true, true, true, true);
        final RecipeLoader rl = new RecipeLoader();
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        final GridPanelImpl pg = new GridPanelImpl(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE,
                INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);

        // Populate the arrList
        final List<String> defRecList = new ArrayList<String>();

        for (final Recipe recipe : rl.getDefaultBook().getRecipeList()) {
            defRecList.add(recipe.getName());
        }
        final List<String> custRecList = new ArrayList<String>();

        for (final Recipe recipe : rl.getCustomBook().getRecipeList()) {
            custRecList.add(recipe.getName());
        }

        final String[] stringDefaultArr = new String[defRecList.size()];
        final JList<String> defaultList = new JList<String>(defRecList.toArray(stringDefaultArr));
        defaultList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        defaultList.setLayoutOrientation(JList.VERTICAL);
        defaultList.setVisibleRowCount(-1);
        final JScrollPane defaultListPane = new JScrollPane(defaultList);
        final TitledBorder defaultBookBord = new TitledBorder(ResourceLoader.loadString("book.defaultbtitle"));
        defaultListPane.setBorder(defaultBookBord);

        defaultList.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                setSelectedItem(defaultList.getSelectedValue());
                setSelectedList(DEFAULT);
                final Matrix<Status> mat = new RLEConvert(
                        rl.getDefaultBook().getRecipeByName(getSelectedItem()).getContent()).convert();
                pg.changeGrid(mat.getWidth(), mat.getHeight());
                final Matrix<Status> newmat = new ListMatrix<Status>(pg.getGridWidth(), pg.getGridHeight(),
                        () -> Status.DEAD);
                Matrices.mergeXY(newmat, 0, 0, mat);
                pg.paintGrid(0, 0, newmat.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
            }

            @Override
            public void mouseClicked(final MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(final MouseEvent arg0) {
            }

            @Override
            public void mouseExited(final MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(final MouseEvent arg0) {
            }
        });

        final String[] stringCustomArr = new String[custRecList.size()];
        final JList<String> customList = new JList<String>(custRecList.toArray(stringCustomArr));
        customList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customList.setLayoutOrientation(JList.VERTICAL);
        customList.setVisibleRowCount(-1);
        final JScrollPane customListPane = new JScrollPane(customList);
        final TitledBorder customBookBord = new TitledBorder(ResourceLoader.loadString("book.custombtitle"));
        customListPane.setBorder(customBookBord);
        customList.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                setSelectedItem(customList.getSelectedValue());
                setSelectedList(CUSTOM);
                final Matrix<Status> mat = new RLEConvert(
                        rl.getCustomBook().getRecipeByName(getSelectedItem()).getContent()).convert();
                pg.changeGrid(mat.getWidth(), mat.getHeight());
                final Matrix<Status> newmat = new ListMatrix<Status>(pg.getGridWidth(), pg.getGridHeight(),
                        () -> Status.DEAD);
                Matrices.mergeXY(newmat, 0, 0, mat);
                pg.paintGrid(0, 0, newmat.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
            }

            @Override
            public void mouseClicked(final MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(final MouseEvent arg0) {
            }

            @Override
            public void mouseExited(final MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(final MouseEvent arg0) {
            }
        });
        // IO BUTTON PANEL
        final JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.Y_AXIS));
        final JButton placeBtn = SandboxTools.newJButton(ResourceLoader.loadString("book.place"),
                ResourceLoader.loadString("book.placett"));
        // ACTION LISTENER PLACE BUTTON
        final ActionListener place = e -> {
            final Matrix<Status> mat;
            if (getSelectedList() == DEFAULT) {
                mat = new RLEConvert(rl.getDefaultBook().getRecipeByName(getSelectedItem()).getContent()).convert();
            } else {
                mat = new RLEConvert(rl.getCustomBook().getRecipeByName(getSelectedItem()).getContent()).convert();
            }
            patternE.addPatternToPlace(mat);
            this.doDefaultCloseAction();
        };

        placeBtn.addActionListener(place);
        ioPanel.add(placeBtn);

        this.add(pg);
        this.add(defaultListPane);
        this.add(customListPane);
        this.add(ioPanel);
    }

    /**
     * 
     * @return selectedItem
     */
    public final String getSelectedItem() {
        return selectedItem;
    }

    /**
     * 
     * @param selectedItem
     *            the item to select
     */
    public void setSelectedItem(final String selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * 
     * @return selectedList
     */
    public String getSelectedList() {
        return selectedList;
    }

    /**
     * 
     * @param selectedList
     *            the list to select
     */
    public void setSelectedList(final String selectedList) {
        this.selectedList = selectedList;
    }
}
