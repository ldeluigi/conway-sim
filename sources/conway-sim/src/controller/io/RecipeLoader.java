package controller.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import controller.book.RecipeBook;
import controller.book.RecipeBookImpl;

//TODO JAVADOC

/**
 * Loads the aviable {@link Recipe}s from package and filesystem into memory
 * ({@link RecipeBook}.
 *
 */
public class RecipeLoader {
    private final RecipeBook defaultbook;
    private final RecipeBook custombook;
    private static final String FS = File.separator;
    private static final String RLE_EXT = ".rle";
    private static final String CURRENTPATH = new File(".").toString();
    private static final File CUSTOMRECIPEFOLDER = new File(CURRENTPATH + FS + "PatternBook");
    private static final String DEFAULTRECIPEFOLDER = "/recipebook/";

    /**
     * Parses all the files in the preset folder. Than it loads it into the
     * {@link RecipeBook}.
     * 
     */
    public RecipeLoader() {
        this.folderInit(CUSTOMRECIPEFOLDER);
        this.defaultbook = new RecipeBookImpl();
        this.custombook = new RecipeBookImpl();
        this.recipeParser(this.custombook, CUSTOMRECIPEFOLDER);
        this.defRecipeLoader();

    }

    /**
     * Lads and saves the default {@link RecipeBook}.
     */
    private void defRecipeLoader() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(DEFAULTRECIPEFOLDER + "recipebook.txt"), "UTF-8"))) {
            final List<String> pthLst = in.lines().collect(Collectors.toList());
            String testLine = "Name_Placeholder";
            Boolean flagName;
            for (final String line : pthLst) {
                final String name = line;
                if (name != null && name.contains(RLE_EXT)) {
                    flagName = false;
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(
                            getClass().getResourceAsStream(DEFAULTRECIPEFOLDER + name), "UTF-8"))) {
                        final List<String> strLst = br.lines().collect(Collectors.toList());
                        final String content = String.join("\n", strLst);
                        testLine = strLst.get(0);
                        if (testLine != null && !testLine.equals("") && testLine.startsWith("#N")) {
                            flagName = true;
                            testLine = testLine.split("#N ")[1];
                        }
                        if (content != null && testLine != null && name != null) {
                            this.defaultbook.addRecipe(content, flagName ? testLine : name.replace(RLE_EXT, ""));
                        }
                    } catch (IOException e) {
                        Logger.logThrowable(e);
                    }
                }
            }
        } catch (IOException e) {
            Logger.logThrowable(e);
            return;
        }
    }

    /**
     * Initializes the custom {@link RecipeBook} folder.
     * 
     * @param folder
     *            to be initialized.
     */
    private void folderInit(final File folder) {
        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception e) {
                Logger.logThrowable(e);
                return;
            }
        }
    }

    /**
     * Parses the {@link Recipe}s from the given filesystem folder.
     * 
     * @param custombook2
     *            {@link RecipeBook} to be filled.
     * @param folder
     *            Folder to be parsed.
     */
    private void recipeParser(final RecipeBook custombook2, final File folder) {
        final File[] list = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File folder, final String name) {
                return name.toLowerCase(Locale.getDefault()).endsWith(RLE_EXT);
            }
        });
        String testLine = "Name_Placeholder";
        Boolean flagName;
        if (list != null) {
            for (final File file : list) {
                if (file.isFile()) {
                    flagName = false;
                    try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                        testLine = in.readLine();
                        if (testLine != null && !testLine.equals("") && testLine.startsWith("#N")) {
                            flagName = true;
                            testLine = testLine.split("#N ")[1];
                        }
                    } catch (IOException e) {
                        Logger.logThrowable(e);
                    }
                    final Path filepath = Paths.get(file.getPath());
                    try {
                        final String content = String.join("\n",
                                Files.readAllLines(filepath, Charset.forName("UTF-8")));
                        custombook2.addRecipe(content, flagName ? testLine : file.getName().replace(RLE_EXT, ""));
                    } catch (IOException e) {
                        Logger.logThrowable(e);
                    }

                }
            }
        } else {
            return;
        }
    }

    /**
     * Gets the Default {@link RecipeBook}.
     * 
     * @return the Default {@link RecipeBook}
     */
    public RecipeBook getDefaultBook() {
        return this.defaultbook;
    }

    /**
     * Gets the Custom {@link RecipeBook}.
     * 
     * @return The Custom {@link RecipeBook}
     */
    public RecipeBook getCustomBook() {
        return this.custombook;
    }
}
