package io.github.gabrielnavas.book_network_api.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException() {
    }

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
