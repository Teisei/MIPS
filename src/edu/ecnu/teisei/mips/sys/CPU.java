package edu.ecnu.teisei.mips.sys;

import edu.ecnu.teisei.mips.instruction.Ins_interface;
import edu.ecnu.teisei.mips.instruction.Parser;
import edu.ecnu.teisei.mips.utils.FileUtil;
import edu.ecnu.teisei.mips.utils.MyFile;
import edu.ecnu.teisei.mips.utils.MyFormat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * CPU.
 * Created by Teisei on 2015/4/19.
 */
public class CPU {

    static FileUtil fileUtil = new FileUtil();
    static String output1 = "disassembly.txt";
    static String output2 = "simulation.txt";
    static String _encode_ = "utf-8";
    static String _new_line_ = "\r\n";
    static boolean isPipeline = false;

    static int _PC_initial_ = 128;
    static int PC;

    /* Current instruction.*/
    static String CURRENT_INSTRUCTION;

    static int Cicle;

    /* 32 registers.*/
    static Map<Integer, Long> registerMap;


    public static int getPC() {
        return PC;
    }
    public static int getCicle() {
        return Cicle;
    }

    public static long getRegisterById(int id) {
        return registerMap.get(id);
    }

    public static void setRegisterById(int id, long value) {
        registerMap.put(id, value);
    }
    public static void disassemble(String path) {

    }

    public static String getCurrentInstruction() {
        CURRENT_INSTRUCTION = Memory.getInstruction(PC);
        return CURRENT_INSTRUCTION;
    }

    public static void nextInstruction() {
        PC += 4;//every instruction sized of 4 bytes.
    }

    public static void setPC(int jumpto) {
        PC = jumpto;
    }

    public static void setPCByOffset(int offset) {
        PC += offset;
    }

    public static void nextCicle() {
        Cicle++;
    }

    public static void loadInsAndData(String path) {
        MyFile myFile = new MyFile(path, _encode_);
        String lines[] = myFile.read().split(_new_line_);
        boolean isData = false;
        String temp = "";
        for (String res : lines) {
            if (!isData) {
                /* parse the instruction, load it into Memory(Instruction block) and check if it is a BREAK.*/
                Ins_interface ins = Parser.parse(res);

                temp += res + MyFormat.split0 + Memory._ins_index_ + MyFormat.split0 + ins.getAssemblyCode() + MyFormat.newLine;
                //fileUtil.write(output1, res + MyFormat.split0 + Memory._ins_index_ + MyFormat.split1 + ins.getAssemblyCode() + MyFormat.newLine, true, _encode_);

                Memory.addInstruction(res);
                if (ins.getOpType().equals("BREAK")) {
                    isData = true;
                    Memory.readyForData();
                }
            } else {
                /* parse the data(for this project, only Integers), load it into Memory(Data block).*/
                long operand = Parser.parseData(res);
                temp += res + MyFormat.split0 + Memory._data_index_ + MyFormat.split0 + operand + MyFormat.newLine;
                Memory.addDataInMemory(operand);
            }
        }
        myFile.close();
        System.out.println(temp);
        fileUtil.write(output1, temp, false, _encode_);
    }


    /**
     * whole life of a program.
     */
    public static void runProgram(boolean isPipeline) {
        String print = "";
        ExecutionInterface executor;

        if(!isPipeline)
            executor = new SingleExecution(); /**  without Pipeline. */
        else
            executor = new PipelineExecution();/**  with Pipeline. */
        executor.init();
        while (true) {
            String temp = "";
            if (Cicle > 1000) {
                break;
            }
            nextCicle();

            /* Parse this instruction from string to executable object.*/
            //Ins_interface ins = Parser.parse(Memory.getInstruction(PC));
            /*
                Execute this instruction totally.
                PC+4 normally, especially for some cases.
            */
            //ins.executeTotally();

            temp += "--------------------" + MyFormat.newLine;
            temp += "Cycle:" + CPU.getCicle();


            if(!isPipeline)
                temp += MyFormat.split0 + getPC() + MyFormat.split0;
            /********************************************************************/
            /** (IF,ID), EXE, MEM, WB.*/
            String ins_str = executor.oneCircle();
            /********************************************************************/


            temp += ins_str + MyFormat.newLine;

            temp += printStateOfRegisters();
            temp += MyFormat.newLine;
            temp += MyFormat.newLine;
            temp += Memory.printStatesOfDataInMemory();
            print += temp;

            /** If program break, end.*/
            if (executor.isProgramEnd()) {
                break;
            }

            if (!isPipeline) {
                temp += MyFormat.newLine;
                print += MyFormat.newLine;
            }
        }
        if(isPipeline)
            print = print.trim();
        fileUtil.write(output2, print, false, _encode_);
    }

    public static void init(String path) {
        File in = new File(path);
        String dir = in.getParent() == null ? "." : in.getParent();
        output1 = dir + File.separator + output1;
        output2 = dir + File.separator + output2;

        /* Init the PC state, pointer to the first instruction's address.*/
        PC = _PC_initial_;

        /* CPU Cicle.*/
        Cicle = 0;

        /* init the set of 32 Registers.*/
        registerMap = new HashMap<>();
        for (int i = 0; i < 32; i++) {
            registerMap.put(i, (long) 0);
        }

    }

    /**
     * simulate the operation of CPU without using Pipeline.
     */
    public static void simulate(String path) {
        init(path);
        loadInsAndData(path);
        runProgram(false);
    }

    public static void simulate(String path, boolean Pipeline) {
        init(path);
        loadInsAndData(path);
        isPipeline = Pipeline;
        runProgram(isPipeline);
    }

    public static void main(String args[]) {
        String in = args[0];
        boolean isPipeline = true;
        simulate(in, isPipeline);
    }

    /**
     * print the states of the 32 registers.
     */
    public static String printStateOfRegisters() {
        String res = "";
        res += "Registers" + MyFormat.newLine;
        int i = 0;
        res+="R00:";
        for (; i < 8; i++) {
            res+="\t" + registerMap.get(i);
        }
        res+=MyFormat.newLine;
        res+="R08:";
        for (; i < 16; i++) {
            res+="\t" + registerMap.get(i);
        }
        res+=MyFormat.newLine;
        res+="R16:";
        for (; i < 24; i++) {
            res+="\t" + registerMap.get(i);
        }
        res+=MyFormat.newLine;
        res+="R24:";
        for (; i < 32; i++) {
            res+="\t" + registerMap.get(i);
        }
        return res;
    }

}
