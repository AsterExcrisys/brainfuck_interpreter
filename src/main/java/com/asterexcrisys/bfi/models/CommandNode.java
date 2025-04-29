package com.asterexcrisys.bfi.models;

import com.asterexcrisys.bfi.exceptions.UnknownCommandException;
import com.asterexcrisys.bfi.services.Memory;

public record CommandNode(char command) implements Node {

    @Override
    public void execute(Memory memory) {
        switch (command) {
            case '>' -> memory.moveRight();
            case '<' -> memory.moveLeft();
            case '+' -> memory.increment();
            case '-' -> memory.decrement();
            case '.' -> memory.printOutput();
            case ',' -> memory.readInput();
            default -> throw new UnknownCommandException("Unknown command: " + command);
        }
    }

}