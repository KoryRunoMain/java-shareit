package ru.practicum.shareit.exception;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(final String message) {
        super(message);
    }
}
