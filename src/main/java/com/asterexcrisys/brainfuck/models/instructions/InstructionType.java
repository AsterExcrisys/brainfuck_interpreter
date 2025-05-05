package com.asterexcrisys.brainfuck.models.instructions;

@SuppressWarnings("unused")
public enum InstructionType {

    MOVE_START('^', (byte) 0x00, 0, true, false),
    MOVE_END('v', (byte) 0x01, 0, true, false),
    MOVE_MIDDLE('=', (byte) 0x02, 0, true, false),
    MOVE_RIGHT('>', (byte) 0x03, 1, false, true),
    MOVE_LEFT('<', (byte) 0x04, 1, false, true),
    INCREMENT('+', (byte) 0x05, 1, false, true),
    DECREMENT('-', (byte) 0x06, 1, false, true),
    MAXIMIZE('*', (byte) 0x07, 0, true, false),
    MINIMIZE(':', (byte) 0x08, 0, true, false),
    HALVE('%', (byte) 0x09, 0, true, false),
    CLEAR('#', (byte) 0x0A, 0, true, false),
    PRINT_OUTPUT('.', (byte) 0x0B, 1, false, true),
    READ_INPUT(',', (byte) 0x0C, 1, false, true),
    START_LOOP('[', (byte) 0x0D, 2, false, true),
    END_LOOP(']', (byte) 0xFF, 0, false, true),
    OPEN_COMMENT('/', (byte) 0xFF, 0, true, false),
    CLOSE_COMMENT('\\', (byte) 0xFF, 0, true, false);

    private final char command;
    private final byte code;
    private final int count;
    private final boolean isOptional;
    private final boolean isRepeatable;

    InstructionType(char command, byte code, int count, boolean isOptional, boolean isRepeatable) {
        this.command = command;
        this.code = code;
        this.count = count;
        this.isOptional = isOptional;
        this.isRepeatable = isRepeatable;
    }

    public char command() {
        return command;
    }

    public byte code() {
        return code;
    }

    public int count() {
        return count;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public static InstructionType fromCommand(char command) {
        for (InstructionType type : InstructionType.values()) {
            if (type.command == command) {
                return type;
            }
        }
        return null;
    }

    public static InstructionType fromCode(byte code) {
        for (InstructionType type : InstructionType.values()) {
            if (type.code == code && type.code != (byte) 0xFF) {
                return type;
            }
        }
        return null;
    }

}