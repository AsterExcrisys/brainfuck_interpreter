package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.models.dialects.Dialect;
import com.asterexcrisys.bfi.models.nodes.ProgramNode;
import com.asterexcrisys.bfi.services.Memory;
import com.asterexcrisys.bfi.services.Optimizer;
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

    public Interpreter(Dialect dialect) {
        parser = new Parser(dialect);
    }

    public Interpreter(String code, Dialect dialect) {
        parser = new Parser(code, dialect);
    }

    public void appendCode(String code) {
        parser.appendCode(code);
    }

    public void interpret(boolean shouldOptimize) {
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(parser.parse());
            program = optimizer.optimize();
        } else {
            program = parser.parse();
        }
        Memory memory = new Memory();
        program.execute(memory);
    }

    public static void interpret(byte[] bytecode, boolean shouldOptimize) {
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(Compiler.decompile(bytecode));
            program = optimizer.optimize();
        } else {
            program = Compiler.decompile(bytecode);
        }
        Memory memory = new Memory();
        program.execute(memory);
    }

}