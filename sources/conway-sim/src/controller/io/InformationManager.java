package controller.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * It Is an utility class for storing and loading informations an a file.
 *
 */
public final class InformationManager {

    private static final String NAME = ".conway";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static File f = new File(InformationManager.NAME);
    private static Cipher cip;
    private static SecretKey key;

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
        try (ObjectOutputStream oStream = new ObjectOutputStream(
                // new CipherOutputStream(
                new FileOutputStream(InformationManager.f)/* , InformationManager.cip) */)) {
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
        try (ObjectOutputStream oStream = new ObjectOutputStream(
                // new CipherOutputStream(
                new FileOutputStream(InformationManager.f)/* , InformationManager.cip) */)) {
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

    private static List<Integer> loadList() {
        if (InformationManager.f.exists()) {
            try (ObjectInputStream oStream2 = new ObjectInputStream(
                    // new CipherInputStream(
                    new FileInputStream(InformationManager.f)/* , InformationManager.cip) */)) {
                List<Integer> list = (LinkedList<Integer>) oStream2.readObject();
                return list != null && !list.isEmpty() ? list : new LinkedList<>(Arrays.asList(0));
            } catch (IOException | ClassNotFoundException e) {
                Logger.logThrowable(e);
            }
        }
        return new LinkedList<>(Arrays.asList(0));
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

    private static void cipInit(final boolean isEncription) {
        try {
            InformationManager.cip = Cipher.getInstance(ALGORITHM);
            final KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            InformationManager.key = keygen.generateKey();
            if (isEncription) {
                InformationManager.cip.init(Cipher.ENCRYPT_MODE, InformationManager.key);
            } else {
                final IvParameterSpec ivParamSpec = new IvParameterSpec(key.getEncoded());
                InformationManager.cip.init(Cipher.DECRYPT_MODE, InformationManager.key, ivParamSpec);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            Logger.logThrowable(e);
        }
    }
}
