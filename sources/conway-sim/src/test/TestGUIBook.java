package test;

import view.swing.book.*;
import controller.editor.GridEditorImpl;
import view.swing.MainGUI;

public class TestGUIBook {


        public static void main(String[] args) {
            MainGUI m = new MainGUI();
            BookFrame bf = new BookFrame(new GridEditorImpl(100, 100));
            m.popUpFrame(bf);
        }
}
