package ru.practicum.shareit.exceptionTest;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;

public class NotFoundExceptionTest {

    @Test
    void setNotFound() {
        NotFoundException exception = new NotFoundException("message");
    }

}
