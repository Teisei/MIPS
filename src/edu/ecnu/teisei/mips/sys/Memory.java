package edu.ecnu.teisei.mips.sys;

import edu.ecnu.teisei.mips.utils.MyFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Teisei on 2015/4/19.
 */
public class Memory {
    private static int _ins_start_ = 128;
    static int _ins_index_;
    private static int _data_start_;
    static int _data_index_;

    /**
     * size of one instruction
     */
    private static int _ins_size_bytes_ = 4;
    /**
     * size of one data(Integer)
     */
    private static int _data_size_bytes_ = 4;
    /**
     * 128 - 180
     */
    static Map<Integer, String> instructionMap;
    //String instructions[] = new String[14];
    /**
     * 184 - 244
     */
    static Map<Integer, Long> dataMap;
    //int data[] = new int[14];

    static{
        instructionMap = new HashMap<>();
        _ins_index_ = _ins_start_;

    }

    public static void addInstruction(String line) {
        instructionMap.put(_ins_index_, line);
        _ins_index_ += _ins_size_bytes_;
    }

    public static String getInstruction(int address) {
        return instructionMap.get(address);
    }

    public static void addDataInMemory(long a) {
        dataMap.put(_data_index_, a);
        _data_index_ += _data_size_bytes_;
    }

    public static void setDataInteger(int address, long value) {
        dataMap.put(address, value);
    }

    public static long getDataInteger(int address) {
        return dataMap.get(address);
    }

    public static void readyForData() {
        dataMap = new HashMap<>();
        _data_start_ = _ins_index_;
        _data_index_ = _data_start_;
    }

    public static String printStatesOfDataInMemory() {
        String res = "";
        res+="Data"+ MyFormat.newLine;
        int i = _data_start_;
        int max = _data_index_;
        while (i < max) {
            res += "" + i + ":";
            int j = 0;
            for (; i < max && j < 8; i += 4) {
                res += "\t" + dataMap.get(i);
                j++;
            }
            res+=MyFormat.newLine;
        }
        return res;
    }
}
