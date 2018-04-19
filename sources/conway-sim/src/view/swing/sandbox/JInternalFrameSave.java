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
import controller.editor.ExtendedGridEditor;
import controller.io.RLEConvert;

/**
 * 
 */
public class JInternalFrameSave extends JInternalFrame {
    private static final long serialVersionUID = 111988025326750938L;
    private final JButton bSave;
    private final JTextField textName;
    private final JTextField textAuthor;
    private final JTextField textComment;


    /**
     * 
     * @param reset a runnable that it's run at the exit from this JInternaFrame
     * @param gridEditor where is call gridEditor.cut() to take the Status<Matrix>
     *                  {@link GridEditorImpl.isCutReady isCutReady} should return true. //TODO fix
     */
    public JInternalFrameSave(final Runnable reset, final ExtendedGridEditor gridEditor) {
      super("Select Title", false, false, false);
      this.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
      final JPanel general = new JPanel(new BorderLayout());
      final JPanel south = new JPanel(new FlowLayout());
      this.bSave = SandboxTools.newJButton("SAVE", "Save the current pattern");
      this.bSave.setEnabled(false);
      final JButton bExit = SandboxTools.newJButton("EXIT", "Exit, without saving");
      south.add(bSave);
      south.add(bExit);
      general.add(south, BorderLayout.AFTER_LAST_LINE);
      final JPanel gridPanel = new JPanel(new GridLayout(3, 2));
      final JTextField fileName = new JTextField("File name:");
      fileName.setEditable(false);
      this.textName = new JTextField(10);
      final JTextField author = new JTextField("Author:");
      author.setEditable(false);
      this.textAuthor = new JTextField(10);
      final JTextField comment = new JTextField("Comment:");
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
          final String preText = "#N " + textName.getText() + System.lineSeparator()
                                 + "#O " + textAuthor.getText() + System.lineSeparator()
                                 + "#C " + textComment.getText() + System.lineSeparator();
          String stringMatrix = RLEConvert.write(gridEditor.cutMatrix());
          final String stringFile = preText.concat(stringMatrix);
          try (BufferedWriter b = new BufferedWriter(new FileWriter(new File("PatternBook" + "/" + textName.getText() + ".rle")));) {
              b.write(stringFile);
              b.close();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
          reset.run();
          try {
              this.setClosed(true);
          } catch (PropertyVetoException e1) {
              e1.printStackTrace();
          }
      });
      bExit.addActionListener(e -> {
          reset.run();
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
                && this.textAuthor.getText().length() > 0
                && this.textComment.getText().length() > 0) {
            this.bSave.setEnabled(true);
        } else {
            this.bSave.setEnabled(false);
        }
    }
}
