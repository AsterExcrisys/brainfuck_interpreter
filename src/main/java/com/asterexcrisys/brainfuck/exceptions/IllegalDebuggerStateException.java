package com.asterexcrisys.brainfuck.exceptions;

@SuppressWarnings("unused")
public class IllegalDebuggerStateException extends RuntimeException {

    public IllegalDebuggerStateException() {
        super();
    }

    public IllegalDebuggerStateException(String message) {
        super(message);
    }

}
