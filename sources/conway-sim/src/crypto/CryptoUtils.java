package crypto;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CryptoUtils {

    private final static String s = "AES/CBC/PKCS5Padding";
    
    public CryptoUtils() {
    }

    public static void main(String[] args) throws Exception {
        
        //creazione ambiente
        File f = new File(".LDLM");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                System.out.println("error in file creation");
                e.printStackTrace();
            }
        } else {
            try {
                f.delete();
                f.createNewFile();
            } catch (Exception e) {
                System.out.println("error in file deletion and creation");
                e.printStackTrace();
            }
        }
        
        //creazione strumenti cifratura
        Cipher cif = Cipher.getInstance(CryptoUtils.s);
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey key = keygen.generateKey();
        cif.init(Cipher.ENCRYPT_MODE, key);
        
        //scrittura
        try (final FileOutputStream fStream = new FileOutputStream(f);
             final CipherOutputStream ciphStream = new CipherOutputStream(fStream, cif);
             final DataOutputStream dStream = new DataOutputStream(ciphStream);
        ){
            dStream.writeUTF("ciao come stai");   // .write("Ciao".getBytes());
            dStream.flush();
            dStream.close();
        } catch (Exception e){
            System.out.println("error in stream write or close");
            e.printStackTrace();
        }
        
        //rifacimento
        try {
            IvParameterSpec ivParaSpec = new IvParameterSpec(key.getEncoded());
            cif.init(Cipher.DECRYPT_MODE, key, ivParaSpec);
        } catch (Exception e) {
            System.out.println("non va");
            e.printStackTrace();
        }
        
        //lettura
        try (final FileInputStream fStream2 = new FileInputStream(f);
             final CipherInputStream ciphStream2 = new CipherInputStream(fStream2, cif);
             final DataInputStream dStream2 = new DataInputStream(ciphStream2);
        ){
            System.out.println(dStream2.readUTF());  //  .readUTF();    //   .readObject();   // .readLine(); // .readByte();
            dStream2.close();
        } catch (Exception e){
            System.out.println("error in stream write or close 2");
            e.printStackTrace();
        }
    }
}
