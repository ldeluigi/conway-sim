package view.swing.grid;

import java.awt.*;

import javax.swing.*;

import core.utils.Matrix;

import core.model.*;

/**
 * 
 *
 */
public class GridPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -6223682631387845522L;

    private JPanel main = new JPanel(new BorderLayout());
    private JScrollPane scroll;
    private JPanel grid;
    private GridBagConstraints c = new GridBagConstraints();

    /**
     * 
     */
    public GridPanel() {
        super();
        this.grid = new JPanel(new GridBagLayout());
        this.scroll = new JScrollPane(this.grid);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.main.add(scroll, BorderLayout.CENTER);
        this.c.fill =  GridBagConstraints.HORIZONTAL;
        this.addGrid();
    }

    private void addGrid() {
        int j = 0;
        for (int i = 0; i < 100; i++) {
            if ((i % 10) == 0) {
                j++;
            }
            this.c.weightx = 0.5;
            this.c.gridx = (i % 10);
            this.c.gridy = j;
            this.grid.add(new JLabel(Integer.toString(i)), c);
        }
    }
}
