package view.swing.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import core.utils.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 
 * 
 */
public class GridPanel extends JScrollPane {

    private static final long serialVersionUID = -1;
    private static final int DIM = 7;

    private final JPanel grid;
    private final List<List<JLabel>> labels = new ArrayList<>();
    final GridBagConstraints c;

    /**
     * 
     */
    public GridPanel() {
        super();
         this.c = new GridBagConstraints();
        this.grid = new JPanel(new GridBagLayout());
        this.c.fill = GridBagConstraints.NONE;
        this.c.weightx = 0.5;
        this.c.weighty = 0.5;
        final List<JLabel> temp = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
               temp.add(new JLabel(" "));
            }
            this.labels.add(temp);
        }

        IntStream.range(0, 100).forEach(i -> {
            IntStream.range(0, 100).forEach(j -> {
                this.labels.get(i).get(j).setForeground(new Color(i));
                this.labels.get(i).get(j).setPreferredSize(new Dimension(DIM, DIM));
                this.c.gridx = i;
                this.c.gridy = j;
                this.grid.add(this.labels.get(i).get(j), c);
            });
        });
        this.setViewportView(this.grid);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Abcde fghi jklmnopqrst uvwxyz.
     * @param painting is the to.
     */
    public void drawMatrix(final Matrix<Color> painting) {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
               this.labels.get(i).get(j).setForeground(painting.get(i, j));
               this.labels.get(i).get(j).setPreferredSize(new Dimension(DIM, DIM));
               this.c.gridx = i;
               this.c.gridy = j;
               this.grid.add(this.labels.get(i).get(j), c);
            } 
        }
    }

}
