package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;


    @PostMapping  //создание запроса бронирования
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody BookingDto bookingDto) {
        log.info("создание запроса бронирования");
        return bookingService.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)  //Подтверждение или отклонение запроса на бронирование.
    public BookingDto updateBooking(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                                    @Positive(message = "неверное значение") @PathVariable("id") long id,
                                    @RequestParam() boolean approved) {
        log.info("обновление запроса бронирования");
        return bookingService.updateBooking(userId, id, approved);
    }

    @GetMapping("/{id}")    //получение данных о конкрертном бронирование
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@Positive(message = "неверное значение") @RequestHeader(value = "X-Sharer-User-Id", required = false) long userId,
                                     @Positive(message = "неверное значение") @PathVariable("id") long id) {
        log.info("поиск запроса бронирования id" + id);
        return bookingService.getBookingById(userId, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> findAllBookingByUserId(
            @Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("вывод списка запросов бронирования");
        return bookingService.findAllBookingByUserId(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> findAllBookingByOwner(
            @Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("вывод списка запросов бронирования");
        return bookingService.findAllBookingByOwner(userId, state);
    }

}
