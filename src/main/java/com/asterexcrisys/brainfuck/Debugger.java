package com.asterexcrisys.brainfuck;

import com.asterexcrisys.brainfuck.exceptions.IllegalDebuggerStateException;
import com.asterexcrisys.brainfuck.models.dialects.Dialect;
import com.asterexcrisys.brainfuck.models.nodes.Node;
import com.asterexcrisys.brainfuck.models.nodes.ProgramNode;
import com.asterexcrisys.brainfuck.services.Generator;
import com.asterexcrisys.brainfuck.services.Memory;
import com.asterexcrisys.brainfuck.services.Optimizer;
import com.asterexcrisys.brainfuck.services.Parser;
import java.util.Iterator;

@SuppressWarnings("unused")
public class Debugger implements AutoCloseable {

    private final Parser parser;
    private Memory memory;
    private Generator<Node> generator;
    private Iterator<Node> iterator;

    public Debugger() {
        parser = new Parser();
        memory = null;
        generator = null;
        iterator = null;
    }

    public Debugger(String code) {
        parser = new Parser(code);
        memory = null;
        generator = null;
        iterator = null;
    }

    public Debugger(Dialect dialect) {
        parser = new Parser(dialect);
        memory = null;
        generator = null;
        iterator = null;
    }

    public Debugger(String code, Dialect dialect) {
        parser = new Parser(code, dialect);
        memory = null;
        generator = null;
        iterator = null;
    }

    public void appendCode(String code) {
        parser.appendCode(code);
    }

    public void start(boolean shouldOptimize) {
        if (generator != null) {
            return;
        }
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(parser.parse());
            program = optimizer.optimize();
        } else {
            program = parser.parse();
        }
        memory = new Memory();
        generator = program.executeOnce(memory);
        iterator = generator.iterator();
    }

    public void close() throws Exception {
        memory.close();
        memory = null;
        generator.close();
        generator = null;
    }

    public Iterator<Node> iterator() {
        if (generator == null) {
            throw new IllegalDebuggerStateException("debugger must be started first");
        }
        return generator.iterator();
    }

    public boolean hasNext() {
        if (iterator == null) {
            throw new IllegalDebuggerStateException("debugger must be started first");
        }
        return iterator.hasNext();
    }

    public Node next() {
        if (iterator == null) {
            throw new IllegalDebuggerStateException("debugger must be started first");
        }
        return iterator.next();
    }

    public static Generator<Node> debug(byte[] bytecode, boolean shouldOptimize) {
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(Compiler.decompile(bytecode));
            program = optimizer.optimize();
        } else {
            program = Compiler.decompile(bytecode);
        }
        Memory memory = new Memory();
        return program.executeOnce(memory);
    }

}