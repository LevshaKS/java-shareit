package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    BookingDto bookingDto;
    Booking booking;
    long id, userId;

    User user, user2;
    Item item, item2;

    @BeforeEach
    void setUp() {
        id = 1L;
        userId = 1L;
        Instant timeNow = Instant.now();
        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@email.ru");

        item = new Item();
        item.setId(1L);
        item.setDescription("test");
        item.setName("testName");
        item.setAvailable(true);
        item.setUser(user);

        bookingDto = new BookingDto();
        bookingDto.setBooker(user);
        bookingDto.setItem(item);
        bookingDto.setStart(LocalDateTime.ofInstant(timeNow, ZoneOffset.UTC));
        bookingDto.setEnd(LocalDateTime.ofInstant(timeNow, ZoneOffset.UTC).plusMinutes(1));
        bookingDto.setStatus(Status.WAITING);
        bookingDto.setItemId(item.getId());

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(timeNow);
        booking.setEnd(timeNow.plusSeconds(60));
        booking.setStatus(Status.WAITING);

        item2 = new Item();
        item2.setId(2L);
        item2.setDescription("test2");
        item2.setName("testName2");
        item2.setAvailable(true);
        item2.setUser(user2);

        user2 = new User();
        user2.setId(2L);
        user2.setName("test2");
        user2.setEmail("test2@email.ru");

    }

    @Test
    void saveBooking() {
        lenient().when(itemRepository.findById(anyLong())).thenReturn((Optional.of(item)));
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn((booking));

        BookingDto result = bookingService.saveBooking(user2.getId(), bookingDto);
        assertNotNull(result, "не должен быть пустым");

        assertEquals(booking.getId(), result.getId(), "id должно совпадать");
        assertEquals(booking.getBooker(), result.getBooker(), "имя должно совпадать");
        assertEquals(booking.getItem(), result.getItem(), "descriptopn должно совпадать");
        assertEquals(booking.getStatus(), result.getStatus(), "available должно совпадать");


        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking() {
        lenient().when(itemRepository.findById(anyLong())).thenReturn((Optional.of(item)));
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn((Optional.of(booking)));
        when(bookingRepository.save(any(Booking.class))).thenReturn((booking));

        BookingDto result = bookingService.updateBooking(user.getId(), id, true);
        assertNotNull(result, "не должен быть пустым");

        assertEquals(booking.getId(), result.getId(), "id должно совпадать");
        assertEquals(booking.getBooker(), result.getBooker(), "имя должно совпадать");
        assertEquals(booking.getItem(), result.getItem(), "descriptopn должно совпадать");
        assertEquals(booking.getStatus(), result.getStatus(), "available должно совпадать");


        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getBookingById() {
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn((Optional.of(booking)));

        BookingDto result = bookingService.getBookingById(user.getId(), id);

        assertNotNull(result, "не должен быть пустым");
        assertEquals(booking.getId(), result.getId(), "id должно совпадать");
        assertEquals(booking.getBooker(), result.getBooker(), "имя должно совпадать");
        assertEquals(booking.getItem(), result.getItem(), "descriptopn должно совпадать");
        assertEquals(booking.getStatus(), result.getStatus(), "available должно совпадать");

        verify(bookingRepository).findById(id);
    }

    @Test
    void getBookingByIdIsNotDataException() {
        when(bookingRepository.findById(anyLong())).thenReturn((Optional.empty()));

        assertThrows(NotDataException.class, () -> bookingService.getBookingById(user.getId(), id), "должна быть ошибка не найден id");

        verify(bookingRepository, times(1)).findById(id);
    }

    @Test
    void findAllBookingByUserId() {
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong())).thenReturn(Arrays.asList(booking));


        Collection<BookingDto> result = bookingService.findAllBookingByUserId(user.getId(), "ALL");
        bookingDto.setId(1L);

        assertNotNull(result, "не должен быть пустым");
        assertEquals(bookingDto, Arrays.stream(result.toArray()).toList().get(0), " должно совпадать");

        verify(bookingRepository, times(1)).findByBookerIdOrderByStartDesc(id);
    }

    @Test
    void findAllBookingByOwner() {
        lenient().when(itemRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(item));
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findDistinctByItemIdInOrderByStartDesc(anyCollection())).thenReturn(Arrays.asList(booking));

        Collection<BookingDto> result = bookingService.findAllBookingByOwner(user.getId(), "ALL");
        bookingDto.setId(1L);

        assertNotNull(result, "не должен быть пустым");
        assertEquals(bookingDto, Arrays.stream(result.toArray()).toList().get(0), " должно совпадать");

        verify(bookingRepository, times(1)).findDistinctByItemIdInOrderByStartDesc(anyCollection());
    }


}
