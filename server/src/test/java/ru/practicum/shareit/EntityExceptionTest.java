package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntityExceptionTest {

    @Test
    void createAndThrowDuplicateDataException() {
        String message = "Test";
        DuplicateDataException exception = new DuplicateDataException(message);

        assertEquals(message, exception.getMessage());
        assertThrows(DuplicateDataException.class, () -> {
            throw new DuplicateDataException(message);
        });
    }

    @Test
    void createAndThrowErrorArgumentException() {
        String message = "Test";
        ErrorArgumentException exception = new ErrorArgumentException(message);

        assertEquals(message, exception.getMessage());
        assertThrows(ErrorArgumentException.class, () -> {
            throw new ErrorArgumentException(message);
        });
    }

    @Test
    void createErrorResponse() {
        String message = "Test";
        String error = "error";
        ErrorResponse exception = new ErrorResponse(error, message);

        assertEquals(message, exception.getDescription());
    }

    @Test
    void createAndThrowNotDataException() {
        String message = "Test";
        NotDataException exception = new NotDataException(message);

        assertEquals(message, exception.getMessage());
        assertThrows(NotDataException.class, () -> {
            throw new NotDataException(message);
        });
    }

    @Test
    void createAndThrowValidationException() {
        String message = "Test";
        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
        assertThrows(ValidationException.class, () -> {
            throw new ValidationException(message);
        });
    }

}
