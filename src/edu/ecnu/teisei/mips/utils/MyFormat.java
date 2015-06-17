package edu.ecnu.teisei.mips.utils;

/**
 * Created by Teisei on 2015/4/20.
 */
public class MyFormat {
    public static String split0 = "\t";
    public static String split1 = " ";
    public static String split2 = ", ";
    public static String imeOperand = "#";
    public static String newLine = "\r\n";


    /**
     * immediate data: #40
     * register number: 3
     */
    public static int getLocation(String r) {
        if (r.startsWith("#")) {
            return 0;
        } else {
            return Integer.parseInt(r);
        }
    }
}
