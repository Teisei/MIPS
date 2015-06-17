package edu.ecnu.teisei.mips.sys;

import edu.ecnu.teisei.mips.instruction.Ins_interface;
import edu.ecnu.teisei.mips.instruction.Parser;

/**
 * execute the instructions one by one.
 * Created by Teisei on 2015/4/21.
 */
public abstract class Execution implements ExecutionInterface {
    /**
     * the program is end.
     */
    private boolean ProgramEnd;
    public void setProgramEnd(boolean programEnd) {
        ProgramEnd = programEnd;
    }
    public boolean isProgramEnd() {
        return ProgramEnd;
    }
    private boolean noMoreInstruction;

    public boolean isNoMoreInstruction() {
        return noMoreInstruction;
    }
    public void setNoMoreInstruction(boolean noMoreInstruction) {
        this.noMoreInstruction = noMoreInstruction;
    }

    /**
     * get an instruction from the memory.
     */
    public static String IF() {
        String ins_str = CPU.getCurrentInstruction();
        CPU.nextInstruction();
        return ins_str;
    }
    /**
     * decode the instruction.
     */
    public static Ins_interface ID(String ins_str) {
        Ins_interface ins = Parser.parse(ins_str);
        ins.ID();
        return ins;
    }
}
