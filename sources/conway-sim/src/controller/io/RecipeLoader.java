package controller.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
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
    private static final File PATH = new File("");
    private static final String CURRENTPATH = PATH.getAbsolutePath();
    private static final File CUSTOMRECIPEFOLDER = new File(CURRENTPATH + FS + "PatternBook");
/** 
This class parses all the files in the preset folder.
     *  than it loads it in the recipebook.
     * @throws IOException .
     */
    public RecipeLoader() {
        System.out.println("\nCurrent Path: " + CURRENTPATH);
        if (!CUSTOMRECIPEFOLDER.exists()) {
            try {
                CUSTOMRECIPEFOLDER.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nCustom Book Folder: " + CUSTOMRECIPEFOLDER);
        this.defaultbook = new RecipeBookImpl();
        this.custombook = new RecipeBookImpl();
        recipeParser(custombook, CUSTOMRECIPEFOLDER);
        System.out.println("USER DIR " + System.getProperty("user.dir"));
        jarLoader();

    }
    /**
     * This methods returns the recipebook when loaded.
     * @return the recipebook
     */
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
     * This method loads recipebook from inside the jarfile.
     */
    private void jarLoader() {
        String testLine = "testLine: NOT_INITIALIZED";
        BufferedReader in;
        Boolean flagName;
        CodeSource src = RecipeLoader.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          URL jar = src.getLocation();
          ZipInputStream zip = null;
        try {
            zip = new ZipInputStream(jar.openStream());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
          while (true) {
            ZipEntry e = null;
            try {
                e = zip.getNextEntry();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (e == null) {
                break;
            }
            String name = e.getName();
            if (name.contains(".rle")) {
                //DEBUG
                System.out.println("DEBUG | NAME FOUND: " + name);
                        flagName = false;
                        //DEBUG
                        System.out.println("DEBUG | RLE found in folder: " + name);
                        try {
                            InputStream is = getClass().getResourceAsStream(FS + name);
                            //DEBUG
                            System.out.println("DEBUG | INPUTSTREAM: " + is);
                            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                            in = new BufferedReader(isr);
                            String content = in.lines().collect(Collectors.joining("\n"));
                            is = new ByteArrayInputStream(content.getBytes());
                            isr = new InputStreamReader(is, "UTF-8");
                            in = new BufferedReader(isr);
                            //DEBUG
                            System.out.println("DEBUG | CONTENT: " + content);
                            testLine = in.readLine();
                            //DEBUG
                            System.out.println("DEBUG | TestLine: " + testLine);
                            try {
                                if (testLine.startsWith("#N")) {
                                    flagName = true;
                                    testLine = testLine.split("#N ")[1];
                                }
                            } catch (NullPointerException ex) {
                                ex.printStackTrace();
                            }
                        //DEBUG
                        System.out.println("DEBUG | Name: " + testLine);
                            defaultbook.addRecipe(content, flagName ? testLine : name);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

              /* Do something with this entry. */
            }
          }
        } else {
            System.out.println("FAILED");
          /* THIS COULD THROW AN EXCEPTIONs */
        }
    }
    /**
     * 
     * @param book to be filled
     * @param folder to be parsed
     */
    private void recipeParser(final RecipeBookImpl book, final File folder) {
        final File[] list = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File folder, final String name) {
                System.out.println("NAMEEEEE: " + name);
                return name.toLowerCase().endsWith(".rle");
            }
        });
        String testLine = "testLine: NOT_INITIALIZED";
        FileReader namereader;
        BufferedReader in;
        Boolean flagName;
        try {
            for (final File file : list) {
                if (file.isFile()) {
                    flagName = false;
                    //DEBUG
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
