package edu.ecnu.teisei.mips.utils;

/**
 * Math tool.
 * Created by Teisei on 2015/4/20.
 */
public class MyMath {
    /**
     * convert a binary str into a decimal Integer.
     * @param binary_number_str binary str
     * @param isSigned whether it is a signed integer.
     * @return
     */
    public static long convert2Integer(String binary_number_str, boolean isSigned) {
        char ch[] = binary_number_str.toCharArray();
        long res = 0;
        int max_bit = ch.length - 1;
        if (isSigned) {
            if (ch[0] == '1') {
                res = -1;
                for (int i = 1; i <= max_bit; i++) {
                    if (ch[i] == '0') {
                        res -= (long) Math.pow(2, max_bit - i);
                    }
                }
            } else {
                res = 0;
                for (int i = 1; i <= max_bit; i++) {
                    if (ch[i] == '1') {
                        res += (long) Math.pow(2, max_bit - i);
                    }
                }
            }
        } else {
            res = 0;
            //Java Integer upbound is 2^31-1
            for (int i = 0; i <= max_bit; i++) {
                if (ch[i] == '1') {
                    res += (long) Math.pow(2, max_bit - i);
                }
            }
        }
        return res;
    }

    public static int convert2Integer_small(String binary_number_str, boolean isSigned) {
        char ch[] = binary_number_str.toCharArray();
        int res = 0;
        int max_bit = ch.length - 1;
        if (isSigned) {
            if (ch[0] == '1') {
                res = -1;
                for (int i = 1; i <= max_bit; i++) {
                    if (ch[i] == '0') {
                        res -= (int) Math.pow(2, max_bit - i);
                    }
                }
            } else {
                res = 0;
                for (int i = 1; i <= max_bit; i++) {
                    if (ch[i] == '1') {
                        res += (int) Math.pow(2, max_bit - i);
                    }
                }
            }
        } else {
            res = 0;
            //Java Integer upbound is 2^31-1
            for (int i = 0; i <= max_bit; i++) {
                if (ch[i] == '1') {
                    res += (long) Math.pow(2, max_bit - i);
                }
            }
        }
        return res;
    }

    public static int AND(int x, int y) {
        return x & y;
    }


    public static int convert2offset(String str, boolean isShiftedLeft) {
        return 0;
    }

    public static void main(String args[]) {
        String str32bit = "00011";//-1
        str32bit = "00110";
//        000000000000000000001000100
        str32bit = "00001000001000100000000000001001".substring(6, 11);
        str32bit = "00001000001000100000000000001001".substring(11, 16);
        System.out.println(convert2Integer_small(str32bit, true));
    }


}
