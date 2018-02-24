package core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 */
public class RLEConvert {

    private BufferedReader buffer;
    //RULEPATTERNS FOR THE ENCODING
    static final String COORDPATTERN = "x ?= ?([1-9]\\d*)",
                        RULEPATTERN = "rule ?= ?((B[0-8]*/S[0-8]*)|([0-8]*/[0-8]*))",
                        CELLRUNPATTERN = "([1-9]\\d*)?([bo$])";

    //DEFAULT RULE TO BE SETTED. METHOD TO BE IMPLEMENTED
    static final String DEFAULTRULE = "B3/S23";

    //WILDCHAR
    static final char ALT = '!';
    static final String DOLLAR = "$";
    static final String HASH = "#";


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

    /**
     * 
     * @return
     * @throws IOException
     */
    private String readLine() throws IOException {
        return buffer.readLine();
    }
    /**
     * 
     * @throws IOException
     */
    private void close() throws IOException {
        buffer.close();
    }

    /**
     * 
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private String getHeaderLine() throws IOException, IllegalArgumentException {
        String line;
        try {
            while (true) {
                line = readLine();
                if (!line.startsWith(HASH)) {
                    return line;
                }
            }
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("No usable (non-commented) strings found in stream.");
        }
    }

    private String getCellString() throws IOException, IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        String line;
        int i;

        while ((line = readLine()) != null) {
            i = line.indexOf(ALT);
            if (i > -1) {
                sb.append(line.substring(0, i));
                break;
            }
            else {
                sb.append(line);
            }
        }

        if (sb.length() == 0) {
            throw new IllegalArgumentException("No cell data.");
        }
        return sb.toString();
    }

    /**
     * 
     * @return grid
     */
    public boolean[][] load() {
        try {
            String header = getHeaderLine();

            Matcher headerMatcher = Pattern.compile(String.format("^%s, ?%s(, ?%s)?$",
                    COORDPATTERN, COORDPATTERN, RULEPATTERN), Pattern.CASE_INSENSITIVE).matcher(header);
            if (!headerMatcher.matches()) {
                throw new IllegalArgumentException("Invalid header.");
            }

            int x = Integer.parseInt(headerMatcher.group(1)), y = Integer.parseInt(headerMatcher.group(2));
            if (x < 1 || y < 1) {
                throw new IllegalArgumentException("Grid size must be at least 1x1.");
            }

            boolean[][] grid = new boolean[y][x];

            String[] cellStrings = getCellString().split("\\" + DOLLAR);
            Pattern p = Pattern.compile(CELLRUNPATTERN);
            Matcher cellRunMatcher;

            int row = 0;
            for (String cellString : cellStrings) {
                cellRunMatcher = p.matcher(cellString + DOLLAR);
                int col = 0;
                int runLength;

                String tag;
                //RULE WILL BE SOON USED
                @SuppressWarnings("unused")
                String rule;

                if (headerMatcher.group(4) != null) {
                    rule = headerMatcher.group(4);
                }
                else {
                    rule = DEFAULTRULE;
                }
                while (cellRunMatcher.find()) {
                    if (cellRunMatcher.group(1) != null) {
                        runLength = Integer.parseInt(cellRunMatcher.group(1));
                    }
                    else {
                        runLength = 1;
                    }
                    tag = cellRunMatcher.group(2);

                    if (!tag.equals("$")) {
                        try {
                            for (int i = 0; i < runLength; i++) {
                                grid[row][col++] = tag.equals("o");
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            throw new IllegalArgumentException("Too many cells in row.");
                        }
                    }
                    else {
                        row += runLength;
                    }
                }
            }
            return grid;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 
     */
    void saveToFile() {
    }

}
