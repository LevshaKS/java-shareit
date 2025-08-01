package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.ValidateBookingController;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private final ValidateBookingController validateBookingController;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Positive(message = "неверное значение")
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @Valid @RequestBody BookItemRequestDto requestDto) {
        validateBookingController.validateBookingDto(requestDto);
        log.info("создание запроса бронирования");
        return bookingClient.saveBooking(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateBooking(@Positive(message = "неверное значение")
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @Positive(message = "неверное значение") @PathVariable("id") long id,
                                                @RequestParam() boolean approved) {
        log.info("обновление запроса бронирования");
        return bookingClient.updateBooking(userId, id, approved);
    }

    @GetMapping("/{id}")    //получение данных о конкрертном бронирование
    public ResponseEntity<Object> getBookingById(@Positive(message = "неверное значение") @RequestHeader(value = "X-Sharer-User-Id", required = false) long userId,
                                                 @Positive(message = "неверное значение") @PathVariable("id") long id) {
        log.info("поиск запроса бронирования id" + id);
        return bookingClient.getBookingById(userId, id);
    }

    @GetMapping

    public ResponseEntity<Object> findAllBookingByUserId(
            @Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("вывод списка запросов бронирования");
        return bookingClient.findAllBookingByUserId(userId, state);
    }

    @GetMapping("/owner")

    public ResponseEntity<Object> findAllBookingByOwner(
            @Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("вывод списка запросов бронирования");
        return bookingClient.findAllBookingByOwner(userId, state);
    }
}
