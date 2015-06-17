package edu.ecnu.teisei.mips.instruction;

import edu.ecnu.teisei.mips.utils.MyMath;

import java.util.ArrayList;
import java.util.List;

/**
 * 000: nstruction Category-1.
 * Created by Teisei on 2015/4/19.
 */
public abstract class Ins_000 extends Ins_Abstract implements Ins_interface {
    //example: 000, OP(3 bits), rs(5 bits), rt(5 bits), offset(16 bits)
    //000 111 00110 00011 0000000010111000: LW R3, 184(R6)
    int rs;//source register
    int rt;//target register
    int offset;//

    public int getRs() {
        return rs;
    }

    private void setRs(int rs) {
        this.rs = rs;
    }

    public int getRt() {
        return rt;
    }

    private void setRt(int rt) {
        this.rt = rt;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Ins_000(String ins_code, String op_type) {
        super(ins_code, op_type);
        rs = MyMath.convert2Integer_small(ins_code.substring(6, 11), true);
        rt = MyMath.convert2Integer_small(ins_code.substring(11, 16), true);
        offset = MyMath.convert2Integer_small(ins_code.substring(16, 32), true);
    }

    @Override
    public List<String> getOperands() {
        List<String> list = new ArrayList<>();
        list.add("" + getRs());
        list.add("" + getRt());
        list.add("#" + getOffset());
        return list;
    }
}
