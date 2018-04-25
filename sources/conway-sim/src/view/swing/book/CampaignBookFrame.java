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
import controller.io.LevelLoader;
import controller.io.RLEConvert;
import controller.io.ResourceLoader;
import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrices;
import core.utils.Matrix;
import view.swing.sandbox.JGridPanel;
import view.swing.sandbox.SandboxTools;

//TODO ADD JAVADOC!!!
/**
 * 
 *
 */
public class CampaignBookFrame extends JInternalFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 400;
    private static final int INITIAL_GRID_SIZE = 50;
    private static final int GRID_TO_CELL_RATIO = 8;
    private String selectedItem;

    /**
     * The constructor shows and fills the JList(s) with the patterns of the given
     * LevelLoader.
     * 
     * @param patternE
     *            the {@link PatternEditor}
     * @param ll
     *            the {@link LevelLoader}
     */
    public CampaignBookFrame(final PatternEditor patternE, final LevelLoader ll) {
        super("Campaign Book", true, true, true, true);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        final JGridPanel pg = new JGridPanel(INITIAL_GRID_SIZE, INITIAL_GRID_SIZE,
                INITIAL_GRID_SIZE / GRID_TO_CELL_RATIO);
        final JList<String> jList;
        if (!ll.getBook().getRecipeList().isEmpty()) {
            final List<String> lst = new ArrayList<String>();
            for (final Recipe r : ll.getBook().getRecipeList()) {
                lst.add(r.getName());
            }
            String[] lstArr = new String[lst.size()];
            lstArr = lst.toArray(lstArr);
            jList = new JList<String>(lstArr);
        } else {
            jList = new JList<String>(new String[0]);
        }

        jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setVisibleRowCount(-1);
        final JScrollPane jListPane = new JScrollPane(jList);
        jListPane.setBorder(new TitledBorder(ResourceLoader.loadString("book.defaultbtitle")));

        jList.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                if (jList.getSelectedValue() != null) {
                    setSelectedItem(jList.getSelectedValue());
                    final Matrix<Status> mat = new RLEConvert(
                            ll.getBook().getRecipeByName(getSelectedItem()).getContent()).convert();
                    pg.changeGrid(mat.getWidth(), mat.getHeight());
                    final Matrix<Status> newmat = new ListMatrix<Status>(pg.getGridWidth(), pg.getGridHeight(),
                            () -> Status.DEAD);
                    Matrices.mergeXY(newmat, 0, 0, mat);
                    pg.paintGrid(0, 0, newmat.map(s -> s.equals(Status.ALIVE) ? Color.BLACK : Color.WHITE));
                }

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
        final JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.Y_AXIS));
        final JButton placeBtn = SandboxTools.newJButton(ResourceLoader.loadString("book.place"),
                ResourceLoader.loadString("book.placett"));
        // ACTION LISTENER PLACE BUTTON
        final ActionListener place = e -> {
            final Matrix<Status> mat;

            mat = new RLEConvert(ll.getBook().getRecipeByName(getSelectedItem()).getContent()).convert();

            patternE.addPatternToPlace(mat);
            this.doDefaultCloseAction();
        };

        placeBtn.addActionListener(place);
        ioPanel.add(placeBtn);

        this.add(pg);
        this.add(jListPane);
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

}
