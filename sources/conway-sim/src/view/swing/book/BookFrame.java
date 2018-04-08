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

import controller.book.RecipeImpl;
import controller.editor.PatternEditor;
import controller.io.RLEConvert;
import controller.io.RecipeLoader;
import controller.io.ResourceLoader;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.GridPanel;
/**
 * 
 *
 */

public class BookFrame extends JInternalFrame {
/**
     * 
     */
    private static final long serialVersionUID = -1045414565623185058L;

    private static final int FRAME_WIDTH = 800;
    //private static final int HEIGHT = 280;
    private static final int HEIGHTOFCELL = 50;
    private static final int INITIAL_GRID_SIZE = 50;
    private static final int GRID_TO_CELL_RATIO = 8;
    private static final String DEFAULT = "DEFAULT";
    private static final String CUSTOM = "CUSTOM";
    private String selectedItem = null;
    private String selectedList = DEFAULT;

    /**
     * 
     * @return selectedItem
     */
    public final String getSelectedItem() {
        return selectedItem;
    }
    /**
     * 
     * @param selectedItem the item to select
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
     * @param selectedList the list to select
     */
    public void setSelectedList(final String selectedList) {
        this.selectedList = selectedList;
    }
    /**
     * @param patternE the PatternManager
     * 
     */
    public BookFrame(final PatternEditor patternE) {
        super("Book", false, true);

        final RecipeLoader rl = new RecipeLoader();

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        // SIZE BY DIMENSION
        this.setSize(FRAME_WIDTH, HEIGHTOFCELL * rl.getRecipeBook().getRecipeBookSize());
        //this.setSize(WIDTH, HEIGHT);

        //PATTERN PREVIEW GRID
        //The final GridPanel constructor will have as 3rd argument int SIZE_OF_CELL
        final GridPanel pg = new GridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE, INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        // FILL THE JList WITH A TEMP ARRAY
        final List<String> arrList = new ArrayList<String>();

        for (final RecipeImpl recipe : rl.getRecipeBook().getBookList()) {
            arrList.add(recipe.getName());
        }
        final List<String> custArrList = new ArrayList<String>();

        for (final RecipeImpl recipe : rl.getCustomBook().getBookList()) {
            custArrList.add(recipe.getName());
        }

        final JList<String> defaultList = new JList<String>(arrList.toArray(new String[arrList.size()]));
        defaultList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        defaultList.setLayoutOrientation(JList.VERTICAL);
        defaultList.setVisibleRowCount(-1);
        JScrollPane defaultListPane = new JScrollPane(defaultList);
        TitledBorder defaultBookBord = new TitledBorder(ResourceLoader.loadString("book.defaultbtitle"));
        defaultListPane.setBorder(defaultBookBord);

        defaultList.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                setSelectedItem(defaultList.getSelectedValue());
                setSelectedList(DEFAULT);
                System.out.println("DEBUG | Selected Item: " + defaultList.getSelectedValue());
                final Matrix<Status> mat = new RLEConvert(rl.getRecipeBook().getRecipeByName(getSelectedItem()).getContent()).convert();
                pg.changeGrid(mat.getWidth(), mat.getHeight());
                final Matrix<Status> newmat = new ListMatrix<Status>(pg.getGridWidth(), pg.getGridHeight(), () -> Status.DEAD);
                Matrices.mergeXY(newmat, 0, 0, mat);
                pg.paintGrid(0, 0, newmat.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
            }
            @Override
            public void mouseClicked(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void mouseEntered(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void mouseExited(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void mouseReleased(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

        final JList<String> customList = new JList<String>(custArrList.toArray(new String[custArrList.size()]));
        customList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customList.setLayoutOrientation(JList.VERTICAL);
        customList.setVisibleRowCount(-1);
        JScrollPane customListPane = new JScrollPane(customList);
        TitledBorder customBookBord = new TitledBorder(ResourceLoader.loadString("book.custombtitle"));
        customListPane.setBorder(customBookBord);
        customList.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                setSelectedItem(customList.getSelectedValue());
                setSelectedList(CUSTOM);
                System.out.println("DEBUG | Selected Item: " + customList.getSelectedValue());
                final Matrix<Status> mat = new RLEConvert(rl.getCustomBook().getRecipeByName(getSelectedItem()).getContent()).convert();
                pg.changeGrid(mat.getWidth(), mat.getHeight());
                final Matrix<Status> newmat = new ListMatrix<Status>(pg.getGridWidth(), pg.getGridHeight(), () -> Status.DEAD);
                Matrices.mergeXY(newmat, 0, 0, mat);
                pg.paintGrid(0, 0, newmat.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
            }
            @Override
            public void mouseClicked(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void mouseEntered(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void mouseExited(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void mouseReleased(final MouseEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

        this.add(pg);
        this.add(defaultListPane);
        this.add(customListPane);

        //BUTTON PANEL
        final JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.Y_AXIS));
        this.add(ioPanel);
        final JButton placeBtn = new JButton(ResourceLoader.loadString("book.place"));

        //ACTION LISTENER PLACE BUTTON
        ActionListener place = e -> {
            System.out.println("DEBUG | PLACE Button pressed, handling the pattern placement.");
            final Matrix<Status> mat;
            if (getSelectedList() == DEFAULT) {
                mat = new RLEConvert(rl.getRecipeBook().getRecipeByName(getSelectedItem()).getContent()).convert();
            } else {
                mat = new RLEConvert(rl.getCustomBook().getRecipeByName(getSelectedItem()).getContent()).convert();
            }
            patternE.addPatternToPlace(mat);
            this.doDefaultCloseAction();
        };

        placeBtn.addActionListener(place);
        ioPanel.add(placeBtn);
    }
}

