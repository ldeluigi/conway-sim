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
 * 
 *
 */
public final class SaveOntoFile {

    private static final String NAME = ".conway";
    private final static String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static File f;
    private static List<Integer> settings;
    private static Cipher cip;
    private static SecretKey key;

    /**
     * 
     * @param level
     */
    public static void saveProgress(final int level) {

    }

    /**
     * 
     * @param toSave
     */
    public static void saveSettings(final int[] toSave) {
        //initialization
        SaveOntoFile.createfile();
        SaveOntoFile.cipInit(true);
        try (ObjectOutputStream oStream = new ObjectOutputStream(
                //new CipherOutputStream(
                        new FileOutputStream(SaveOntoFile.f)/* , SaveOntoFile.cip)*/ )
        ) {
            if (toSave != null && toSave.length > 0) {
                SaveOntoFile.settings = new LinkedList<>();
                for (int i = 0; i < toSave.length; i++) {
                    SaveOntoFile.settings.add(toSave[i]);
                }
            }
            oStream.writeObject(SaveOntoFile.settings);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            //TODO mettere roba
            e.printStackTrace();
        }

    }

    /**
     * 
     * @return
     */
    public static int loadProgress() {
        return 0;
    }

    /**
     * 
     */
    public static void loadSettings() {
        if (SaveOntoFile.f.exists()) {
            try (ObjectInputStream oStream2 = new ObjectInputStream(
                    //new CipherInputStream(
                            new FileInputStream(SaveOntoFile.f)/* , SaveOntoFile.cip)*/)
            ) {
                System.out.println(oStream2.readObject());
                oStream2.close();
            } catch (IOException | ClassNotFoundException e) {
                // TODO mettere cose
                e.printStackTrace();
            }
        }
    }

    private static void createfile() {
        SaveOntoFile.f = new File(SaveOntoFile.NAME);
        if (!SaveOntoFile.f.exists()) {
            try {
                SaveOntoFile.f.createNewFile();
            } catch (Exception e) {
                //TODO scrivere cose
                System.out.println("error in file creation");
                e.printStackTrace();
            }
        }
    }
    
    private static void cipInit(final boolean isEncription) {
        try {
            SaveOntoFile.cip = Cipher.getInstance(ALGORITHM);
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            SaveOntoFile.key = keygen.generateKey();
            if (isEncription) {
                SaveOntoFile.cip.init(Cipher.ENCRYPT_MODE, SaveOntoFile.key);
            } else {
                IvParameterSpec ivParamSpec = new IvParameterSpec(key.getEncoded());
                SaveOntoFile.cip.init(Cipher.DECRYPT_MODE, SaveOntoFile.key, ivParamSpec);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            // TODO scrivere robe
            e.printStackTrace();
        }
    }
}
