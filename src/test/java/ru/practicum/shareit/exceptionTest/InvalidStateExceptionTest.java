package ru.practicum.shareit.exceptionTest;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.InvalidStateException;

public class InvalidStateExceptionTest {

    @Test
    void setInvalidState() {
        InvalidStateException exception = new InvalidStateException("message");
    }

}
