package crypto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Dumb test for the utility class.
 *
 */
public class TestCrypto {

    /**
     * Main for running the test.
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {

        ArrayList<Integer> aList = new ArrayList<>();
        LinkedList<Integer> lList = new LinkedList<>();

        aList.add(13);
        aList.add(16);
        aList.add(91);
        aList.add(-44);

        lList.add(8);
        lList.add(9);
        lList.add(-129);

        SaveOntoFile.saveProgress(31);

        SaveOntoFile.saveSettings(aList);
        System.out.println(SaveOntoFile.loadProgress());
        System.out.println(SaveOntoFile.loadSettings());

        SaveOntoFile.saveSettings(lList);
        System.out.println(SaveOntoFile.loadProgress());
        System.out.println(SaveOntoFile.loadSettings());
    }

}
