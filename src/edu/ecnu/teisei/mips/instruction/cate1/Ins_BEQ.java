package edu.ecnu.teisei.mips.instruction.cate1;

import edu.ecnu.teisei.mips.instruction.Ins_000;
import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.utils.MyFormat;
import edu.ecnu.teisei.mips.utils.MyMath;

/**
 * To compare GPRs then do a PC-relative conditional branch.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_BEQ extends Ins_000 {

    public Ins_BEQ(String ins_code, String op_type) {
        super(ins_code, op_type);
        /* shifted  left by  two  bits.*/
        String offset_str = ins_code.substring(16, 32) + "00";
        setOffset(MyMath.convert2Integer_small(offset_str, true));
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + "R" + getRs() + MyFormat.split2 + "R" + getRt() + MyFormat.split2 + MyFormat.imeOperand + getOffset();
    }

    /** Jump PC to . */
    @Override
    public void EXE() {
        if (CPU.getRegisterById(getRs()) == CPU.getRegisterById(getRt())) {
            CPU.setPCByOffset(getOffset());
        }
    }
}
