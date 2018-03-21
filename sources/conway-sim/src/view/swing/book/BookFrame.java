package view.swing.book;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import controller.book.RecipeImpl;
import controller.editor.PatternEditor;
import controller.io.IOLoader;
import controller.io.RLEConvert;
import controller.io.RecipeLoader;
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



    private static final int FRAME_WIDTH = 500;
    //private static final int HEIGHT = 280;
    private static final int HEIGHTOFCELL = 50;
    private static final int INITIAL_GRID_SIZE = 50;
    private static final int GRID_TO_CELL_RATIO = 8;
    private String selectedItem = null;

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
        // TEST FOR THE JList WITH A TEMP ARRAY
        final List<String> arrList = new ArrayList<String>();

        for (final RecipeImpl recipe : rl.getRecipeBook().getBookList()) {
            arrList.add(recipe.getName());
        }

        final JList<String> list = new JList<String>(arrList.toArray(new String[arrList.size()]));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        list.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                setSelectedItem(list.getSelectedValue());
                System.out.println("DEBUG | Selected Item: " + list.getSelectedValue());
                final Matrix<Status> mat = new RLEConvert(rl.getRecipeBook().getRecipeByName(getSelectedItem()).getContent()).convert();
                final Matrix<Status> newmat = new ListMatrix<Status>(pg.getGridHeight(), pg.getGridWidth(), () -> Status.DEAD);
                Matrices.mergeXY(newmat, 0, 0, mat);
                pg.paintGrid(newmat.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
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
        this.add(list);

        //BUTTON PANEL
        final JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.Y_AXIS));
        this.add(ioPanel);
        final JButton placeBtn = new JButton("Place");

        //ACTION LISTENER TESSSSST
        ActionListener alPlace = e -> {
            System.out.println("DEBUG | PLACE Button pressed, handling the pattern placement.");
            final Matrix<Status> mat = new RLEConvert(rl.getRecipeBook().getRecipeByName(getSelectedItem()).getContent()).convert();
            patternE.addPatternToPlace(mat);
            this.doDefaultCloseAction();
        };
        placeBtn.addActionListener(alPlace);
        ioPanel.add(placeBtn);

        //JFILECHOOSER
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select the file you want to load");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JButton loadBtn = new JButton("Load");

        //ACTION LISTENER TESSSSST
        ActionListener alLoad = e -> {
            final JButton jb = (JButton) e.getSource();
            if (fc.showOpenDialog(jb) == JFileChooser.APPROVE_OPTION) {
                final String filepath;
                filepath = fc.getSelectedFile().getAbsolutePath();
                System.out.println("File selected:" + filepath);
                IOLoader ioLoader = new IOLoader();
                try {
                    ArrayList<String> al = ioLoader.load(filepath);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        };

        loadBtn.addActionListener(alLoad);
        //ADD THE BUTTON
        ioPanel.add(loadBtn);

    }
}

