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
        builder = new StringBuilder(code.trim());
    }

    public void appendCode(String code) {
        builder.append(code.trim());
    }

    public ProgramNode parse() {
        return parse(builder.toString());
    }

    private static ProgramNode parse(String code) {
        List<Node> operations = new ArrayList<>();
        Stack<LoopNode> loops = new Stack<>();
        boolean isComment = false;
        for (int i = 0; i < code.length(); i++) {
            char operation = code.charAt(i);
            switch (operation) {
                case '(' -> isComment = true;
                case ')' -> {
                    if (!isComment) {
                        throw new InvalidSyntaxException("Unmatched closing bracket (comment) at position: " + i);
                    }
                    isComment = false;
                }
                case '>', '<', '+', '-', '.', ',' -> {
                    if (isComment) {
                        break;
                    }
                    CommandNode command = new CommandNode(operation, 1);
                    if (loops.empty()) {
                        operations.add(command);
                    } else {
                        loops.peek().addOperation(command);
                    }
                }
                case '[' -> {
                    if (isComment) {
                        break;
                    }
                    LoopNode loop = new LoopNode(1);
                    if (loops.empty()) {
                        operations.add(loop);
                    }
                    loops.push(loop);
                }
                case ']' -> {
                    if (isComment) {
                        break;
                    }
                    if (loops.empty()) {
                        throw new InvalidSyntaxException("Unmatched closing bracket (loop) at position: " + i);
                    }
                    LoopNode loop = loops.pop();
                    if (!loops.empty()) {
                        loops.peek().addOperation(loop);
                    }
                }
                default -> {
                    if (isComment) {
                        break;
                    }
                    throw new InvalidSyntaxException("Unrecognized operation '" + operation + "' at position: " + i);
                }
            }
        }
        if (!loops.empty()) {
            throw new InvalidSyntaxException("Unmatched opening bracket (loop) at an unknown position");
        }
        if (isComment) {
            throw new InvalidSyntaxException("Unmatched opening bracket (comment) at an unknown position");
        }
        return new ProgramNode(operations);
    }

}