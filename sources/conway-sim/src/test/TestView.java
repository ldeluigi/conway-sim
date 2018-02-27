package test;

import javax.swing.JInternalFrame;

import view.swing.MainGUI;

public class TestView {

    public static void main(String[] args) {
        MainGUI m = new MainGUI();
        JInternalFrame i = new JInternalFrame("test");
        i.setSize(100, 100);
        m.popUpFrame(i);
    }

}
