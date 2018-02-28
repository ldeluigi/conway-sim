package test;

import javax.swing.JInternalFrame;

import view.swing.MainGUI;
import view.swing.sandbox.Sandbox;

public class TestView {

    public static void main(String[] args) {
        MainGUI m = new MainGUI();
        JInternalFrame i = new JInternalFrame("test", true, true, true, true);
        i.setSize(300, 100);
        m.popUpFrame(i);
    }

}
