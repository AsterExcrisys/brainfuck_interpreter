package com.asterexcrisys.brainfuck.models.nodes;

import com.asterexcrisys.brainfuck.exceptions.InvalidSyntaxException;
import com.asterexcrisys.brainfuck.models.instructions.InstructionType;
import com.asterexcrisys.brainfuck.services.Memory;
import java.util.List;

@SuppressWarnings("unused")
public class CommandNode implements Node {

    private final InstructionType type;
    private int count;

    public CommandNode() {
        type = null;
        count = 0;
    }

    public CommandNode(InstructionType type) {
        this.type = type;
        count = 1;
    }

    public CommandNode(int count) {
        type = null;
        this.count = count;
    }

    public CommandNode(InstructionType type, int count) {
        this.type = type;
        this.count = count;
    }

    public InstructionType type() {
        return type;
    }

    public int count() {
        return count;
    }

    public void count(int count) {
        this.count = count;
    }

    public void execute(Memory memory) {
        if (type == null) {
            throw new InvalidSyntaxException("Missing valid type for command node");
        }
        switch (type) {
            case InstructionType.MOVE_START -> memory.moveStart();
            case InstructionType.MOVE_END -> memory.moveEnd();
            case InstructionType.MOVE_MIDDLE -> memory.moveMiddle();
            case InstructionType.MOVE_RIGHT -> memory.moveRight(count);
            case InstructionType.MOVE_LEFT -> memory.moveLeft(count);
            case InstructionType.INCREMENT -> memory.increment(count);
            case InstructionType.DECREMENT -> memory.decrement(count);
            case InstructionType.MAXIMIZE -> memory.maximize();
            case InstructionType.MINIMIZE -> memory.minimize();
            case InstructionType.HALVE -> memory.halve();
            case InstructionType.CLEAR -> memory.clear();
            case InstructionType.PRINT_OUTPUT -> memory.printOutput(count);
            case InstructionType.READ_INPUT -> memory.readInput(count);
            default -> throw new InvalidSyntaxException("Unknown command: " + type.command());
        }
    }

    public int translate(List<Byte> bytecode) {
        if (type == null) {
            throw new InvalidSyntaxException("Missing valid type for command node");
        }
        switch (type) {
            case InstructionType.MOVE_START -> bytecode.add((byte) 0x00);
            case InstructionType.MOVE_END -> bytecode.add((byte) 0x01);
            case InstructionType.MOVE_MIDDLE -> bytecode.add((byte) 0x02);
            case InstructionType.MOVE_RIGHT, InstructionType.MOVE_LEFT, InstructionType.INCREMENT, InstructionType.DECREMENT, InstructionType.PRINT_OUTPUT, InstructionType.READ_INPUT -> {
                bytecode.add(type.code());
                bytecode.add((byte) (count & 0xFF));
            }
            case InstructionType.MAXIMIZE -> bytecode.add((byte) 0x07);
            case InstructionType.MINIMIZE -> bytecode.add((byte) 0x08);
            case InstructionType.HALVE -> bytecode.add((byte) 0x09);
            case InstructionType.CLEAR -> bytecode.add((byte) 0x0A);
            default -> throw new InvalidSyntaxException("Unknown command: " + type.command());
        }
        return 1;
    }

    public Node partialCopy() {
        return new CommandNode(count);
    }

    public Node fullCopy() {
        return new CommandNode(type, count);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CommandNode node)) {
            return false;
        }
        return node.type == type && node.count == count;
    }

}