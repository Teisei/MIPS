package edu.ecnu.teisei.mips.sys;

import edu.ecnu.teisei.mips.domain.InstructionStatus;
import edu.ecnu.teisei.mips.instruction.Ins_interface;
import edu.ecnu.teisei.mips.utils.MyFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Teisei on 2015/5/24.
 */
public class PipelineExecution extends Execution implements ExecutionInterface {
    /**
     * Status
     */
    /**
     * table1: Instruction status
     */
    private Map<Ins_interface, InstructionStatus> instructionStatusMap = new HashMap<>();

    /*  table2: Functional unit status */

    /*  table3: Register result status */
    private Map<Integer, String> registerStatusMap = new HashMap<>();//read, write
    private Map<Integer, Ins_interface> registerResultStatusMap = new HashMap<>();

    /**
     * data in IF unit.
     */
    /* waiting for its operand.*/
    private Ins_interface IF_WaitingInstruction;
    /* operand is executed in this circle. */
    private Ins_interface IF_ExecutingInstruction;

    /**
     * Queue
     */
    private boolean isPreIssueQueOpen = true;
    private ArrayBlockingQueue<Ins_interface> PreIssueQue = new ArrayBlockingQueue<>(4);
    private ArrayBlockingQueue<Ins_interface> PreALUQue = new ArrayBlockingQueue<>(2);
    private ArrayBlockingQueue<Ins_interface> PreMEMQue = new ArrayBlockingQueue<>(1);
    private ArrayBlockingQueue<Ins_interface> PostMEMQue = new ArrayBlockingQueue<>(1);
    private ArrayBlockingQueue<Ins_interface> PostALUQue = new ArrayBlockingQueue<>(1);


    private ArrayBlockingQueue<Ins_interface> WBEndQue = new ArrayBlockingQueue<>(2);


    /**
     * if register are ready( or immediate target value).
     * when all the source operands are ready.
     */
    private boolean checkRegistersReady(Ins_interface ins) {
        List<String> operand = ins.getOperands();
        if (operand != null) {
            for (String e : operand) {
                int op = MyFormat.getLocation(e);
                if (op > 0) {
                    String status = registerStatusMap.get(op);
                    if (status == null) {
                        registerStatusMap.put(op, "");
                    }
//                    else if (status.equals("read")) {
//                        return false;
//                    }
                    else if (status.equals("write")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * No structural hazards.
     * Pre-ALU has empty slots at the end of last cycle.
     */
    private boolean noStructuralHazards(Ins_interface ins) {
        return false;
    }

    /**
     * No WAW hazards with active instructions.
     * (issued but not finished, or earlier not-issued instructions.)
     */
    private boolean noWAWwithActiveInstructions(Ins_interface ins) {
        return false;
    }


    private boolean allStoreIssued(ArrayBlockingQueue<Ins_interface> queue) {
        for (Ins_interface ealier : queue) {
            if (ealier.getOpType().equals("SW")) {
                return false;
            }
        }
        return true;
    }

    private boolean noRAW(Ins_interface instruction) {
        List<String> operands = instruction.getOperands();
        for (String e : operands) {
            int op = MyFormat.getLocation(e);
            String status = registerStatusMap.get(op);
            if (status == null) {
                registerStatusMap.put(op, "");
            } else if (status.equals("read")) {
                return false;
            }
        }
        return true;
    }
    private boolean noRAW(Ins_interface instruction, Ins_interface ealier) {
        if(ealier==null) return true;
        List<String> operands = instruction.getOperands();
        if(operands==null || ealier.getWBto()==null) return true;
        for (String e : operands) {
            int op = MyFormat.getLocation(e);
            if (op > 0) {
                if (op == MyFormat.getLocation(ealier.getWBto())) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean noRAW(Ins_interface instruction, ArrayBlockingQueue<Ins_interface> queue) {
        for (Ins_interface ealier : queue) {
            if (!noRAW(instruction, ealier)) {
                return false;
            }
        }
        return true;
    }





    private boolean noWAR(Ins_interface instruction) {
        int op = MyFormat.getLocation(instruction.getWBto());
        String status = registerStatusMap.get(op);
        if (status == null) {
            registerStatusMap.put(op, "");
        }else if (status.equals("read")) {
            return false;
        }
        return true;
    }
    private boolean noWAR(Ins_interface instruction, Ins_interface ealier) {
        if(ealier==null) return true;
        List<String> operands = ealier.getOperands();
        if(operands==null||instruction.getWBto()==null) return true;
        for (String e : operands) {
            int op = MyFormat.getLocation(e);
            if (op > 0) {
                if (op == MyFormat.getLocation(instruction.getWBto())) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean noWAR(Ins_interface instruction, ArrayBlockingQueue<Ins_interface> queue) {
        for (Ins_interface ealier : queue) {
            if (!noWAR(instruction, ealier)) {
                return false;
            }
        }
        return true;
    }



    private boolean noWAW(Ins_interface instruction) {
        int op = MyFormat.getLocation(instruction.getWBto());
        String status = registerStatusMap.get(op);
        if (status == null) {
            registerStatusMap.put(op, "");
        }else if (status.equals("write")) {
            return false;
        }
        return true;
    }
    private boolean noWAW(Ins_interface instruction, Ins_interface ealier) {
        if(ealier==null) return true;
        if(instruction.getWBto()==null||ealier.getWBto()==null) return true;
//        String status1 = ealier.getWBto();
//        String status2 = instruction.getWBto();
//        if(status1==null) registerStatusMap.put(ealier, "");
        return MyFormat.getLocation(ealier.getWBto()) != MyFormat.getLocation(instruction.getWBto());
    }

    private boolean noWAW(Ins_interface instruction, ArrayBlockingQueue<Ins_interface> queue) {
        for (Ins_interface ealier : queue) {
            if (!noWAW(instruction, ealier)) {
                return false;
            }
        }
        return true;
    }

    /**
     * If two instructions are issued in a cycle,
     * you need to make sure that there are no WAW or WAR hazards between them.
     * Example:
     * WAW:
     * ADD R1 R2 R3
     * ADD R1 R4 R5
     * WAR:
     * ADD R1 R2 R3
     * ADD R2 R4 R5
     */
    private boolean noWAWorWAR(Ins_interface a, Ins_interface b) {
        if (b == null) return true;
        if (noWAW(a, b) && noWAR(a, b)) return true;
        return false;
    }

    /**
     * No WAR hazards with earlier not-issued instructions.
     * Example:
     * (waiting for R5)
     * 0, ADD R4 R5 R1
     * 1, ADD R1 R2 R3 <--
     * ins(1) has a WAR hazard with ins(0).
     * */


    /**
     * For MEM hazards with earlier not-issued instructions. */

    /**
     * The load instruction must wait until all the previous stores are issued. */

    /**
     * The stores must be issued in order.
     */


    private ArrayBlockingQueue<Ins_interface>InstructionFetchUpdateQue = new ArrayBlockingQueue<>(2);
    private void InstructionFetchUpdate() {
        int i=0;
        while (!InstructionFetchUpdateQue.isEmpty() && i < 2) {
            Ins_interface ins = InstructionFetchUpdateQue.poll();
            PreIssueQue.add(ins);
            instructionStatusMap.put(ins, new InstructionStatus(ins));
            i++;
        }
    }
    private void FETCH(Ins_interface ins) {
        InstructionFetchUpdateQue.add(ins);
    }
    /**
     * INSTRUCTION FETCH
     */
    private void InstructionFetch() {
        if (FetchOneInstruction())
            FetchOneInstruction();
    }
    private boolean FetchOneInstruction() {
        if (isPreIssueQueOpen) {
            IF_WaitingInstruction = IF_ExecutingInstruction = null;
        }
        if (PreIssueQue.size() < 4 && isPreIssueQueOpen) {
            String ins_str = IF();
            Ins_interface ins = ID(ins_str);
            if (ins.getOpType().equals("BEQ") || ins.getOpType().equals("BGTZ") || ins.getOpType().equals("J")) {
                if (checkRegistersReady(ins) && noRAW(ins, PreIssueQue)&& noRAW(ins, InstructionFetchUpdateQue)) {
                    IF_WaitingInstruction = null;
                    IF_ExecutingInstruction = ins;
//                    ins.EXE();
                    isPreIssueQueOpen = false;
                    return false;
                } else {
                    IF_WaitingInstruction = ins;
                    isPreIssueQueOpen = false;
                    return false;
                }
            } else if (ins.getOpType().equals("BREAK")) {
                IF_ExecutingInstruction = ins;
                setNoMoreInstruction(true);
                setProgramEnd(true);
                return false;
            } else {
                FETCH(ins);
                return true;
            }
        } else if (!isPreIssueQueOpen) {
            if (IF_WaitingInstruction != null) {
                if (checkRegistersReady(IF_WaitingInstruction) && noRAW(IF_WaitingInstruction, PreIssueQue)) {
                    IF_ExecutingInstruction = IF_WaitingInstruction;
                    IF_WaitingInstruction = null;
                    IF_ExecutingInstruction.EXE();
                    isPreIssueQueOpen = true;
                }
                return false;
            } else {
                //IF_EXECUTION != null
                IF_ExecutingInstruction.EXE();
                IF_ExecutingInstruction = null;
                isPreIssueQueOpen = true;
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * read operands from register file,
     * and issue instruction when all the source operands are ready.
     */
    private boolean canIssue(Ins_interface ins) {
        return false;
    }


    private ArrayBlockingQueue<Ins_interface> ISSUEUpdateQue = new ArrayBlockingQueue<>(2);
    private void ISSUEUpdate() {
        int i=0;
        while (!ISSUEUpdateQue.isEmpty() && i < 2) {
            Ins_interface ins = ISSUEUpdateQue.poll();
            PreALUQue.add(ins);
            instructionStatusMap.get(ins).setIssue(true);
            if(ins.getOperands()!=null)
                for (String e : ins.getOperands()) {
                    int op = MyFormat.getLocation(e);
                    registerStatusMap.put(op, "read");
                }
            if(ins.getWBto()!=null)
                registerStatusMap.put(MyFormat.getLocation(ins.getWBto()), "write");
            i++;
        }
    }
    private void ISSUE(Ins_interface ins) {
        PreIssueQue.remove(ins);
        ISSUEUpdateQue.add(ins);

    }
    /**
     * ISSUE:
     */
    private void ISSUE() {
        /*  ealier not issued instructions. */
        ArrayBlockingQueue<Ins_interface> earlierNotIssuedIns = new ArrayBlockingQueue<>(4);
        ArrayBlockingQueue<Ins_interface> IssuedIns = new ArrayBlockingQueue<>(4);

        Ins_interface pre_inf = null;
        /* No structural hazards.*/
        if (PreALUQue.size() == 2) return;
        if (PreALUQue.size() == 1) pre_inf = PreALUQue.peek();
        for (Ins_interface ins : PreIssueQue) {
            if (IssuedIns.size() == 2) {
                break;
            }
            boolean isFetched = false;
            if (checkRegistersReady(ins)) {
                if (noWAW(ins, earlierNotIssuedIns)&&noWAR(ins, earlierNotIssuedIns)) {
                    Ins_interface ealier_issue = IssuedIns.peek();
                    if (noWAWorWAR(ins, ealier_issue) && noRAW(ins, ealier_issue)) {
                        if (ins.getOpType().equals("LW") || ins.getOpType().equals("SW")) {
                            if (allStoreIssued(earlierNotIssuedIns)) {
                                IssuedIns.add(ins);
                                isFetched = true;
                            }
                        } else {
                            IssuedIns.add(ins);
                            isFetched = true;
                        }
                    }
                }
            }
            if (!isFetched) {
                earlierNotIssuedIns.add(ins);
            }
        }
        for (Ins_interface ins : IssuedIns) {
            ISSUE(ins);
//            PreIssueQue.remove(ins);
//            PreALUQue.add(ins);
//            instructionStatusMap.get(ins).setIssue(true);
        }
        earlierNotIssuedIns.clear();
        IssuedIns.clear();
    }



    private ArrayBlockingQueue<Ins_interface> EXECUTIONUpdateQue = new ArrayBlockingQueue<>(1);
    private void EXECUTIONUpdate() {
        int i=0;
        while (!EXECUTIONUpdateQue.isEmpty() && i < 1) {
            Ins_interface ins = EXECUTIONUpdateQue.poll();
            instructionStatusMap.get(ins).setExecutionComplete(true);
            if (ins.getOpType().equals("LW")||ins.getOpType().equals("SW")) {
                PreMEMQue.add(ins);
            }else {
                PostALUQue.add(ins);
            }
            i++;
        }
    }
    public void EXECUTION(Ins_interface ins) {
        ins.EXE();
        EXECUTIONUpdateQue.add(ins);
    }
    /**
     * EXECUTE:
     */
    public void EXECUTION() {
        Ins_interface ins = PreALUQue.poll();
        if (ins != null) {
            EXECUTION(ins);
        }
    }



    private ArrayBlockingQueue<Ins_interface> MEMORYUpdateQue = new ArrayBlockingQueue<>(1);
    private void MEMORYUpdate() {
        int i=0;
        while (!MEMORYUpdateQue.isEmpty() && i < 1) {
            Ins_interface ins = MEMORYUpdateQue.poll();
            if(!ins.getOpType().equals("SW"))
                PostMEMQue.add(ins);
            i++;
        }
    }
    /**
     * MEMORY:
     */
    public void MEMORY() {
        Ins_interface ins = PreMEMQue.poll();
        if (ins != null) {
            ins.MEM();
            MEMORYUpdateQue.add(ins);
        }
    }




    private void WB(Ins_interface ins) {
        ins.WB();
        for (String e : ins.getOperands()) {
            int op = MyFormat.getLocation(e);
            registerStatusMap.put(op, "");
        }
        registerStatusMap.put(MyFormat.getLocation(ins.getWBto()), "");
    }
    /**
     * WRITE BACK:
     */
    public void WB() {
        WBEndQue.clear();
        Ins_interface ins = PostMEMQue.poll();
        if (ins != null) WBEndQue.add(ins);
        ins = PostALUQue.poll();
        if (ins != null) WBEndQue.add(ins);
    }

    private void WBUpdate() {
        Ins_interface ins;
        while (!WBEndQue.isEmpty()) {
            ins = WBEndQue.poll();
            WB(ins);
        }
    }


    @Override
    public void init() {

        instructionStatusMap = new HashMap<>();

    /*  table2: Functional unit status */
    /*  table3: Register result status */
        registerStatusMap = new HashMap<>();//yes, no
        registerResultStatusMap = new HashMap<>();

        /**
         * data in IF unit.
         */
        /* waiting for its operand.*/
        IF_WaitingInstruction = null;
        /* operand is executed in this circle. */
        IF_ExecutingInstruction = null;

        /**
         * Queue
         */
        isPreIssueQueOpen = true;
        PreIssueQue = new ArrayBlockingQueue<>(4);
        PreALUQue = new ArrayBlockingQueue<>(2);
        PreMEMQue = new ArrayBlockingQueue<>(1);
        PostMEMQue = new ArrayBlockingQueue<>(1);
        PostALUQue = new ArrayBlockingQueue<>(1);
        WBEndQue = new ArrayBlockingQueue<>(2);
        setNoMoreInstruction(false);
        setProgramEnd(false);
    }

    /**
     * what CPU does in one circle.
     */
    public String oneCircle() {


        /**  INSTRUCTION FETCH */
        InstructionFetch();
        /**  ISSUE:
         * Read operands from Register File and issue instructions when all source operands are ready.
         * It can issue up to two instructions out-of-order per cycle.
         * When an instruction is issued, it is removed from the Pre-issue Queue before the end of current cycle.*/
        ISSUE();
        InstructionFetchUpdate();

        /**  EXECUTE */
        EXECUTION();
        ISSUEUpdate();

        /**  MEMORY */
        MEMORY();

        /**  WRITE BACK */
        WB();
        EXECUTIONUpdate();
        MEMORYUpdate();

        /*  WB Update the registers. */
        WBUpdate();


        return printInfo();
    }


    /**
     * get the status of all parts for print.
     */
    public String printInfo() {
        Ins_interface ins;
        int i = 0;

        String res = "\n" +
                "\n" +
                "IF Unit:\n" +
                "\tWaiting Instruction:";
        if (IF_WaitingInstruction != null)
            res += "[" + IF_WaitingInstruction.getAssemblyCode() + "]";
        else
            res += "";

        res += "\n" +
                "\tExecuted Instruction:";
        if (IF_ExecutingInstruction != null)
            res += "[" + IF_ExecutingInstruction.getAssemblyCode() + "]";
        else
            res += "";


        res += "\nPre-Issue Queue:\n";
        ArrayBlockingQueue<String> que = new ArrayBlockingQueue<>(4);
        Iterator<Ins_interface> iter = PreIssueQue.iterator();
        while (iter.hasNext()) {
            ins = iter.next();
            que.add(ins.getAssemblyCode());
        }
        i = 0;
        while (!que.isEmpty()) {
            res += "\tEntry " + i + ":[" + que.poll() + "]\n";
            i++;
        }
        while (i <= 3) {
            res += "\tEntry " + i + ":\n";
            i++;
        }


        res += "Pre-ALU Queue:\n";
        que.clear();
        iter = PreALUQue.iterator();
        while (iter.hasNext()) {
            ins = iter.next();
            que.add(ins.getAssemblyCode());
        }
        i = 0;
        while (!que.isEmpty()) {
            res += "\tEntry " + i + ":[" + que.poll() + "]\n";
            i++;
        }
        while (i <= 1) {
            res += "\tEntry " + i + ":\n";
            i++;
        }


        ins = PreMEMQue.peek();
        if (ins != null) res += "Pre-MEM Queue:[" + ins.getAssemblyCode() + "]\n";
        else res += "Pre-MEM Queue:\n";
        ins = PostMEMQue.peek();
        if (ins != null) res += "Post-MEM Queue:[" + ins.getAssemblyCode() + "]\n";
        else res += "Post-MEM Queue:\n";
        ins = PostALUQue.peek();
        if (ins != null) res += "Post-ALU Queue:[" + ins.getAssemblyCode() + "]\n";
        else res += "Post-ALU Queue:\n";

        return res;
    }
}
