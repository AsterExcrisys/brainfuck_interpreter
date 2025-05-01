package com.asterexcrisys.bfi.exceptions;

@SuppressWarnings("unused")
public class NotExpectedStreamException extends RuntimeException {

    public NotExpectedStreamException() {
        super();
    }

    public NotExpectedStreamException(String message) {
        super(message);
    }

}