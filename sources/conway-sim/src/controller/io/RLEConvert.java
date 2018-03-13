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

    private BufferedReader buffer;

    //CHARSET PATTERNS FOR THE RLE READING IN THE BUFFER
    static final String XCOORDPATTERN = "x ?= ?([1-9]\\d*)",
                        YCOORDPATTERN = "y ?= ?([1-9]\\d*)",
                        RULEPATTERN = "rule ?= ?((B[0-8]*/S[0-8]*)|([0-8]*/[0-8]*))",
                        CELLRUNPATTERN = "([1-9]\\d*)?([bo$])";

    //DEFAULT RULE TO BE SETTED. METHOD TO BE IMPLEMENTED AS CHALLENGE
    private static final String DEFAULTRULE = "B3/S23";

    //WILDCHARS
    private static final char ALT = '!';
    private static final String DOLLAR = "$";
    private static final String HASH = "#";

    //ALIVE CELL
    private static final String AC = "o";


    //This will contain methods to convert a given matrix into a RLE Format
    //http://www.conwaylife.com/w/index.php?title=Run_Length_Encoded
    //NB: This will be used also as I/O method and SaveToFile

    //UNUSED FOR NOW
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
            e.printStackTrace();
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
        final StringBuilder sb = new StringBuilder();
        String line;
        int i;

        line = readLine();
        while (line != null) {
            i = line.indexOf(ALT);
            if (i > -1) {
                sb.append(line.substring(0, i));
                break;
            } else {
                sb.append(line);
            }
            line = readLine();
        }

        if (sb.length() == 0) {
            throw new IllegalArgumentException("No cell data.");
        }
        return sb.toString();
    }
    /**
     * This method converts the Matrix from the format Boolean[][] into a Matrix<Status>.
     * @param grid Grid of boolean to be converted.
     * @param row Size of the row of the grid.
     * @param col Size of the column of the grid.
     * @return matrix The matrix converted in Matrix<Status> format.
     */
    public final Matrix<Status> mBoolToStatus(final boolean[][] grid, final int row, final int col) {
        Matrix<Status> matrix = new ListMatrix<>(row, col, () -> Status.DEAD);
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                if (grid[i][k]) {
                    matrix.set(i, k, Status.ALIVE);
                }
            }
        }
        return null;
    }
    /**
     * This is the main method, it returns the matrix (Matrix<Status>) converted from the 
     * RLE format.
     * @return The converted pattern in Matrix<Status> format.
     */
    public Matrix<Status> convert() {
        try {

            final String header = getHeaderLine();
            final Matcher headerMatcher = Pattern.compile(String.format("^%s, ?%s(, ?%s)?$",
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
            for (final String cellString : cellStrings) {
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

                    if (!tag.equals(DOLLAR)) {
                        try {
                            for (int i = 0; i < runLength; i++) {
                                grid[row][col++] = tag.equals(AC);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new IllegalArgumentException("The number of cells in this row is higher than the row size.");
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
