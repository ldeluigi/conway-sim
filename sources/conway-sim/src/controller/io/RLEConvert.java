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

    private final BufferedReader buffer;

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
    private static final String DCSLASH = "\\";

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
            while (true) {
                line = readLine();
                if (line != null && !line.startsWith(HASH)) {
                    return line;
                } else if (line == null) {
                    throw new IllegalArgumentException("No usable (non-commented) strings found in stream.");
                }
            }

    }
    /**
     * 
     * @return
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private String getCellString() throws IOException, IllegalArgumentException {
        final StringBuilder cellString = new StringBuilder();
        String line;
        //Index
        int i;

        line = readLine();
        while (line != null) {
            //Extracting the end of CellString (ALT = !)
            i = line.indexOf(ALT);
            if (i > -1) {
                cellString.append(line.substring(0, i));
                break;
            } else {
                cellString.append(line);
            }
            line = readLine();
        }

        if (cellString.length() == 0) {
            throw new IllegalArgumentException("No cell info in RLE.");
        }
        return cellString.toString();
    }
    /**
     * This method converts the Matrix from the format Boolean[][] into a Matrix<Status>.
     * @param grid Grid of boolean to be converted.
     * @param row Size of the row of the grid.
     * @param col Size of the column of the grid.
     * @return matrix The matrix converted in Matrix<Status> format.
     */
    public final Matrix<Status> mBoolToStatus(final boolean[][] grid, final int row, final int col) {
        final Matrix<Status> matrix = new ListMatrix<>(row, col, () -> Status.DEAD);
        //TODO DEBUG TBR
        //System.out.println("Converting a matrix of COL (X): " + col + " ROW (Y): " + row);
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                //TODO DEBUG TBR
                //System.out.println("Trying to access: \nCOL: " + k + " ROW: " + i);
                if (grid[i][k]) {
                    matrix.set(k, i, Status.ALIVE);
                    //TODO DEBUG TBR
                    //System.out.println("\nACCESSED AND SETTED\n");
                }
            }
        }
        return matrix;
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
            //Get the pattern dimension by parsing the header and extracting the x and y values
            final int x = Integer.parseInt(headerMatcher.group(1));
            final int y = Integer.parseInt(headerMatcher.group(2));
            //Handle dimension <= 0
            if (x < 1 || y < 1) {
                throw new IllegalArgumentException("Grid size must be at least 1x1.");
            }
            //Create a new boolean matrix
            boolean[][] grid = new boolean[y][x];

            //Split cellString by wildchars
            final String[] cellStrings = getCellString().split(DCSLASH + DOLLAR);
            final Pattern p = Pattern.compile(CELLRUNPATTERN);
            Matcher cellRunMatcher;

            int row = 0;
            for (final String cellString : cellStrings) {
                cellRunMatcher = p.matcher(cellString + DOLLAR);
                int col = 0;
                int runLength;

                String tag;
                //TODO REMOVE: RULE NOT IMPLEMENTED
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

    static public String write(Matrix<Status> tab) {
            String header = "x = " + tab.getHeight() + ", y = " + tab.getWidth() + ", rule = B3/S23";
            header = header.concat(System.lineSeparator());
            int lines = 0;
            for (int i = 0; i < tab.getHeight(); i++) {
                int buffer = 0;
                int last = -1;
                for (int j = 0; j < tab.getWidth(); j++) {
                    if (tab.get(i, j) == Status.ALIVE) {

                        if (lines > 0) {
                            if (lines > 1) {
                                header = header.concat(Integer.toString(lines));
                            }
                            header = header.concat("$");
                            lines = 0;
                        }

                        if (last == 0) {
                            if (buffer > 1) {
                                header = header.concat(Integer.toString(buffer));
                            }
                            header = header.concat("b");
                            buffer = 0;
                        }

                        last = 1;
                        buffer++;
                    } else {
                        if (last == 1) {
                            if (buffer > 1) {
                                header = header.concat(Integer.toString(buffer));
                            }
                            header = header.concat("o");
                            buffer = 0;
                        }
                        last = 0;
                        buffer++;
                    }
                }
                if (last == 1) {
                    if (buffer > 1) {
                        header = header.concat(Integer.toString(buffer));
                    }
                    header = header.concat("o");
                    buffer = 0;
                }
                lines++;
            }

            if (lines > 0) {
                if (lines > 1) {
                    header = header.concat(Integer.toString(lines));
                }
                header = header.concat("$");
                lines = 0;
            }

            header = header.concat("!");
            System.out.println(header);
            return header;
    }

}
