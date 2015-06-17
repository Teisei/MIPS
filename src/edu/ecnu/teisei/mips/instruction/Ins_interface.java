package edu.ecnu.teisei.mips.instruction;

import java.util.List;

/**
 * Created by Teisei on 2015/4/19.
 */
public interface Ins_interface {

    /**
     * get the stage this instruction is in of the pipeline.
     * @return
     */
    public String getStage();
    /**
     * get the original machine code.
     * @return
     */
    public String getIns_code();
    /**
     * get the assembly code of this instruction.
     * @return
     */
    public String getAssemblyCode();

    /**
     * get the operands of this instruction.
     */
    public List<String> getOperands();

    /**
     * object of the write back.
     */
    public String getWBto();
    /**
     * get the operation type of this instruction.
     * @return
     */
    public String getOpType();

    /**
     * execute one stage in the pipeline.
     */
    public void executeOneStage();

    /**
     * execute this instruction only.
     */
    public void executeTotally();

    /** get this instruction. */
    public void IF();
    /** decode this instrcution */
    public void ID();

    /**
     * Issue this instruction.
     */
    public void ISSUE();

    /**
     * the execution part.
     */
    public void EXE();

    /**
     * the memory part
     */
    public void MEM();

    /**
     * write back
     */
    public void WB();
}
