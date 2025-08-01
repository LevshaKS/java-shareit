package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exception.NotDataException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookingValidateTest {

    private BookItemRequestDto bookingDto;


    @InjectMocks
    private ValidateBookingController validateBookingController;

    @BeforeEach
    void setUp() {

        bookingDto = new BookItemRequestDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now());

    }


    @Test
    void itemDtoNotStart() {
        bookingDto.setStart(null);
        assertThrows(NotDataException.class, () -> validateBookingController.validateBookingDto(bookingDto), "время начало бронирования не может быть пустым");
    }

    @Test
    void itemDtoNotEnd() {
        bookingDto.setEnd(null);
        assertThrows(NotDataException.class, () -> validateBookingController.validateBookingDto(bookingDto), "время начало бронирования не может быть пустым");
    }

}
