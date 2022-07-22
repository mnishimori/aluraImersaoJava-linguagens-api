package br.com.alura.linguagensapi.exception;

public class EntityEmptyException extends RuntimeException{

    public EntityEmptyException(String message) {
        super(message);
    }
}
