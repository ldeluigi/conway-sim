package test;

import view.swing.book.*;
import view.swing.sandbox.GridPanel;
import view.swing.sandbox.JGridPanel;
import controller.editor.GridEditorImpl;
import controller.io.LevelLoader;
import view.swing.MainGUI;

public class TestGUIBook {

    public static void main(String[] args) {
        MainGUI m = new MainGUI();
        CampaignBookFrame bf = new CampaignBookFrame(new GridEditorImpl(new JGridPanel(100, 100, 100)), new LevelLoader(1));
        m.popUpFrame(bf, true);
    }
}
