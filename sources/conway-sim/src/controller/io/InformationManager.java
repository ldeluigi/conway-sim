package controller.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * It Is an utility class for storing and loading informations an a file.
 *
 */
public final class InformationManager {

    private static final String NAME = ".conway";
    private static File f = new File(InformationManager.NAME);

    private InformationManager() {
    }

    /**
     * Method which stores the progress made, for example the level reached.
     *
     * @param level
     *            the number representing the level reached
     */
    public static void saveProgress(final int level) {
        InformationManager.createfile();
        final List<Integer> completeList = InformationManager.loadList();
        try (ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(InformationManager.f))) {
            oStream.flush();
            completeList.set(0, level);
            oStream.writeObject(completeList);
            oStream.flush();
        } catch (IOException e) {
            Logger.logThrowable(e);
        }
    }

    /**
     * Method which gives reads from file the number representing the level reached.
     *
     * @return an Optional containing the level reached so far.
     */
    public static int loadProgress() {
        final List<Integer> saved = InformationManager.loadList();
        return saved.get(0);
    }

    /**
     * Method which stores the setting of the game on a file via ArrayList.
     *
     * @param toSave
     *            is the List of integers to be stored
     */
    public static void saveSettings(final List<Integer> toSave) {
        InformationManager.createfile();
        final List<Integer> completeList = InformationManager.loadList();
        try (ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(InformationManager.f))) {
            oStream.flush();
            final List<Integer> list = new LinkedList<>(completeList.subList(0, 1));
            list.addAll(toSave);
            oStream.writeObject(list);
            oStream.flush();
        } catch (IOException e) {
            Logger.logThrowable(e);
        }
    }

    /**
     * Method which loads the settings of the game already stored on the file.
     *
     * @return the Optional containing the list of integers describing the settings
     */
    public static List<Integer> loadSettings() {
        final List<Integer> list = InformationManager.loadList();
        list.remove(0);
        return list;
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> loadList() {
        List<Integer> list = null;
        if (InformationManager.f.exists()) {
            try (ObjectInputStream oStream2 = new ObjectInputStream(new FileInputStream(InformationManager.f))) {
                final Object read = oStream2.readObject();
                if (read instanceof LinkedList<?> && !((LinkedList<?>) read).isEmpty()
                        && ((LinkedList<?>) read).stream().allMatch(o -> o instanceof Integer)) {
                    list = (LinkedList<Integer>) read;
                }
            } catch (EOFException e1) {
                list = null;
            } catch (Exception e2) {
                Logger.logTime("Couldn't read from the specified file");
                Logger.logThrowable(e2);
            }
        }
        return list != null && !list.isEmpty() ? list : new LinkedList<>(Arrays.asList(0));
    }

    private static void createfile() {
        if (!InformationManager.f.exists()) {
            try {
                InformationManager.f.createNewFile();
            } catch (Exception e) {
                Logger.logThrowable(e);
            }
        }
    }
}
