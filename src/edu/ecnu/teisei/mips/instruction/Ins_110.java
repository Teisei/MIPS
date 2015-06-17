package edu.ecnu.teisei.mips.instruction;

import edu.ecnu.teisei.mips.utils.MyFormat;
import edu.ecnu.teisei.mips.utils.MyMath;

import java.util.ArrayList;
import java.util.List;

/**
 * 110: nstruction Category-2.
 * Created by Teisei on 2015/4/19.
 */
public class Ins_110 extends Ins_Abstract implements Ins_interface {
    //example: 110, rs(5 bits), rt(5 bits), opcode(3 bits), rd(5 bits), 00000000000
    int rs;
    int rt;
    int rd;

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

    public int getRd() {
        return rd;
    }

    private void setRd(int rd) {
        this.rd = rd;
    }

    @Override
    public String getAssemblyCode() {
        return getOpType() + MyFormat.split1 + "R" + getRd() + MyFormat.split2 + "R" + getRs() + MyFormat.split2 + "R" + getRt();
    }

    public Ins_110(String ins_code, String op_type) {
        super(ins_code, op_type);
        rs = MyMath.convert2Integer_small(ins_code.substring(3, 8), true);
        rt = MyMath.convert2Integer_small(ins_code.substring(8, 13), true);
        rd = MyMath.convert2Integer_small(ins_code.substring(16, 21), true);
    }

    @Override
    public List<String> getOperands() {
        List<String> list = new ArrayList<>();
        list.add("" + getRs());
        list.add("" + getRt());
        return list;
    }

    @Override
    public String getWBto() {
        return "" + getRd();
    }
}
