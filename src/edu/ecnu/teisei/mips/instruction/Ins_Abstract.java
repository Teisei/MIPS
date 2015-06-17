package edu.ecnu.teisei.mips.instruction;


import edu.ecnu.teisei.mips.sys.CPU;

import java.util.List;

/**
 * Created by Teisei on 2015/4/20.
 */
public class Ins_Abstract implements Ins_interface {
    //example: 000, OP(3 bits), rs(5 bits), rt(5 bits), offset(16 bits)
    String ins_code;//original code.
    public String op_type;//operation type.

    private final String _STAGE_INIT_ = "START";
    /* the operating stage if using pipeline.*/
    public String _STAGE_LEVEL_ = _STAGE_INIT_;//"IF","ID","EXE","MEM","WB".

    public Ins_Abstract(String ins_code, String op_type) {
        this.ins_code = ins_code;
        this.op_type = op_type;
    }

    @Override
    public String getStage() {
        return _STAGE_LEVEL_;
    }
    public String getIns_code() {
        return ins_code;
    }
    @Override
    public String getOpType() {
        return op_type;
    }
    @Override
    public String getAssemblyCode() {
        return null;
    }

    @Override
    public List<String> getOperands() {
        return null;
    }

    @Override
    public String getWBto() {
        return null;
    }

    @Override
    public void executeOneStage() {
        if (_STAGE_LEVEL_ == _STAGE_INIT_) {
            IF();
            _STAGE_LEVEL_ = "IF";
        }else if (_STAGE_LEVEL_.equals("IF")) {
            ID();
            _STAGE_LEVEL_ = "ID";
        }else if (_STAGE_LEVEL_.equals("ID")) {
            EXE();
            _STAGE_LEVEL_ = "ISSUE";
        }else if (_STAGE_LEVEL_.equals("ISSUE")) {
            EXE();
            _STAGE_LEVEL_ = "EXE";
        }else if (_STAGE_LEVEL_.equals("EXE")) {
            MEM();
            _STAGE_LEVEL_ = "MEM";
        }else if (_STAGE_LEVEL_.equals("MEM")) {
            WB();
            _STAGE_LEVEL_ = "WB";
        }else if (_STAGE_LEVEL_.equals("WB")) {

        }
    }

    @Override
    public void executeTotally() {
        while (!_STAGE_LEVEL_.equals("WB")) {
            executeOneStage();
        }
    }

    /**
     * PC+=4 normally, means to the next instruction.
     * we can overwrite this method in some instruction jump.
     */
    @Override
    public void IF(){CPU.nextInstruction();}
    /** decode the instruction.*/
    @Override
    public void ID(){}

    /**  issue this instruction. */
    @Override
    public void ISSUE() {}

    /** execute the instruction.*/
    @Override
    public void EXE(){}
    /** access the memory.*/
    @Override
    public void MEM(){}
    /** write back.*/
    @Override
    public void WB() {}
}
