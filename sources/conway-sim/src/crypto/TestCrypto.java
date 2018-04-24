package crypto;

/**
 * 
 *
 */
public class TestCrypto {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        int[] array = { 1, 2, 5, 18, -23 };
        SaveOntoFile.saveSettings(array);
        SaveOntoFile.loadSettings();
    }

}
