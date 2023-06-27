package org.jazztech.portador.exception;

public class CardHolderAlreadyExistException extends RuntimeException {
    public CardHolderAlreadyExistException(String message) {
        super(message);
    }
}
