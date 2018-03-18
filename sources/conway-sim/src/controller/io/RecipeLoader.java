package controller.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import controller.book.RecipeBookImpl;
/**
 * 
 *
 */
public class RecipeLoader {
    private final RecipeBookImpl recipebook;
    private static final String RECIPEFOLDER = "./src/test/recipebook";
    /** This class parses all the files in the preset folder.
     *  than it loads it in the recipebook.
     * @throws IOException .
     */
    public RecipeLoader() {
        final File folder = new File(RECIPEFOLDER);
        final File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File folder, final String name) {
                return name.toLowerCase().endsWith(".rle");
            }
        });
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
                    System.out.println("DEBUG | RLE found in folder: " + file.getPath());
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
                    System.out.println("DEBUG | Name: " + testLine);
                    Path filepath = Paths.get(file.getAbsolutePath());
                    try {
                        String content = java.nio.file.Files.lines(filepath).collect(Collectors.joining("\n"));
                        this.recipebook.addRecipe(content, flagName ? testLine : file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
