package edu.ecnu.teisei.mips.instruction;

import edu.ecnu.teisei.mips.instruction.cate1.*;
import edu.ecnu.teisei.mips.instruction.cate2.*;
import edu.ecnu.teisei.mips.instruction.cate3.Ins_ADDI;
import edu.ecnu.teisei.mips.instruction.cate3.Ins_ANDI;
import edu.ecnu.teisei.mips.instruction.cate3.Ins_ORI;
import edu.ecnu.teisei.mips.instruction.cate3.Ins_XORI;
import edu.ecnu.teisei.mips.utils.MyMath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Teisei on 2015/4/19.
 */
public class Parser {
    static Map<String, String> ins_000_map;
    static Map<String, String> ins_110_map;
    static Map<String, String> ins_111_map;

    static{
        ins_000_map = new HashMap<>();
        ins_000_map.put("000", "J");
        ins_000_map.put("010", "BEQ");
        ins_000_map.put("100", "BGTZ");
        ins_000_map.put("101", "BREAK");
        ins_000_map.put("110", "SW");
        ins_000_map.put("111", "LW");

        ins_110_map = new HashMap<>();
        ins_110_map.put("000", "ADD");
        ins_110_map.put("001", "SUB");
        ins_110_map.put("010", "MUL");
        ins_110_map.put("011", "AND");
        ins_110_map.put("100", "OR");
        ins_110_map.put("101", "XOR");
        ins_110_map.put("110", "NOR");

        ins_111_map = new HashMap<>();
        ins_111_map.put("000", "ADDI");
        ins_111_map.put("001", "ANDI");
        ins_111_map.put("001", "ORI");
        ins_111_map.put("001", "XORI");
    }

    /**
     * parse a instruction string into a certain-type, real-executable instruction.
     * @param ins instruction
     * @return instruction.
     */
    public static Ins_interface parse(String ins) {
        if (ins.startsWith("000")) {
            String op_type_code = ins.substring(3, 6);
            String op_type = ins_000_map.get(op_type_code);
            if (op_type.equals("BEQ")) {
                return new Ins_BEQ(ins, op_type);
            } else if (op_type.equals("BGTZ")) {
                return new Ins_BGTZ(ins, op_type);
            } else if (op_type.equals("BREAK")) {
                return new Ins_BREAK(ins, op_type);
            } else if (op_type.equals("J")) {
                return new Ins_J(ins, op_type);
            } else if (op_type.equals("LW")) {
                return new Ins_LW(ins, op_type);
            } else if (op_type.equals("SW")) {
                return new Ins_SW(ins, op_type);
            } else {
                return null;
            }
        } else if (ins.startsWith("110")) {
            String op_type_code = ins.substring(13, 16);
            String op_type = ins_110_map.get(op_type_code);
            if (op_type.equals("ADD")) {
                return new Ins_ADD(ins, op_type);
            } else if (op_type.equals("MUL")) {
                return new Ins_MUL(ins, op_type);
            } else if (op_type.equals("NOR")) {
                return new Ins_NOR(ins, op_type);
            } else if (op_type.equals("OR")) {
                return new Ins_OR(ins, op_type);
            } else if (op_type.equals("SUB")) {
                return new Ins_SUB(ins, op_type);
            } else if (op_type.equals("XOR")) {
                return new Ins_XOR(ins, op_type);
            } else {
                return null;
            }
        } else if (ins.startsWith("111")) {
            String op_type_code = ins.substring(13, 16);
            String op_type = ins_111_map.get(op_type_code);
            if (op_type.equals("ADDI")) {
                return new Ins_ADDI(ins, op_type);
            } else if (op_type.equals("ANDI")) {
                return new Ins_ANDI(ins, op_type);
            } else if (op_type.equals("ORI")) {
                return new Ins_ORI(ins, op_type);
            } else if (op_type.equals("XORI")) {
                return new Ins_XORI(ins, op_type);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * convert the input binary inte
     * @param str32bits
     * @return
     */
    public static long parseData(String str32bits) {
        return MyMath.convert2Integer(str32bits, true);
    }

    public static void main(String args[]) {
        String code = "11000011001000100010100000000000";
//        code = "11000000000000000000100000000000";
        Ins_interface ins = parse(code);
        System.out.println(ins.getOpType());
    }
}
