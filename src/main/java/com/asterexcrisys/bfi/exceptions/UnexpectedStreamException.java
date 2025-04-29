package com.asterexcrisys.bfi.exceptions;

@SuppressWarnings("unused")
public class UnexpectedStreamException extends RuntimeException {

    public UnexpectedStreamException() {
        super();
    }

    public UnexpectedStreamException(String message) {
        super(message);
    }

}