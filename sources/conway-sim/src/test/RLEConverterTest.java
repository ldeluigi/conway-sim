package test;
import java.io.IOException;
import java.util.Arrays;

import controller.io.RLEConvert;

class RLEConverterTest {
    private static String TESTRLE = "#C This is a glider.\n" 
                                    + "x = 3, y = 3\n"
                                    + "bo$2bo$3o!";
    private static RLEConvert rleC = new RLEConvert(TESTRLE);
    public RLEConverterTest() {
    }
    public static void convert(boolean mat[][]){
        // Loop through all rows
        for (boolean[] row : mat) {
 
            // converting each row as string
            // and then printing in a separate line
            System.out.println(Arrays.toString(row));
        }
    }
    public static void main(String[] args) {
        System.out.println(TESTRLE);
        convert(rleC.convert());
    }
}
