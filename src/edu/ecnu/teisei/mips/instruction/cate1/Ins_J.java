package edu.ecnu.teisei.mips.instruction.cate1;

import edu.ecnu.teisei.mips.instruction.Ins_000;
import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.utils.MyFormat;
import edu.ecnu.teisei.mips.utils.MyMath;

import java.util.ArrayList;
import java.util.List;

/**
 * To branch within the current 256 MB-aligned region.
 * Created by Teisei on 2015/4/19.
 */
public class Ins_J extends Ins_000 {
    int instr_index;
    public Ins_J(String ins_code, String op_type) {
        super(ins_code, op_type);
        /* shifted  left by  two  bits*/
        String instr_index_str = ins_code.substring(6, 32)+"00";

        instr_index = MyMath.convert2Integer_small(instr_index_str, true);
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + MyFormat.imeOperand + instr_index;
    }


    /** Jump PC to . */
    @Override
    public void EXE() {
        CPU.setPC(instr_index);
    }

    @Override
    public List<String> getOperands() {
        List<String> list = new ArrayList<>();
        list.add("#" + instr_index);
        return list;
    }
}
