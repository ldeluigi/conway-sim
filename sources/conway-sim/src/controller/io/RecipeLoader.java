package controller.io;

import java.io.File;
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
     */
    public RecipeLoader() {
        File folder = new File("../../test/recipebook/");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> filespaths = new ArrayList<String>();
        this.recipebook = new RecipeBookImpl();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                filespaths.add(file.getAbsolutePath());
                this.recipebook.addRecipe(file.toString(), file.getName());
            }
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
