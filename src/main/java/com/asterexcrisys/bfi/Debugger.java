package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.exceptions.IllegalDebuggerStateException;
import com.asterexcrisys.bfi.models.dialects.Dialect;
import com.asterexcrisys.bfi.models.nodes.Node;
import com.asterexcrisys.bfi.models.nodes.ProgramNode;
import com.asterexcrisys.bfi.services.Generator;
import com.asterexcrisys.bfi.services.Memory;
import com.asterexcrisys.bfi.services.Optimizer;
import com.asterexcrisys.bfi.services.Parser;
import java.util.Iterator;

@SuppressWarnings("unused")
public class Debugger implements AutoCloseable {

    private final Parser parser;
    private Generator<Node> generator;
    private Iterator<Node> iterator;

    public Debugger() {
        parser = new Parser();
        generator = null;
        iterator = null;
    }

    public Debugger(String code) {
        parser = new Parser(code);
        generator = null;
        iterator = null;
    }

    public Debugger(Dialect dialect) {
        parser = new Parser(dialect);
        generator = null;
        iterator = null;
    }

    public Debugger(String code, Dialect dialect) {
        parser = new Parser(code, dialect);
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
        Memory memory = new Memory();
        generator = program.executeOnce(memory);
        iterator = generator.iterator();
    }

    public void close() throws Exception {
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