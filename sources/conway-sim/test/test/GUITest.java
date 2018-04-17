package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GUITest {

	JDesktopPane d;
	JFrame f;
	JInternalFrame i;

	JPanel a = new JPanel();

	GUITest() {
		f = new JFrame();
		d = new JDesktopPane();
		f.setContentPane(d);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
		d.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		i = new JInternalFrame("hey");
		a.setBounds(0, 0, 200, 200);
		// a.setVisible(true);
		a.setLayout(new BorderLayout());
		a.add(new JButton("Hello world"));
		f.getContentPane().add(a);
		d.add(i);
		i.setSize(200, 200);
		i.setLocation(300, 300);
		i.setVisible(true);
		f.setVisible(true);
	}

	public static void main(String[] args) {
		GUITest g = new GUITest();
	}

}
