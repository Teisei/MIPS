package edu.ecnu.teisei.mips.instruction.cate1;

import edu.ecnu.teisei.mips.instruction.Ins_000;
import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.sys.Memory;
import edu.ecnu.teisei.mips.utils.MyFormat;

/**
 * To store a word to memor.
 * memory[base+offset] <-- rt
 * Created by Teisei on 2015/4/20.
 */
public class Ins_SW extends Ins_000 {
    private int address;
    private long value;
    public Ins_SW(String ins_code, String op_type) {
        super(ins_code, op_type);
    }

    /**  calculation of address for memory SW and get the value to be written */
    @Override
    public void EXE() {
        value =  CPU.getRegisterById(getRt());
        address = (int)CPU.getRegisterById(getRs()) + getOffset();
    }

    /**  write the new value to specific address in memory */
    @Override
    public void MEM() {
        Memory.setDataInteger(address, value);
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + "R" + getRt() + MyFormat.split2 + getOffset() + "(R" + getRs() + ")";
    }
}
