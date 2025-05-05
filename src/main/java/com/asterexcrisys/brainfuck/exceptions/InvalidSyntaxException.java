package com.asterexcrisys.brainfuck.exceptions;

@SuppressWarnings("unused")
public class InvalidSyntaxException extends RuntimeException {

    public InvalidSyntaxException() {
        super();
    }

    public InvalidSyntaxException(String message) {
        super(message);
    }

}