package com.asterexcrisys.bfi.models.nodes;

import com.asterexcrisys.bfi.exceptions.UnknownCommandException;
import com.asterexcrisys.bfi.services.Memory;

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
            default -> throw new UnknownCommandException("Unknown command: " + command);
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