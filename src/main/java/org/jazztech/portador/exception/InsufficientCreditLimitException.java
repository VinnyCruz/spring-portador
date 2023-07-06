package org.jazztech.portador.exception;

public class InsufficientCreditLimitException extends RuntimeException {
    public InsufficientCreditLimitException(String message) {
        super(message);
    }
}
