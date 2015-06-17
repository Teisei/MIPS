package edu.ecnu.teisei.mips.instruction.cate1;

import edu.ecnu.teisei.mips.instruction.Ins_000;
import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.utils.MyFormat;
import edu.ecnu.teisei.mips.utils.MyMath;

import java.util.ArrayList;
import java.util.List;

/**
 * To test a GPR then do a PC-relative conditional branch.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_BGTZ extends Ins_000 {
    public Ins_BGTZ(String ins_code, String op_type) {
        super(ins_code, op_type);
        /* shifted  left by  two  bits.*/
        String offset_str = ins_code.substring(16, 32) + "00";
        setOffset(MyMath.convert2Integer_small(offset_str, true));
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + "R" + getRs() + MyFormat.split2 + MyFormat.imeOperand + getOffset();
    }


    /** Jump PC to . */
    @Override
    public void EXE() {
        if (CPU.getRegisterById(getRs()) > 0) {
            CPU.setPCByOffset(getOffset());
        }
    }

    @Override
    public List<String> getOperands() {
        List<String> list = new ArrayList<>();
        list.add("" + getRs());
        list.add("#" + getOffset());
        return list;
    }
}
