package edu.ecnu.teisei.mips.domain;

import edu.ecnu.teisei.mips.instruction.Ins_interface;

/**
 * One Example:
 * Instruction      Issue   Read operands   Execution complete  Write result
 * LD F6,34(R2)     true    true            true                true
 * LD F2,45(R3)     true    true            true                false
 * MULTD F0,F2,F4   true    false           false               false
 * SUBD F8,F6,F2    true    false           false               false
 * DIVD F10,F0,F6   true    false           false               false
 * ADDD F6,F8,F2    false   false           false               false
 *
 * Created by Teisei on 2015/5/25.
 */
public class InstructionStatus {
    Ins_interface Instruction;
    boolean Issue;
    boolean ReadOperands;
    boolean ExecutionComplete;
    boolean WriteBack;

    public boolean issue() {
        return Issue;
    }

    public void setIssue(boolean issue) {
        Issue = issue;
    }

    public boolean isReadOperands() {
        return ReadOperands;
    }

    public void setReadOperands(boolean readOperands) {
        ReadOperands = readOperands;
    }

    public boolean isExecutionComplete() {
        return ExecutionComplete;
    }

    public void setExecutionComplete(boolean executionComplete) {
        ExecutionComplete = executionComplete;
    }

    public boolean isWriteBack() {
        return WriteBack;
    }

    public void setWriteBack(boolean writeBack) {
        WriteBack = writeBack;
    }

    public InstructionStatus(Ins_interface instruction) {
        Instruction = instruction;
        Issue = ReadOperands = ExecutionComplete = WriteBack = false;
    }
}
