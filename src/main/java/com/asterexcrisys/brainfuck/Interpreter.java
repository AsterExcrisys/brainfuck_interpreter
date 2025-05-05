package com.asterexcrisys.brainfuck;

import com.asterexcrisys.brainfuck.exceptions.StreamInteractionException;
import com.asterexcrisys.brainfuck.models.dialects.Dialect;
import com.asterexcrisys.brainfuck.models.nodes.ProgramNode;
import com.asterexcrisys.brainfuck.services.Memory;
import com.asterexcrisys.brainfuck.services.Optimizer;
import com.asterexcrisys.brainfuck.services.Parser;
import java.io.IOException;

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
        try (Memory memory = new Memory()) {
            program.execute(memory);
        } catch (IOException exception) {
            throw new StreamInteractionException("Encountered an error while closing input/output stream: " + exception.getMessage());
        }
    }

    public static void interpret(byte[] bytecode, boolean shouldOptimize) {
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(Compiler.decompile(bytecode));
            program = optimizer.optimize();
        } else {
            program = Compiler.decompile(bytecode);
        }
        try (Memory memory = new Memory()) {
            program.execute(memory);
        } catch (IOException exception) {
            throw new StreamInteractionException("Encountered an error while closing input/output stream: " + exception.getMessage());
        }
    }

}