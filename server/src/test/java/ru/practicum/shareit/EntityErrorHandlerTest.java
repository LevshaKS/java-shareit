package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = ErrorHandler.class)
public class EntityErrorHandlerTest {

    @Autowired
    private ErrorHandler errorHandler;


    @Test
    void createHandlerIllegalArgumentException() {

        String message = "Test";
        ErrorResponse exception = errorHandler.handlerIllegalArgumentException(new ErrorArgumentException(message));
        assertEquals(message, exception.getDescription());

    }

    @Test
    void createHandlerIsNull() {

        String message = "Test";
        ErrorResponse exception = errorHandler.handlerIsNull(new ErrorIsNull(message));
        assertEquals(message, exception.getDescription());

    }

    @Test
    void createHandlerValidationException() {

        String message = "Test";
        ErrorResponse exception = errorHandler.handlerValidationException(new ValidationException(message));
        assertEquals(message, exception.getDescription());

    }


}
