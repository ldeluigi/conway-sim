package core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

/**
 * 
 */
public class RLEConvert {
    private BufferedReader buffer;

    static final String COORDPATTERN = "x ?= ?([1-9]\\d*)",
                        RULEPATTERN = "rule ?= ?((B[0-8]*/S[0-8]*)|([0-8]*/[0-8]*))",
                        CELLRUNPATTERN = "([1-9]\\d*)?([bo$])";

    //This will contain methods to convert a given matrix into a RLE Format
    //http://www.conwaylife.com/w/index.php?title=Run_Length_Encoded
    //NB: This will be used also as I/O method and SaveToFile
    /**
     * 
     * @param fileName name of the file to be loaded
     * @throws FileNotFoundException when file not found
     */
    public RLEConvert(final File fileName) throws FileNotFoundException {
        this.buffer = new BufferedReader(new FileReader(fileName));
    }
    /**
     * 
     * @param rle string of rle loaded
     */
    public RLEConvert(final String rle) {
        this.buffer = new BufferedReader(new StringReader(rle));
    }
}
