package test;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Menu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.io.ResourceLoader;
import view.swing.MainGUI;
import view.swing.menu.MenuSettings;
import view.swing.sandbox.SandboxImpl;

/**
 * 
 */
public class TestButtonBackground {

    /**
     * 
     */
    public TestButtonBackground() {
        final JFrame frame = new JFrame("test");
        final JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        final JButton button = new MyButton("button", new ImageIcon(ResourceLoader.loadImage("sandbox.button.on")));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setDisabledSelectedIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.pressed")));
        button.setDisabledIcon(new ImageIcon(ResourceLoader.loadImage("sandbox.button.off")));
//        Dimension dim = new Dimension(200, 200);
//        button.setPreferredSize(dim);
        button.setFont(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize()));
        final JButton b2 = new JButton("b2");
        b2.addActionListener(e -> button.setEnabled(true));
        panel.add(b2);
        panel.add(button);
        frame.pack();
        frame.setVisible(true);
        button.addActionListener(e -> {
            button.setEnabled(false);
        });
    }

    public static void main(String... s) {
        new TestButtonBackground();
    }

    /**
     * 
     */
    public class MyButton extends JButton {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * 
         * @param string a
         * @param imageIcon a
         */
        public MyButton(final String string, final ImageIcon imageIcon) {
            super(string, imageIcon);
        }

        @Override
        public void setText(final String arg0) {
            super.setText(arg0);
            final FontMetrics metrics = getFontMetrics(new Font(Font.MONOSPACED, Font.PLAIN, MenuSettings.getFontSize())); 
            final int width = metrics.stringWidth(getText());
            final int height = metrics.getHeight();
            final Dimension newDimension =  new Dimension(width * 2, height * 2);
            setPreferredSize(newDimension);
//            setBounds(new Rectangle(getLocation(), getPreferredSize()));
        }
//
//        public void ResizeToTextButton(String txt){
////            super(txt);
//            addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent arg0) {
//                    setText(JOptionPane.showInputDialog("Text"));
//                }
//            });
//        }
    }
}

