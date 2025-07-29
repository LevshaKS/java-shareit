package ru.practicum.shareit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntityNotFoundExceptionTest {

    @Test
    void createAndThrowException (){
        String message = "Test";
        EntityNotFoundException exception = new EntityNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertThrows(EntityNotFoundException.class, () -> {
            throw new EntityNotFoundException(message);
        });

    }
}
