package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exception.NotDataException;

@Slf4j
@Component
public class ValidateBookingController {

    public void validateBookingDto(BookItemRequestDto bookingDto) {
        if (bookingDto.getItemId() == null) {
            log.warn("item не может быть пустым");
            throw new NotDataException("item не может быть пустым");
        }

        if (bookingDto.getStart() == null) {
            log.warn("время начало бронирования не может быть пустым");
            throw new NotDataException("время начало бронирования не может быть пустым");
        }
        if (bookingDto.getEnd() == null) {
            log.warn("время начало бронирования не может быть пустым");
            throw new NotDataException("время начало бронирования не может быть пустым");
        }
    }
}
