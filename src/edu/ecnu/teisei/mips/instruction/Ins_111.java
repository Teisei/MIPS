package edu.ecnu.teisei.mips.instruction;

import edu.ecnu.teisei.mips.utils.MyFormat;
import edu.ecnu.teisei.mips.utils.MyMath;

import java.util.ArrayList;
import java.util.List;

/**
 * 111: nstruction Category-3.
 * Created by Teisei on 2015/4/19.
 */
public class Ins_111 extends Ins_Abstract implements Ins_interface  {
    //example: 111, rs(5 bits), rt(5 bits), opcode(3 bits), immediate_value (16 bits)
    int rs;
    int rt;
    int immediate_value;

    public int getImmediate_value() {
        return immediate_value;
    }

    private void setImmediate_value(int immediate_value) {
        this.immediate_value = immediate_value;
    }

    public int getRt() {
        return rt;
    }

    private void setRt(int rt) {
        this.rt = rt;
    }

    public int getRs() {
        return rs;
    }

    private void setRs(int rs) {
        this.rs = rs;
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + "R" + getRt() + MyFormat.split2 + "R" + getRs() + MyFormat.split2 + MyFormat.imeOperand + getImmediate_value();
    }

    public Ins_111(String ins_code, String op_type) {
        super(ins_code, op_type);
        rs = MyMath.convert2Integer_small(ins_code.substring(3, 8), true);
        rt = MyMath.convert2Integer_small(ins_code.substring(8, 13), true);
        immediate_value = MyMath.convert2Integer_small(ins_code.substring(16, 32), true);
    }

    @Override
    public List<String> getOperands() {
        List<String> list = new ArrayList<>();
        list.add("" + getRs());
        list.add("#" + immediate_value);
        return list;
    }

    @Override
    public String getWBto() {
        return "" + getRt();
    }
}
