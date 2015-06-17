package edu.ecnu.teisei.mips.instruction.cate2;

import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.instruction.Ins_110;

/**
 * To do a bitwise logical NOT OR.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_NOR extends Ins_110 {
    private long value;
    public Ins_NOR(String ins_code, String op_type) {
        super(ins_code, op_type);
    }

    @Override
    public void EXE() {
        value = ~(CPU.getRegisterById(getRs()) | CPU.getRegisterById(getRt()));
    }
    @Override
    public void WB() {
        CPU.setRegisterById(getRd(), value);
    }
}
