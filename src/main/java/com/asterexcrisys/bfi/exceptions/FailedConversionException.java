package com.asterexcrisys.bfi.exceptions;

@SuppressWarnings("unused")
public class FailedConversionException extends RuntimeException {

    public FailedConversionException() {
        super();
    }

    public FailedConversionException(String message) {
        super(message);
    }

}