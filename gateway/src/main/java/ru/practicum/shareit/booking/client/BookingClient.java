package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> saveBooking(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> updateBooking(long userId, long bookingId, Boolean approved) {

        return patch("/" + bookingId + "?approved=" + approved, userId);
    }


    public ResponseEntity<Object> findAllBookingByUserId(long userId, String stat) {
        return get("?stat=" + stat, userId);

    }
//

    public ResponseEntity<Object> findAllBookingByOwner(long userId, String stat) {
        return get("/owner?stat=" + stat, userId);
    }


    public ResponseEntity<Object> getBookingById(long userId, long id) {

        return get("/" + id, userId);
    }
}