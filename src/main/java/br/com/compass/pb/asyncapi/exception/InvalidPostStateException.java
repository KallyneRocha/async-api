package br.com.compass.pb.asyncapi.exception;

public class InvalidPostStateException extends RuntimeException {

    public InvalidPostStateException(String message) {
        super(message);
    }
}