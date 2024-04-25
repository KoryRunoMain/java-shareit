package ru.practicum.shareit.exception;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(final String message) {
        super(message);
    }
}
