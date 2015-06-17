package edu.ecnu.teisei.mips.instruction.cate1;

import edu.ecnu.teisei.mips.instruction.Ins_000;
import edu.ecnu.teisei.mips.sys.CPU;
import edu.ecnu.teisei.mips.sys.Memory;
import edu.ecnu.teisei.mips.utils.MyFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * To load a word from memory as a signed value.
 * Created by Teisei on 2015/4/20.
 */
public class Ins_LW extends Ins_000 {
    private int address;
    private long value;

    public Ins_LW(String ins_code, String op_type) {
        super(ins_code, op_type);
    }

    /**  calculation of address for memory LW */
    @Override
    public void EXE() {
        address = (int) CPU.getRegisterById(getRs()) + getOffset();
    }

    /**  read the data from memory. */
    @Override
    public void MEM() {
        value = Memory.getDataInteger(address);
    }

    /**  write back tp register */
    @Override
    public void WB() {
        CPU.setRegisterById(getRt(), value);
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + "R" + getRt() + MyFormat.split2 + getOffset() + "(R" + getRs() + ")";
    }

    @Override
    public List<String> getOperands() {
        List<String> list = new ArrayList<>();
        list.add("" + getRs());
        list.add("#" + getOffset());
        return list;
    }

    @Override
    public String getWBto() {
        return "" + getRt();
    }
}
