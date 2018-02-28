package test;

import view.swing.book.*; 

import view.swing.MainGUI;

public class TestGUIBook {


        public static void main(String[] args) {
            MainGUI m = new MainGUI();
            BookFrame bf = new BookFrame();
            bf.setSize(300, 100);
            m.popUpFrame(bf);
        }
}
