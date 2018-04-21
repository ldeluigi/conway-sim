package view.swing.sandbox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.PropertyVetoException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.io.ResourceLoader;

/**
 * This class implements the save of a {@link String} in a file .rle,
 * with in addition the name, the author and an optional comment.
 */
public class JInternalFrameSave extends JInternalFrame {
    private static final long serialVersionUID = 111988025326750938L;
    private final JButton bSave;
    private final JTextField textName;
    private final JTextField textAuthor;
    private final JTextField textComment;


    /**
     * 
     * @param stringRLEformat is a format RLE string that contains
     *                  the two dimensions, the version and the matrix in standard RLE string
     */
    public JInternalFrameSave(final String stringRLEformat) {
      super(ResourceLoader.loadString("save.title"), true, false, true);
      this.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
      final JPanel general = new JPanel(new BorderLayout());
      final JPanel south = new JPanel(new FlowLayout());
      this.bSave = SandboxTools.newJButton(ResourceLoader.loadString("save.button.save"), ResourceLoader.loadString("save.tooltip.save"));
      this.bSave.setEnabled(false);
      final JButton bExit = SandboxTools.newJButton(ResourceLoader.loadString("save.button.exit"), ResourceLoader.loadString("save.tooltip.exit"));
      south.add(bSave);
      south.add(bExit);
      general.add(south, BorderLayout.AFTER_LAST_LINE);
      final JPanel gridPanel = new JPanel(new GridLayout(3, 2));
      final JTextField fileName = new JTextField(ResourceLoader.loadString("save.name"));
      fileName.setEditable(false);
      this.textName = new JTextField(10);
      final JTextField author = new JTextField(ResourceLoader.loadString("save.author"));
      author.setEditable(false);
      this.textAuthor = new JTextField(10);
      final JTextField comment = new JTextField(ResourceLoader.loadString("save.comment"));
      comment.setEditable(false);
      this.textComment = new JTextField(10);
      gridPanel.add(fileName);
      gridPanel.add(textName);
      gridPanel.add(author);
      gridPanel.add(textAuthor);
      gridPanel.add(comment);
      gridPanel.add(textComment);
      general.add(gridPanel, BorderLayout.CENTER);
      this.add(general);
      this.pack();
      this.setVisible(true);
      this.bSave.addActionListener(e -> {
          final String preText = "#N " + this.textName.getText() + System.lineSeparator()
                               + "#O " + this.textAuthor.getText() + System.lineSeparator()
                               + (this.textComment.getText().length() > 0 ? ("#C " + this.textComment.getText() + System.lineSeparator()) : "");
          final String stringFile = preText.concat(stringRLEformat);
          try (BufferedWriter buffer = new BufferedWriter(new FileWriter(new File("PatternBook" + "/" + this.textName.getText() + ".rle")));) {
              buffer.write(stringFile);
              buffer.close();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
          try {
              this.setClosed(true);
          } catch (PropertyVetoException e1) {
              e1.printStackTrace();
          }
      });
      bExit.addActionListener(e -> {
          try {
              this.setClosed(true);
          } catch (PropertyVetoException e1) {
              e1.printStackTrace();
          }
      });
      this.textName.addCaretListener(l -> enableSave());
      this.textAuthor.addCaretListener(l -> enableSave());
      this.textComment.addCaretListener(l -> enableSave());
    }

    private void enableSave() {
        if (this.textName.getText().length() > 0 
                && this.textAuthor.getText().length() > 0) {
            this.bSave.setEnabled(true);
        } else {
            this.bSave.setEnabled(false);
        }
    }
}
