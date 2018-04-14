package controller.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import controller.book.RecipeBookImpl;
/**
 * 
 *
 */
public class RecipeLoader {
    private final RecipeBookImpl defaultbook;
    private final RecipeBookImpl custombook;
    private static final String FS = File.separator;
    private static final File PATH = new File(".");
    private static final String CURRENTPATH = PATH.toString();
    private static final File CUSTOMRECIPEFOLDER = new File(CURRENTPATH + FS + "PatternBook");
    private static final File DEFAULTRECIPEFOLDER = new File(CURRENTPATH + FS + "res" + FS + "recipebook");
/** 
This class parses all the files in the preset folder.
     *  than it loads it in the recipebook.
     * @throws IOException .
     */
    public RecipeLoader() {
        System.out.println("\nCurrent Path: " + CURRENTPATH);
        folderInit(CUSTOMRECIPEFOLDER);
        System.out.println("\nCustom Book Folder: " + CUSTOMRECIPEFOLDER);
        this.defaultbook = new RecipeBookImpl();
        this.custombook = new RecipeBookImpl();
        recipeParser(custombook, CUSTOMRECIPEFOLDER);
        try {
            final URI uri = RecipeLoader.class.getResource("/recipebook").toURI();
            if (uri.getScheme().equals("jar")) {
                jarLoader();
            } else {
                recipeParser(defaultbook, DEFAULTRECIPEFOLDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * This methods returns the recipebook when loaded.
     * @return the recipebook
     */
    public RecipeBookImpl getRecipeBook() {
        return this.defaultbook;
    }
    /**
     * This methods returns the custombook when loaded.
     * @return the custombook
     */
    public RecipeBookImpl getCustomBook() {
        return this.custombook;
    }
    /**
     * This method loads and saves the recipebook from inside the jarfile.
     * @throws IOException 
     */
    private void jarLoader() throws NullPointerException, IOException {
        String testLine = "testLine: NOT_INITIALIZED";
        Boolean flagName;
        final CodeSource src = RecipeLoader.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          final URL jar = src.getLocation();
          ZipInputStream zip = null;
          zip = new ZipInputStream(jar.openStream());
          while (true) {
            final ZipEntry e = zip.getNextEntry();
            if (e == null) {
                break;
            }
            final String name = e.getName();
            if (name.contains(".rle")) {
                //TODO DEBUG
                System.out.println("DEBUG | NAME FOUND: " + name);
                        flagName = false;
                        //TODO DEBUG
                        System.out.println("DEBUG | RLE found in folder: " + name);
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + name), "UTF-8"))) {
                            List<String> strLst = in.lines().collect(Collectors.toList());
                            String content = String.join("\n", strLst);
                            //TODO DEBUG
                            System.out.println("DEBUG | CONTENT: " + content);
                            testLine = strLst.get(0);
                            //TODO DEBUG
                            System.out.println("DEBUG | TestLine: " + testLine);
                            if (testLine != null && !testLine.equals("") && testLine.startsWith("#N")) {
                                flagName = true;
                                testLine = testLine.split("#N ")[1];
                            }
                        //TODO DEBUG
                        System.out.println("DEBUG | Name: " + testLine);
                            defaultbook.addRecipe(content, flagName ? testLine : name.replace(".rle", ""));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

            }
          }
              zip.close();
        } else {
            System.out.println("FAILED");
            //TODO
          /* THIS COULD THROW AN EXCEPTIONs */
        }
    }

    /**
     * 
     * @param folder to be initialized.
     */
    private void folderInit(final File folder) {
        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param book to be filled
     * @param folder to be parsed
     */
    private void recipeParser(final RecipeBookImpl book, final File folder) throws NullPointerException {
        final File[] list = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File folder, final String name) {
                System.out.println("NAME FOUND: " + name);
                return name.toLowerCase(Locale.getDefault()).endsWith(".rle");
            }
        });
        String testLine = "testLine: NOT_INITIALIZED";
        Boolean flagName;
            for (final File file : list) {
                if (file.isFile()) {
                    flagName = false;
                    //TODO DEBUG
                    System.out.println("DEBUG | RLE found in folder: " + file.getPath());
                    try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                        testLine = in.readLine();
                        if (testLine != null && !testLine.equals("") && testLine.startsWith("#N")) {
                            flagName = true;
                            testLine = testLine.split("#N ")[1];
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //TODO DEBUG
                    System.out.println("DEBUG | Name: " + testLine);
                    final Path filepath = Paths.get(file.getPath());
                    try {
                        final String content = java.nio.file.Files.lines(filepath).collect(Collectors.joining("\n"));
                        book.addRecipe(content, flagName ? testLine : file.getName().replace(".rle", ""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
    }
}
