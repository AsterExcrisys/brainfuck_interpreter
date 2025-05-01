package com.asterexcrisys.bfi.models.instructions;

@SuppressWarnings("unused")
public enum InstructionType {

    MOVE_START('^', false),
    MOVE_END('v', false),
    MOVE_MIDDLE('=', false),
    MOVE_RIGHT('>', true),
    MOVE_LEFT('<', true),
    INCREMENT('+', true),
    DECREMENT('-', true),
    MAXIMIZE('*', false),
    MINIMIZE(':', false),
    HALVE('%', false),
    CLEAR('#', false),
    START_LOOP('[', true),
    END_LOOP(']', true),
    PRINT_CHAR('.', true),
    READ_CHAR(',', true),
    OPEN_COMMENT('/', false),
    CLOSE_COMMENT('\\', false);

    private final char keyword;
    private final boolean isRepeatable;

    InstructionType(char keyword, boolean isRepeatable) {
        this.keyword = keyword;
        this.isRepeatable = isRepeatable;
    }

    public char keyword() {
        return keyword;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

}