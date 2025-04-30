package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.exceptions.IllegalDebuggerStateException;
import com.asterexcrisys.bfi.models.Node;
import com.asterexcrisys.bfi.models.ProgramNode;
import com.asterexcrisys.bfi.services.Generator;
import com.asterexcrisys.bfi.services.Memory;
import com.asterexcrisys.bfi.services.Optimizer;
import com.asterexcrisys.bfi.services.Parser;
import java.util.Iterator;

@SuppressWarnings("unused")
public class Debugger implements AutoCloseable {

    private final Parser parser;
    private Generator<Node> generator;

    public Debugger() {
        parser = new Parser();
        generator = null;
    }

    public Debugger(String code) {
        parser = new Parser(code);
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

    public Node next() {
        if (generator == null) {
            throw new IllegalDebuggerStateException("debugger must be started first");
        }
        return generator.iterator().next();
    }

}