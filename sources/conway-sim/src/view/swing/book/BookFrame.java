package view.swing.book;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * 
 *
 */
public class BookFrame extends JInternalFrame {
/**
     * 
     */
    private static final long serialVersionUID = -1045414565623185058L;

    /**
     * 
     */
    public BookFrame() {
        super("Book", false, true);
        
        // TEST FOR THE JList WITH A TEMP ARRAY
        String[] elems = new String[10];
        elems[0] = "elem1";
        elems[1] = "elem2";
        JList list = new JList(elems);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        this.add(list);
    }
}

