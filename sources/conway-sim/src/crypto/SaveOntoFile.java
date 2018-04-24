package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import controller.io.Logger;

/**
 * It Is an utility class for storing and loading informations an a file.
 *
 */
public final class SaveOntoFile {

    private static final String NAME = ".conway";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static File f = new File(SaveOntoFile.NAME);
    private static List<Integer> settings = new ArrayList<>();
    private static Cipher cip;
    private static SecretKey key;

    private SaveOntoFile() {
    }

    /**
     * Method which stores the progress made, for example the level reached.
     * 
     * @param level
     *            the number representing the level reched
     */
    public static void saveProgress(final int level) {
        SaveOntoFile.createfile();
        SaveOntoFile.cipInit(true);
        try (ObjectOutputStream oStream = new ObjectOutputStream(
                // new CipherOutputStream(
                new FileOutputStream(SaveOntoFile.f)/* , SaveOntoFile.cip) */)) {
            SaveOntoFile.settings = new ArrayList<>();
            SaveOntoFile.settings.add(level);
            oStream.writeObject(SaveOntoFile.settings);
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
    public static Optional<Integer> loadProgress() {
        Optional<List<Integer>> saved = SaveOntoFile.loadList();
        if (saved.isPresent()) {
            if (saved.get().isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(saved.get().get(0));
        }
        return Optional.empty();
    }

    /**
     * Method which stores the setting of the game on a file via ArrayList.
     * 
     * @param toSave
     *            is the List of integers to be stored
     */
    public static void saveSettings(final ArrayList<Integer> toSave) {
        // initialization
        SaveOntoFile.createfile();
        SaveOntoFile.cipInit(true);
        try (ObjectOutputStream oStream = new ObjectOutputStream(
                // new CipherOutputStream(
                new FileOutputStream(SaveOntoFile.f)/* , SaveOntoFile.cip) */)) {
            if (toSave != null && (!toSave.isEmpty())) {
                if (SaveOntoFile.settings.isEmpty()) {
                    SaveOntoFile.settings.add(0);
                }
                SaveOntoFile.settings.addAll(toSave);
                oStream.writeObject(SaveOntoFile.settings);
                oStream.flush();
                SaveOntoFile.settings = new ArrayList<>();
            }
        } catch (IOException e) {
            Logger.logThrowable(e);
        }

    }

    /**
     * Method which stores the setting of the game on a file via LinkedList.
     * 
     * @param toSave
     *            is the list of integers to be stored
     */
    public static void saveSettings(final LinkedList<Integer> toSave) {
        // initialization
        SaveOntoFile.createfile();
        SaveOntoFile.cipInit(true);
        try (ObjectOutputStream oStream = new ObjectOutputStream(
                // new CipherOutputStream(
                new FileOutputStream(SaveOntoFile.f)/* , SaveOntoFile.cip) */)) {
            if (toSave != null && (!toSave.isEmpty())) {
                if (SaveOntoFile.settings.isEmpty()) {
                    SaveOntoFile.settings.add(0);
                }
                SaveOntoFile.settings.addAll(toSave);
                oStream.writeObject(SaveOntoFile.settings);
                oStream.flush();
                SaveOntoFile.settings = new ArrayList<>();
            }
        } catch (IOException e) {
            Logger.logThrowable(e);
        }

    }

    /**
     * Method which loads the settings of the game already stored on the file.
     * 
     * @return the Optional containing the list of integers describing the settings
     */
    public static Optional<List<Integer>> loadSettings() {
        Optional<List<Integer>> saved = SaveOntoFile.loadList();
        if (saved.isPresent()) {
            int length = saved.get().size();
            if (length == 0) {
                return Optional.empty();
            }
            return Optional.of(Collections.unmodifiableList(saved.get().subList(1, length)));
        }
        return Optional.empty();
    }

    private static Optional<List<Integer>> loadList() {
        if (SaveOntoFile.f.exists()) {
            try (ObjectInputStream oStream2 = new ObjectInputStream(
                    // new CipherInputStream(
                    new FileInputStream(SaveOntoFile.f)/* , SaveOntoFile.cip) */)) {
                List<Integer> list = (List<Integer>) oStream2.readObject();
                return Optional.ofNullable(list);
            } catch (IOException | ClassNotFoundException e) {
                Logger.logThrowable(e);
            }
        }
        return Optional.empty();
    }

    private static void createfile() {
        if (!SaveOntoFile.f.exists()) {
            try {
                SaveOntoFile.f.createNewFile();
            } catch (Exception e) {
                Logger.logThrowable(e);
            }
        }
    }

    private static void cipInit(final boolean isEncription) {
        try {
            SaveOntoFile.cip = Cipher.getInstance(ALGORITHM);
            final KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            SaveOntoFile.key = keygen.generateKey();
            if (isEncription) {
                SaveOntoFile.cip.init(Cipher.ENCRYPT_MODE, SaveOntoFile.key);
            } else {
                final IvParameterSpec ivParamSpec = new IvParameterSpec(key.getEncoded());
                SaveOntoFile.cip.init(Cipher.DECRYPT_MODE, SaveOntoFile.key, ivParamSpec);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            Logger.logThrowable(e);
        }
    }
}
