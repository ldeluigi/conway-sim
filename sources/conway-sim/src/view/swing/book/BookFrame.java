package view.swing.book;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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
/**
 * 
 *
 */
public class BookFrame extends JInternalFrame {
/**
     * 
     */
    private static final long serialVersionUID = -1045414565623185058L;


    

    private static final int WIDTH = 150;
    //private static final int HEIGHT = 280;
    private static final int HEIGHTOFCELL = 20;
    
    String selectedItem = null;
    /**
     * @param patternE the PatternManager
     * 
     */
    public BookFrame(final PatternEditor patternE) {
        super("Book", false, true);

        final RecipeLoader rl = new RecipeLoader();

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        // SIZE BY DIMENSION
        this.setSize(WIDTH, HEIGHTOFCELL * rl.getRecipeBook().getRecipeBookSize());
        //this.setSize(WIDTH, HEIGHT);

        // TEST FOR THE JList WITH A TEMP ARRAY
        final List<String> arrList = new ArrayList<String>();

        for (RecipeImpl recipe : rl.getRecipeBook().getBookList()) {
            arrList.add(recipe.getName());
        }

        JList<String> list = new JList<String>(arrList.toArray(new String[arrList.size()]));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        list.addMouseListener(new MouseListener() {
            public void mousePressed(final MouseEvent e) {
                selectedItem = list.getSelectedValue();
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

        this.add(list);

        //BUTTON PANEL
        JPanel ioPanel = new JPanel();
        this.add(ioPanel);
        JButton placeBtn = new JButton("Place");

        //ACTION LISTENER TESSSSST
        ActionListener alPlace = e -> {
            //TBI
            patternE.addPatternToPlace(new RLEConvert(rl.getRecipeBook().getRecipeByName(selectedItem).getContent()).convert());
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

