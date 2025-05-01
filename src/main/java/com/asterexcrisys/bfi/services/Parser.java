package com.asterexcrisys.bfi.services;

import com.asterexcrisys.bfi.exceptions.InvalidSyntaxException;
import com.asterexcrisys.bfi.models.dialects.DialectType;
import com.asterexcrisys.bfi.models.nodes.CommandNode;
import com.asterexcrisys.bfi.models.nodes.LoopNode;
import com.asterexcrisys.bfi.models.nodes.Node;
import com.asterexcrisys.bfi.models.nodes.ProgramNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

@SuppressWarnings("unused")
public class Parser {

    private final StringBuilder builder;
    private final DialectType type;

    public Parser() {
        builder = new StringBuilder();
        type = DialectType.BRAINFUCK;
    }

    public Parser(String code) {
        builder = new StringBuilder(code);
        type = DialectType.BRAINFUCK;
    }

    public Parser(DialectType type) {
        builder = new StringBuilder();
        this.type = type;
    }

    public Parser(String code, DialectType type) {
        builder = new StringBuilder(code);
        this.type = Objects.requireNonNull(type);
    }

    public String code() {
        return builder.toString();
    }

    public void appendCode(String code) {
        builder.append(code);
    }

    public DialectType type() {
        return type;
    }

    public ProgramNode parse() {
        String code;
        if (type == DialectType.BRAINFUCK) {
            code = sanitize(builder.toString());
        } else {
            code = type.dialect().convertTo(
                    sanitize(builder.toString()),
                    DialectType.BRAINFUCK
            );
        }
        return parse(code);
    }

    private static String sanitize(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        return code.trim().replaceAll("[\\s\\t\\n]+", "");
    }

    private static ProgramNode parse(String code) {
        if (code == null || code.isEmpty()) {
            return new ProgramNode(0);
        }
        List<Node> operations = new ArrayList<>();
        Stack<LoopNode> loops = new Stack<>();
        boolean isComment = false;
        for (int i = 0; i < code.length(); i++) {
            char operation = code.charAt(i);
            switch (operation) {
                case '/' -> {
                    if (isComment) {
                        throw new InvalidSyntaxException("Nested comment opening slash found at position: " + i);
                    }
                    isComment = true;
                }
                case '\\' -> {
                    if (!isComment) {
                        throw new InvalidSyntaxException("Unmatched comment closing backslash found at position: " + i);
                    }
                    isComment = false;
                }
                case '^', 'v', '=', '>', '<', '+', '-', '*', ':', '%', '#', '.', ',' -> {
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
                        throw new InvalidSyntaxException("Unmatched loop closing bracket at position: " + i);
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
            throw new InvalidSyntaxException("Unmatched loop opening bracket at an unknown position");
        }
        if (isComment) {
            throw new InvalidSyntaxException("Unmatched comment opening slash at an unknown position");
        }
        return new ProgramNode(operations, 1);
    }

}