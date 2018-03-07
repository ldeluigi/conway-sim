package controller.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * 
 *
 */
public class IOLoader {
    //private static final String filepath = "/test/file.txt";
    /**
     * 
     */
    public IOLoader() {
    }
    /**
     * 
     * @param filepath > Path of file to be loaded
     * @return list of Arrays
     * @throws FileNotFoundException if file not found
     */
    public final ArrayList<String> load(final String filepath) throws FileNotFoundException {
        Scanner s = new Scanner(new File(filepath));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
        return list;
    }

}
