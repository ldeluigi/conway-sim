package view.swing.sandbox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import core.utils.ListMatrix;
import core.utils.Matrix;


/**
 * 
 * 
 */
public class GridPanel extends JScrollPane {

    private static final long serialVersionUID = -1;
    private static final int DIMENSION = 5;

    private final JPanel grid;

    /**
     * 
     */
    public GridPanel() {
      /*final Matrix<JLabel> labels = new ListMatrix<>(100, 100, () -> {
            JLabel l = new JLabel(" L ");
            l.setPreferredSize(new Dimension(GridPanel.DIMENSION, GridPanel.DIMENSION));
            l.setForeground(Color.BLACK);
            return l;
        }); */

        this.grid = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                /*final JLabel lab = new JLabel("A");
                lab.setPreferredSize(new Dimension(GridPanel.DIMENSION, GridPanel.DIMENSION));
                lab.setForeground(new Color(i * 1000 + j));*/
                c.gridx = i;
                c.gridy = j;
                this.grid.add(/*labels.get(i, j)*/ new JLabel(" L") /*lab*/, c);
            }
        }
        this.setViewportView(this.grid);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setWheelScrollingEnabled(true);
        //this.colorGrid();
    }

    private void colorGrid() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                final Component comp = this.grid.getComponentAt(i, j);
                comp.setForeground(new Color((i * 1000) + j));
                comp.setPreferredSize(new Dimension(GridPanel.DIMENSION, GridPanel.DIMENSION));
            }
        }
    }

    /**
     * Anfjbòk rebjq   rpàa  agniertyibtucq.
     * @param paint is the to.
     */
    public void paintMatrix(final Matrix<Color> paint) {
        // cose che non funzionano
    }

}
