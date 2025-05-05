package com.asterexcrisys.brainfuck.exceptions;

@SuppressWarnings("unused")
public class FailedConversionException extends RuntimeException {

    public FailedConversionException() {
        super();
    }

    public FailedConversionException(String message) {
        super(message);
    }

}