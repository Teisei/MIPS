package edu.ecnu.teisei.mips.instruction.cate2;

import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.instruction.Ins_110;

/**
 * to subtract 32-bit integers. If overflow occurs, then trap.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_SUB  extends Ins_110 {
    private long value;
    public Ins_SUB(String ins_code, String op_type) {
        super(ins_code, op_type);
    }

    @Override
    public void EXE() {
        value = CPU.getRegisterById(getRs()) - CPU.getRegisterById(getRt());
    }

    @Override
    public void WB() {
        CPU.setRegisterById(getRd(), value);
    }
}
