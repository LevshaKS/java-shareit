package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static Booking mapToBooking(BookingDto bookingDto, Item item) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId() != null ? bookingDto.getId() : null);
        booking.setStart(bookingDto.getStart().toInstant(ZoneOffset.UTC));
        booking.setEnd(bookingDto.getEnd().toInstant(ZoneOffset.UTC));
        booking.setItem(item);
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId() != null ? booking.getId() : null);
        bookingDto.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneOffset.UTC));
        bookingDto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC));
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setItem(booking.getItem());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItemId(booking.getItem().getId());
        return bookingDto;
    }
}
