package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ErrorIsNull;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto saveBooking(long userId, BookingDto bookingDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorIsNull("нет такого пользователя"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ErrorIsNull("нет такой вещи"));

        if (item.getUser().getId() == userId) {
            log.warn("нельзя бронировать свою вещь");
            throw new NotDataException("нельзя бронировать свою вещь");
        }
        if (!item.getAvailable()) {
            log.warn("нельзя бронировать");
            throw new NotDataException("нельзя бронировать");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().equals(bookingDto.getStart())) {
            log.warn("даты бронирования не верные");
            throw new NotDataException("даты бронирования не верные");
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto, item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        booking = bookingRepository.save(booking);
        log.info("сохранение запроса бронирование id " + booking.getId());
        return BookingMapper.mapToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto updateBooking(long userId, long itemId, Boolean approved) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));
        Booking booking = bookingRepository.findById(itemId).orElseThrow(() -> new ErrorIsNull("нет такого запроса"));

        if (booking.getItem().getUser().getId() != userId) {
            log.warn("вы не владелиц вещи");
            throw new NotDataException("вы не владелиц вещи");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.info("обновили запрос");
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> findAllBookingByUserId(long userId, String stat) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));
        BookingDtoStatus status = BookingDtoStatus.valueOf(stat);
        switch (status) {
            case WAITING: {
                log.info("ожидает подтверждения список бронирования пользвателя");
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case REJECTED: {
                log.info("отклоненный список бронирования пользвателя");
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case CURRENT: {
                log.info("в данном моменте список бронирования пользвателя");
                return bookingRepository.findCurrentByBooker_idOrderByTimeDesc(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC), LocalDateTime.now().toInstant(ZoneOffset.UTC)).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case PAST: {
                log.info("завершенный список бронирования пользвателя");
                return bookingRepository.findPastByBooker_idOrderByTimeDesc(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case FUTURE: {
                log.info("в будущем список бронирования пользвателя");
                return bookingRepository.findFutureByBooker_idOrderByTimeDesc(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case ALL: {
                log.info("весь список бронирования пользвателя");
                return bookingRepository.findByBookerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            default:
                throw new ErrorIsNull(stat + " - статус не известен");
        }
    }

    @Override
    public Collection<BookingDto> findAllBookingByOwner(long userId, String stat) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));

        Collection<Long> usersItems = itemRepository.findByUserId(userId).stream().map(Item::getId).collect(Collectors.toList());
        if (usersItems.isEmpty()) {
            return new ArrayList<>();
        }
        BookingDtoStatus status = BookingDtoStatus.valueOf(stat);
        switch (status) {
            case WAITING: {
                log.info("весь список вещей пользователя ожидают подтверждение бронирования");
                return bookingRepository.findDistinctByItem_idInAndStatusOrderByStart_dateDesc(usersItems, Status.WAITING).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case REJECTED: {
                log.info("весь список вещеь пользователя откланеное бронирование");
                return bookingRepository.findDistinctByItem_idInAndStatusOrderByStart_dateDesc(usersItems, Status.REJECTED).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case CURRENT: {
                log.info("весь список вещей пользователя сейчас в пользвоании");
                return bookingRepository.findCurrentByItem_idTimeDesc(usersItems, LocalDateTime.now().toInstant(ZoneOffset.UTC), LocalDateTime.now().toInstant(ZoneOffset.UTC)).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case PAST: {
                log.info("весь список вещей пользователя в прошедшем бронировали");
                return bookingRepository.findPastByItem_idTimeDesc(usersItems, LocalDateTime.now().toInstant(ZoneOffset.UTC)).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case FUTURE: {
                log.info("весь список вещей пользователя в будущем забронированы");
                return bookingRepository.findFutureByItem_idTimeDesc(usersItems, LocalDateTime.now().toInstant(ZoneOffset.UTC)).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            case ALL: {
                log.info("весь список вещей пользователя в бронировании");
                return bookingRepository.findDistinctByItemIdInOrderByStartDesc(usersItems).stream()
                        .map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
            default:
                throw new ErrorIsNull(stat + " - статус не известен");
        }
    }

    @Override
    public BookingDto getBookingById(long userId, long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotDataException("нет такого запроса"));
        if (booking.getBooker().getId() == userId || booking.getItem().getUser().getId() == userId) {
            log.info("возвращаем запрос по id");
            return BookingMapper.mapToBookingDto(booking);
        }
        log.warn("вы не собственник запроса и не владелиц вещи");
        throw new NotDataException("вы не собственник запроса и не владелиц вещи");
    }
}
