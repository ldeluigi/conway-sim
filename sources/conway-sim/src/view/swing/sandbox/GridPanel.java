package view.swing.sandbox;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * 
 * 
 */
public class GridPanel extends JScrollPane {

    private static final long serialVersionUID = -1;

    private final JPanel grid;
    private final GridBagConstraints c;

    /**
     * 
     */
    public GridPanel() {
        super();
        this.grid = new JPanel(new GridBagLayout());
        this.c = new GridBagConstraints();
        this.c.fill = GridBagConstraints.NONE;
        this.c.weightx = 0.5;
        this.c.weighty = 0.5;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                this.c.gridx = i;
                this.c.gridy = j;
                this.grid.add(new JLabel("X"), c);
            }
        }
        this.setViewportView(this.grid);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

}
