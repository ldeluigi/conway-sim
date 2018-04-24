package controller.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;
import java.util.stream.Collectors;

import controller.book.RecipeBook;
import controller.book.RecipeBookImpl;
import core.campaign.CellType;
import core.campaign.Editable;
import core.campaign.Level;
import core.campaign.LevelImpl;
import core.model.EnvironmentFactory;
import core.model.StandardCellEnvironments;
import core.model.Status;
import core.utils.Matrix;

/**
 * 
 *
 */
public class LevelLoader {
    private static final String LVLPACK = "/levels/lvl";
    private static final String DEFLIST = "default.txt";
    private static final String EDTLIST = "editable.txt";
    private static final String STSLIST = "status.txt";
    private static final String CLTLIST = "celltype.txt";
    private static final String ENVLIST = "environment.txt";
    private static final String DEFBOOK = "/recipebook/";
    private static final String RLE_EXT = ".rle";
    private final String selLvl;
    private final RecipeBook defaultBook;
    private final Level level;

    /**
     * 
     * @param lvl
     *            Number ID of the level to load
     */
    public LevelLoader(final int lvl) {
        this.defaultBook = new RecipeBookImpl();
        this.selLvl = LVLPACK + Integer.toString(lvl) + "/";
        bookLoader(defaultBook);
        this.level = new LevelImpl(editableLoader(), cellTypeLoader(), statusLoader(),
                EnvironmentFactory.from(cellEnvironmentLoader()));
    }

    /**
     * 
     * @param book
     * @param selLvl
     */
    private void bookLoader(final RecipeBook book) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(selLvl + DEFLIST)))) {
            final List<String> pthLst = in.lines().collect(Collectors.toList());
            String testLine = null;
            for (final String name : pthLst) {
                if (name != null && name.contains(RLE_EXT)) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(getClass().getResourceAsStream(DEFBOOK + name), "UTF-8"))) {
                        final List<String> strLst = br.lines().collect(Collectors.toList());
                        final String content = String.join("\n", strLst);
                        testLine = strLst.get(0);
                        if (testLine != null && !testLine.equals("") && testLine.startsWith("#N")) {
                            testLine = testLine.split("#N ")[1];
                        }
                        if (content != null && testLine != null && name != null) {
                            book.addRecipe(content, (!testLine.equals("")) ? testLine : name.replace(RLE_EXT, ""));
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

    private Matrix<CellType> cellTypeLoader() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(selLvl + CLTLIST)))) {
            return RLETranslator.rleStringToMatrix(in.lines().collect(Collectors.joining()), CellType.class);
        } catch (IOException e) {
            Logger.logThrowable(e);
            return null;
        }
    }

    private Matrix<Status> statusLoader() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(selLvl + STSLIST)))) {
            return RLETranslator.rleStringToMatrix(in.lines().collect(Collectors.joining()), Status.class);
        } catch (IOException e) {
            Logger.logThrowable(e);
            return null;
        }
    }

    private Matrix<StandardCellEnvironments> cellEnvironmentLoader() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(selLvl + ENVLIST)))) {
            return RLETranslator.rleStringToMatrix(in.lines().collect(Collectors.joining()),
                    StandardCellEnvironments.class);
        } catch (IOException e) {
            Logger.logThrowable(e);
            return null;
        }
    }

    private Matrix<Editable> editableLoader() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(selLvl + EDTLIST)))) {
            return RLETranslator.rleStringToMatrix(in.lines().collect(Collectors.joining()), Editable.class);
        } catch (IOException e) {
            Logger.logThrowable(e);
            return null;
        }
    }

    /**
     * 
     * @return level
     */
    public final Level getLevel() {
        return this.level;
    }

    /**
     * 
     * @return book
     */
    public final RecipeBook getBook() {
        return this.defaultBook;
    }

}
