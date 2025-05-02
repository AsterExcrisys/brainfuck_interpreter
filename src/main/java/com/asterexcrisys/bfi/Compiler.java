package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.exceptions.InvalidSyntaxException;
import com.asterexcrisys.bfi.models.dialects.Dialect;
import com.asterexcrisys.bfi.models.instructions.InstructionType;
import com.asterexcrisys.bfi.models.nodes.CommandNode;
import com.asterexcrisys.bfi.models.nodes.LoopNode;
import com.asterexcrisys.bfi.models.nodes.Node;
import com.asterexcrisys.bfi.models.nodes.ProgramNode;
import com.asterexcrisys.bfi.services.Counter;
import com.asterexcrisys.bfi.services.Optimizer;
import com.asterexcrisys.bfi.services.Parser;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class Compiler {

    private final Parser parser;

    public Compiler() {
        parser = new Parser();
    }

    public Compiler(String code) {
        parser = new Parser(code);
    }

    public Compiler(Dialect dialect) {
        parser = new Parser(dialect);
    }

    public Compiler(String code, Dialect dialect) {
        parser = new Parser(code, dialect);
    }

    public void appendCode(String code) {
        parser.appendCode(code);
    }

    public byte[] compile(boolean shouldOptimize) {
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(parser.parse());
            program = optimizer.optimize();
        } else {
            program = parser.parse();
        }
        List<Byte> bytecode = new ArrayList<>();
        program.translate(bytecode);
        byte[] result = new byte[bytecode.size()];
        for (int i = 0; i < bytecode.size(); i++) {
            result[i] = bytecode.get(i);
        }
        return result;
    }

    public static ProgramNode decompile(byte[] bytecode) {
        if (bytecode == null || bytecode.length == 0) {
            return null;
        }
        List<Node> operations = new ArrayList<>();
        Stack<LoopNode> loops = new Stack<>();
        Stack<Counter> counts = new Stack<>();
        for (int i = 0; i < bytecode.length; i++) {
            byte operation = bytecode[i];
            switch (operation) {
                case 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C -> {
                    CommandNode command = decompileNode(bytecode, operation, i);
                    i += command.type().count();
                    if (loops.empty()) {
                        operations.add(command);
                    } else {
                        loops.peek().addOperation(command);
                        counts.peek().increment();
                        if (counts.peek().distance() == 0) {
                            loops.pop();
                            counts.pop();
                        }
                    }
                }
                case 0x0D -> {
                    InstructionType type = InstructionType.START_LOOP;
                    if (i + type.count() > bytecode.length - 1) {
                        throw new InvalidSyntaxException("Missing repetition/operation count metadata at position: " + i);
                    }
                    LoopNode loop = new LoopNode(bytecode[i + 1]);
                    int count = bytecode[i + 2];
                    i += type.count();
                    if (loops.empty()) {
                        operations.add(loop);
                    } else {
                        counts.peek().increment();
                        if (counts.peek().distance() == 0) {
                            loops.pop();
                            counts.pop();
                        }
                    }
                    loops.push(loop);
                    counts.push(new Counter(count, 0));
                }
                default -> throw new InvalidSyntaxException("Unrecognized operation '" + operation + "' at position: " + i);
            }
        }
        if (!loops.empty()) {
            throw new InvalidSyntaxException("Missing operations inside one or more loops");
        }
        return new ProgramNode(operations, 1);
    }

    private static CommandNode decompileNode(byte[] bytecode, byte operation, int index) {
        InstructionType type = InstructionType.fromCode(operation);
        if (type == null) {
            throw new InvalidSyntaxException("Unrecognized operation '" + operation + "' at position: " + index);
        }
        if (type.isRepeatable()) {
            if (index + type.count() > bytecode.length - 1) {
                throw new InvalidSyntaxException("Missing repetition count metadata at position: " + index);
            }
            return new CommandNode(type, bytecode[index + type.count()]);
        }
        return new CommandNode(type, 1);
    }

}