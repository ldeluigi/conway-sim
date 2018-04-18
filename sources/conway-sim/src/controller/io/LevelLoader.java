package controller.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controller.book.RecipeBook;
import controller.book.RecipeBookImpl;
import core.model.Status;
import core.utils.Matrix;
/**
 * 
 *
 */
public class LevelLoader {
    private static final String LVLPACK = "levels/lvl";
    private static final String DEFLIST = "default.txt";
    private static final String DEFBOOK = "/recipebook/";
    private static final String RLE_EXT = ".rle";
    private static final String CNW_EXT = ".conway";
    private static final int N_STAGES = 5;
    private final RecipeBook defaultBook;
    /**
     * 
     * @param lvl sda
     */
    public LevelLoader(final int lvl) {
        this.defaultBook = new RecipeBookImpl();
        final String selLvl = LVLPACK + Integer.toString(lvl) + "/";
        bookLoader(this.defaultBook, selLvl);
        List<Matrix<? extends Enum<?>>> stagesLst = stageLoader(selLvl, N_STAGES);
    }

    private List<Matrix<? extends Enum<?>>> stageLoader(final String selLvl, final int stages) {
        List<Matrix<? extends Enum<?>>> stagesLst = new ArrayList<Matrix<? extends Enum<?>>>();
        for (int i = 0; i < stages; i++) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(selLvl + "stage" + Integer.toString(stages + 1) + CNW_EXT)))) {
                stagesLst.add(RLETranslator.rleStringToMatrix(in.readLine(), null));
             } catch (IOException e) {
                 e.printStackTrace();
             }
        }
        return stagesLst;
    }
    /**
     * 
     * @param book
     * @param selLvl
     */
    private void bookLoader(final RecipeBook book, final String selLvl) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(selLvl + DEFLIST)))) {
            final List<String> pthLst = in.lines().collect(Collectors.toList());
            String testLine = null;
            for (final String name : pthLst) {
                if (name != null && name.contains(RLE_EXT)) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(
                            getClass().getResourceAsStream(DEFBOOK + name), "UTF-8"))) {
                        final List<String> strLst = br.lines().collect(Collectors.toList());
                        final String content = String.join("\n", strLst);
                        testLine = strLst.get(0);
                        if (testLine != null && !testLine.equals("") && testLine.startsWith("#N")) {
                            testLine = testLine.split("#N ")[1];
                        }
                        if (content != null && testLine != null && name != null) {
                            book.addRecipe(content,
                                    (!testLine.equals("")) ? testLine : name.replace(RLE_EXT, ""));
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            //TODO DEBUG
            System.out.println("Couldn't load RLE List .txt from res.");
            e.printStackTrace();
            return;
        }

    }

}
