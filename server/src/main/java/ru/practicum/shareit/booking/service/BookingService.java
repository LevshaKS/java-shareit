package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    BookingDto saveBooking(long userId, BookingDto bookingDto);

    BookingDto updateBooking(long userId, long itemId, Boolean approved);

    Collection<BookingDto> findAllBookingByUserId(long userId, String stat);

    Collection<BookingDto> findAllBookingByOwner(long userId, String stat);

    BookingDto getBookingById(long userId, long id);

}
