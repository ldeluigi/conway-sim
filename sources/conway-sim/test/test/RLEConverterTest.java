package test;

import java.io.IOException;
import java.util.Arrays;

import controller.io.RLEConvert;

class RLEConverterTest {
    private static final String TESTRLE = "#C This is a glider.\n" + "x = 3, y = 3\n" + "bo$2bo$3o!";
    private static RLEConvert rleC = new RLEConvert(TESTRLE);

    RLEConverterTest() {
    }

    public static void main(String[] args) {
        System.out.println(TESTRLE);
        System.out.println(rleC.convert());
    }
}
