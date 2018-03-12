package controller.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.model.Status;
import core.utils.ListMatrix;
import core.utils.Matrix;

/**
 * 
 * 
 */
public class RLEConvert {
    //I'm adding this suppress warning, in the meanwhile I check what to do with it.
    @SuppressWarnings("PMD.ImmutableField")
    private BufferedReader buffer;
    //RULEPATTERNS FOR THE ENCODING
    static final String XCOORDPATTERN = "x ?= ?([1-9]\\d*)",
                        YCOORDPATTERN = "y ?= ?([1-9]\\d*)",
                        RULEPATTERN = "rule ?= ?((B[0-8]*/S[0-8]*)|([0-8]*/[0-8]*))",
                        CELLRUNPATTERN = "([1-9]\\d*)?([bo$])";

    //DEFAULT RULE TO BE SETTED. METHOD TO BE IMPLEMENTED AS CHALLENGE
    private static final String DEFAULTRULE = "B3/S23";

    //WILDCHAR
    private static final char ALT = '!';
    private static final String DOLLAR = "$";
    private static final String HASH = "#";


    //This will contain methods to convert a given matrix into a RLE Format
    //http://www.conwaylife.com/w/index.php?title=Run_Length_Encoded
    //NB: This will be used also as I/O method and SaveToFile
    /**
     *  This is the builder from file, it takes a fileName of File type
     *  and builds the buffer with the given text found.
     * @param fileName name of the file to be loaded
     * @throws FileNotFoundException when file is null or not found in filesystem
     */
    public RLEConvert(final File fileName) throws FileNotFoundException {
        this.buffer = new BufferedReader(new FileReader(fileName));
    }
    /**
     *  This is the builder from String, it takes a rle of String type
     *  and builds the buffer with the given text found in the String.
     * @param rle String in RLE Format.
     */
    public RLEConvert(final String rle) {
        this.buffer = new BufferedReader(new StringReader(rle));
    }

    /**
     * This is a wrapper of the readLine() method of buffer.
     * @return
     * @throws IOException
     */
    private String readLine() throws IOException {
        return buffer.readLine();
    }
    /**
     * This is a wrapper of the close() method of buffer.
     * @throws IOException
     */
    private void close() throws IOException {
        buffer.close();
    }

    /**
     * This method finds and returns the header line of the RLE.
     * @return the header line in String format
     * @throws IOException
     * @throws IllegalArgumentException if no non-commented strings are found in buffer
     */
    private String getHeaderLine() throws IOException, IllegalArgumentException {
        String line;
        try {
            while (true) {
                line = readLine();
                //DEBUG TBR
                System.out.println("Read line: " + line);
                if (!line.startsWith(HASH)) {

                    return line;
                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("No usable (non-commented) strings found in stream.");
        }

    }
    /**
     * 
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private String getCellString() throws IOException, IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        String line;
        int i;

        while ((line = readLine()) != null) {
            i = line.indexOf(ALT);
            if (i > -1) {
                sb.append(line.substring(0, i));
                break;
            } else {
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
     * @param grid boolean grid
     * @param row size of row
     * @param col size of col
     * @return matrix converted
     */
    public final Matrix<Status> mBoolToStatus(final boolean[][] grid, final int row, final int col) {
        Matrix<Status> matrix = new ListMatrix<>(row, col, () -> Status.DEAD);
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                if (grid[i][k]) {
                    matrix.set(row, col, Status.ALIVE);
                } else {
                    matrix.set(row, col, Status.DEAD);
                }
            }
        }
        return null;
    }
    /**
     * This is the main method, it returns the matrix (grid[][]) converted from the 
     * RLE format.
     * @return grid
     */
    public Matrix<Status> convert() {
        try {
            String header = getHeaderLine();
            Matcher headerMatcher = Pattern.compile(String.format("^%s, ?%s(, ?%s)?$",
                    XCOORDPATTERN, YCOORDPATTERN, RULEPATTERN), Pattern.CASE_INSENSITIVE).matcher(header);
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
                } else {
                    rule = DEFAULTRULE;
                }
                while (cellRunMatcher.find()) {
                    if (cellRunMatcher.group(1) != null) {
                        runLength = Integer.parseInt(cellRunMatcher.group(1));
                    } else {
                        runLength = 1;
                    }
                    tag = cellRunMatcher.group(2);

                    if (!tag.equals("$")) {
                        try {
                            for (int i = 0; i < runLength; i++) {
                                grid[row][col++] = tag.equals("o");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new IllegalArgumentException("Too many cells in row.");
                        }
                    } else {
                        row += runLength;
                    }
                }
            }
            return mBoolToStatus(grid, grid.length, grid[0].length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
