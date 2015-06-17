package edu.ecnu.teisei.mips.instruction.cate3;

import edu.ecnu.teisei.mips.instruction.Ins_111;
import edu.ecnu.teisei.mips.sys.CPU;

/**
 * To do a bitwise logical OR with a constant.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_ORI extends Ins_111 {
    private long value;
    public Ins_ORI(String ins_code, String op_type) {
        super(ins_code, op_type);
    }

    @Override
    public void EXE() {
        value = CPU.getRegisterById(getRs()) | getImmediate_value();
    }
    @Override
    public void WB() {
        CPU.setRegisterById(getRt(), value);
    }
}
