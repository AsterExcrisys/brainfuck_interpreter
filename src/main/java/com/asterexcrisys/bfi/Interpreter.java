package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.models.ProgramNode;
import com.asterexcrisys.bfi.services.Memory;
import com.asterexcrisys.bfi.services.Parser;

@SuppressWarnings("unused")
public class Interpreter {

    private final Parser parser;

    public Interpreter() {
        parser = new Parser();
    }

    public Interpreter(String code) {
        parser = new Parser(code);
    }

    public void appendCode(String code) {
        parser.appendCode(code);
    }

    public void interpret() {
        ProgramNode program = parser.parse();
        Memory memory = new Memory();
        program.execute(memory);
    }

}