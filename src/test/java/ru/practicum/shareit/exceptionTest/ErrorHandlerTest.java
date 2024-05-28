package ru.practicum.shareit.exceptionTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.InvalidStateException;

import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    @Mock
    private Logger log;

    @Test
    void test_1_validationExceptionHandle_ReturnsBadRequestErrorResponse() {
        ValidationException exception = new ValidationException("validation error");
        ErrorResponse response = errorHandler.validationExceptionHandle(exception);
        assertEquals("validation error", response.getError());
    }

    @Test
    void test_2_alreadyExistExceptionHandler_ReturnsConflictErrorResponse() {
        AlreadyExistsException exception = new AlreadyExistsException("already exists");
        ErrorResponse response = errorHandler.alreadyExistExceptionHandler(exception);
        assertEquals("already exists", response.getError());
    }

    @Test
    void test_3_notFoundExceptionHandle_ReturnsNotFoundErrorResponse() {
        NotFoundException exception = new NotFoundException("not found");
        ErrorResponse response = errorHandler.notFoundExceptionHandle(exception);
        assertEquals("not found", response.getError());
    }

    @Test
    void test_4_throwableExceptionHandle_ReturnsInternalServerErrorErrorResponse() {
        Throwable exception = new RuntimeException("something went wrong");
        ErrorResponse response = errorHandler.throwableExceptionHandle(exception);
        assertEquals("something went wrong", response.getError());
    }

    @Test
    void test_5_invalidStateException_ReturnsInternalServerErrorErrorResponse() {
        InvalidStateException exception = new InvalidStateException("invalid state");
        ErrorResponse response = errorHandler.invalidStateException(exception);
        assertEquals("invalid state", response.getError());
    }

}
