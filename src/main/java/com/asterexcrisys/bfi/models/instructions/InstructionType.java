package com.asterexcrisys.bfi.models.instructions;

@SuppressWarnings("unused")
public enum InstructionType {

    MOVE_START('^', (byte) 0x00, true, false),
    MOVE_END('v', (byte) 0x01, true, false),
    MOVE_MIDDLE('=', (byte) 0x02, true, false),
    MOVE_RIGHT('>', (byte) 0x03, false, true),
    MOVE_LEFT('<', (byte) 0x04, false, true),
    INCREMENT('+', (byte) 0x05, false, true),
    DECREMENT('-', (byte) 0x06, false, true),
    MAXIMIZE('*', (byte) 0x07, true, false),
    MINIMIZE(':', (byte) 0x08, true, false),
    HALVE('%', (byte) 0x09, true, false),
    CLEAR('#', (byte) 0x0A, true, false),
    PRINT_CHAR('.', (byte) 0x0B, false, true),
    READ_CHAR(',', (byte) 0x0C, false, true),
    START_LOOP('[', (byte) 0x0D, false, true),
    END_LOOP(']', (byte) 0x0E, false, true),
    OPEN_COMMENT('/', (byte) 0x0F, true, false),
    CLOSE_COMMENT('\\', (byte) 0x0F, true, false);

    private final char keyword;
    private final byte code;
    private final boolean isOptional;
    private final boolean isRepeatable;

    InstructionType(char keyword, byte code, boolean isOptional, boolean isRepeatable) {
        this.keyword = keyword;
        this.code = code;
        this.isOptional = isOptional;
        this.isRepeatable = isRepeatable;
    }

    public char keyword() {
        return keyword;
    }

    public byte code() {
        return code;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

}