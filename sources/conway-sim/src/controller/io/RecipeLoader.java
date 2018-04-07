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
    private final RecipeBookImpl defaultbook;
    private final RecipeBookImpl custombook;
    //TBI - USE "getResourceAsStream" TO ACCESS FILESYSTEM
    private static final File PATH = new File("");
    private static final String CURRENTPATH = PATH.getAbsolutePath();
    private static final File DEFAULTRECIPEFOLDER = new File(CURRENTPATH + "/res/recipebook");
    private static final File CUSTOMRECIPEFOLDER = new File(CURRENTPATH + "/recipebook");
/** 
This class parses all the files in the preset folder.
     *  than it loads it in the recipebook.
     * @throws IOException .
     */
    public RecipeLoader() {
        System.out.println("\nCurrent Path: " + CURRENTPATH);
        if (!CUSTOMRECIPEFOLDER.exists()) {
            CUSTOMRECIPEFOLDER.mkdir();
        }
        System.out.println("\nDefauld Book Folder: " + DEFAULTRECIPEFOLDER);
        System.out.println("\nCustom Book Folder: " + CUSTOMRECIPEFOLDER);
        this.defaultbook = new RecipeBookImpl();
        this.custombook = new RecipeBookImpl();
        recipeParser(defaultbook, DEFAULTRECIPEFOLDER);
        recipeParser(custombook, CUSTOMRECIPEFOLDER);
    }
    /**
     * This methods returns the recipebook when loaded.
     * @return the recipebook
     */
    @SuppressWarnings("PMD.AvoidCatchingNPE")
    public RecipeBookImpl getRecipeBook() {
        return this.defaultbook;
    }
    /**
     * This methods returns the custmbook when loaded.
     * @return the custombook
     */
    public RecipeBookImpl getCustomBook() {
        return this.custombook;
    }

    /**
     * 
     * @param book to be filled
     * @param folder to be parsed
     */
    private void recipeParser(final RecipeBookImpl book, final File folder) {
        final File[] list = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File folder, final String name) {
                return name.toLowerCase().endsWith(".rle");
            }
        });
        final ArrayList<String> filespaths = new ArrayList<String>();
        String testLine = "testLine: NOT_INITIALIZED";
        FileReader namereader;
        BufferedReader in;
        Boolean flagName;
        try {
            for (final File file : list) {
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
                        book.addRecipe(content, flagName ? testLine : file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
