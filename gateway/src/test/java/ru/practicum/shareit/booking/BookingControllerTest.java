package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    BookingClient bookingClient;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    long id;
    long userId;

    BookItemRequestDto bookingDto, bookingDtoRequest;

    LocalDateTime timeStart, timeEnd;


    @BeforeEach
    void setUp() {
        userId = 1L;
        id = 1L;
        timeStart = LocalDateTime.now().plusHours(1);
        timeEnd = LocalDateTime.now().plusHours(2);
        bookingDto = new BookItemRequestDto(null, timeStart, timeEnd);
        bookingDtoRequest = new BookItemRequestDto(id, timeStart, timeEnd);


    }

    @Test
    void createBooking() throws Exception {
        when(bookingClient.bookItem(anyLong(), ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(bookingDtoRequest));

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

       verify(bookingClient, times(1)).bookItem(eq(userId), any());
    }


    @Test
    void getBookingById() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(bookingDtoRequest));

        mvc.perform(get("/bookings/{id}", id)

                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)

                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bookingClient, times(1)).getBooking(eq(userId), eq(id));
    }

}
