package controller.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import controller.book.RecipeBookImpl;
/**
 * 
 *
 */
public class RecipeLoader {
    private RecipeBookImpl recipebook;
    /** This class parses all the files in the preset folder.
     *  than it loads it in the recipebook.
     * @throws IOException 
     */
    public RecipeLoader() {
        File folder = new File("./src/test/recipebook");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> filespaths = new ArrayList<String>();
        this.recipebook = new RecipeBookImpl();
        String testLine = "testLine: NOT_INITIALIZED";
        FileReader namereader;
        BufferedReader in;
        Boolean flagName;
        try {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    flagName = false;
                    filespaths.add(file.getAbsolutePath());
                    System.out.println(file.getAbsolutePath());
                    try {
                        namereader = new FileReader(file);
                        in = new BufferedReader(namereader);
                        testLine = in.readLine();
                        if (testLine.startsWith("#N")) {
                            flagName = true;
                            testLine = testLine.split("#N ")[1];
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("testLine: " + testLine);
                    this.recipebook.addRecipe(file.toString(), flagName ? testLine : file.getName());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    /**
     * This methods returns the recipebook when loaded.
     * @return the recipebook
     */
    public RecipeBookImpl getRecipeBook() {
        return this.recipebook;
    }
}
