package edu.ecnu.teisei.mips.instruction.cate1;

import edu.ecnu.teisei.mips.instruction.Ins_000;

/**
 * To cause a Breakpoint exception.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_BREAK extends Ins_000 {
    public Ins_BREAK(String ins_code, String op_type) {
        super(ins_code, op_type);
    }

    @Override
    public String getAssemblyCode() {
        return getOpType();
    }
}
