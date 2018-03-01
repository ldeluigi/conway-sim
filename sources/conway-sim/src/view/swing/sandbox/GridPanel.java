package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 *
 */
public class GridPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -6223682631387845522L;
    private final JPanel grid;
    private final GridBagConstraints c = new GridBagConstraints();

    /**
     * 
     */
    public GridPanel() {
        final JScrollPane scroll;
        this.grid = new JPanel(new GridBagLayout());
        scroll = new JScrollPane(this.grid);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroll, BorderLayout.CENTER);
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
