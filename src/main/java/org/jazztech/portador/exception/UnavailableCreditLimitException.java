package org.jazztech.portador.exception;

public class UnavailableCreditLimitException extends RuntimeException {
    public UnavailableCreditLimitException(String message) {
        super(message);
    }
}
