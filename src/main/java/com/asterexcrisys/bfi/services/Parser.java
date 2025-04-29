package com.asterexcrisys.bfi.services;

import com.asterexcrisys.bfi.exceptions.InvalidSyntaxException;
import com.asterexcrisys.bfi.models.CommandNode;
import com.asterexcrisys.bfi.models.LoopNode;
import com.asterexcrisys.bfi.models.Node;
import com.asterexcrisys.bfi.models.ProgramNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class Parser {

    private final StringBuilder builder;

    public Parser() {
        builder = new StringBuilder();
    }

    public Parser(String code) {
        builder = new StringBuilder(code);
    }

    public void appendCode(String code) {
        builder.append(code);
    }

    public ProgramNode parse() {
        return parse(builder.toString());
    }

    private static ProgramNode parse(String code) {
        List<Node> operations = new ArrayList<>();
        Stack<LoopNode> loops = new Stack<>();
        for (int i = 0; i < code.length(); i++) {
            char operation = code.charAt(i);
            switch (operation) {
                case '>', '<', '+', '-', '.', ',' -> {
                    if (loops.empty()) {
                        operations.add(new CommandNode(operation));
                    } else {
                        loops.peek().addOperation(new CommandNode(operation));
                    }
                }
                case '[' -> {
                    LoopNode loop = new LoopNode();
                    if (loops.empty()) {
                        operations.add(loop);
                    }
                    loops.push(loop);
                }
                case ']' -> {
                    if (loops.empty()) {
                        throw new InvalidSyntaxException("Unmatched closing bracket at position: " + i);
                    }
                    LoopNode loop = loops.pop();
                    if (!loops.empty()) {
                        loops.peek().addOperation(loop);
                    }
                }
                default -> throw new InvalidSyntaxException("Unrecognized operation: " + operation);
            }
        }
        if (!loops.empty()) {
            throw new InvalidSyntaxException("Unmatched opening bracket at an unknown position");
        }
        return new ProgramNode(operations);
    }

}