package com.asterexcrisys.bfi.models.nodes;

import com.asterexcrisys.bfi.exceptions.InvalidSyntaxException;
import com.asterexcrisys.bfi.services.Memory;
import java.util.List;

@SuppressWarnings("unused")
public class CommandNode implements Node {

    private final char command;
    private int count;

    public CommandNode() {
        command = '\0';
        count = 0;
    }

    public CommandNode(char command) {
        this.command = command;
        count = 1;
    }

    public CommandNode(int count) {
        command = '\0';
        this.count = count;
    }

    public CommandNode(char command, int count) {
        this.command = command;
        this.count = count;
    }

    public char command() {
        return command;
    }

    public int count() {
        return count;
    }

    public void count(int count) {
        this.count = count;
    }

    public void execute(Memory memory) {
        switch (command) {
            case '^' -> memory.moveStart();
            case 'v' -> memory.moveEnd();
            case '=' -> memory.moveMiddle();
            case '>' -> memory.moveRight(count);
            case '<' -> memory.moveLeft(count);
            case '+' -> memory.increment(count);
            case '-' -> memory.decrement(count);
            case '*' -> memory.maximize();
            case ':' -> memory.minimize();
            case '%' -> memory.halve();
            case '#' -> memory.clear();
            case '.' -> memory.printOutput(count);
            case ',' -> memory.readInput(count);
            default -> throw new InvalidSyntaxException("Unknown command: " + command);
        }
    }

    public void translate(List<Byte> bytecode) {
        switch (command) {
            case '^' -> bytecode.add((byte) 0x00);
            case 'v' -> bytecode.add((byte) 0x01);
            case '=' -> bytecode.add((byte) 0x02);
            case '>' -> {
                for (int i = 0; i < count; i++) {
                    bytecode.add((byte) 0x03);
                }
            }
            case '<' -> {
                for (int i = 0; i < count; i++) {
                    bytecode.add((byte) 0x04);
                }
            }
            case '+' -> {
                for (int i = 0; i < count; i++) {
                    bytecode.add((byte) 0x05);
                }
            }
            case '-' -> {
                for (int i = 0; i < count; i++) {
                    bytecode.add((byte) 0x06);
                }
            }
            case '*' -> bytecode.add((byte) 0x07);
            case ':' -> bytecode.add((byte) 0x08);
            case '%' -> bytecode.add((byte) 0x09);
            case '#' -> bytecode.add((byte) 0x0A);
            case '.' -> {
                for (int i = 0; i < count; i++) {
                    bytecode.add((byte) 0x0B);
                }
            }
            case ',' -> {
                for (int i = 0; i < count; i++) {
                    bytecode.add((byte) 0x0C);
                }
            }
            default -> throw new InvalidSyntaxException("Unknown command: " + command);
        }
    }

    public Node partialCopy() {
        return new CommandNode(count);
    }

    public Node fullCopy() {
        return new CommandNode(command, count);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CommandNode node)) {
            return false;
        }
        return node.command == command && node.count == count;
    }

}