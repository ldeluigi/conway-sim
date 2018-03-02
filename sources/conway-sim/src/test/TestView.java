package test;

import javax.swing.JInternalFrame;

import view.swing.MainGUI;
import view.swing.sandbox.GridPanel;
import view.swing.sandbox.Sandbox;

public class TestView {

    public static void main(String[] args) {
        MainGUI m = new MainGUI();
        //Pop up updates
        final JInternalFrame updates = new JInternalFrame("Tutorial", true, true, false, true);
        updates.add(new javax.swing.JLabel("CLICK BUTTONS YAY"));
        updates.setSize(m.getCurrentWidth() / 2, m.getCurrentHeight() / 2);
        m.popUpFrame(updates);
    }

}
