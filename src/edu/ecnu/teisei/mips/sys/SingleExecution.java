package edu.ecnu.teisei.mips.sys;

import edu.ecnu.teisei.mips.instruction.Ins_interface;
import edu.ecnu.teisei.mips.utils.MyFormat;

/**
 * execute the instructions one by one.
 * Created by Teisei on 2015/4/21.
 */
public class SingleExecution extends Execution implements ExecutionInterface {

    public void EXE(Ins_interface ins) {
        ins.EXE();
    }

    public void MEM(Ins_interface ins) {
        ins.MEM();
    }

    public void WB(Ins_interface ins) {
        ins.WB();
    }

    @Override
    public String oneCircle() {
        String ins_str = IF();
        Ins_interface ins = ID(ins_str);
        EXE(ins);
        MEM(ins);
        WB(ins);
        if (ins.getOpType().equals("BREAK")) {
            setNoMoreInstruction(true);
            setProgramEnd(true);
        }
        return ins.getAssemblyCode() + MyFormat.newLine;
    }

    @Override
    public void init() {

    }
}
