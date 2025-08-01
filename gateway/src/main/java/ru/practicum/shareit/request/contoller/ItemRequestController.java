package ru.practicum.shareit.request.contoller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Validated
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                                                    @Valid @RequestBody ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty() || itemRequestDto.getDescription().isBlank()) {
            throw new NotDataException("description не может быть пустым");
        }
        log.info("создание запроса создания вещи");
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findIdItemRequestById(@Positive(message = "неверное значение") @PathVariable("id") long id,
                                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("вывод запроса создания вещи id " + id);
        return itemRequestClient.findIdItemRequestById(id, userId);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findByUser(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("вывод всех запросов создания вещи пользователем");
        return itemRequestClient.findByUser(id);
    }

    @GetMapping("/ALL")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findByAlLUser(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("вывод всех запросов создания вещи пользователем");
        return itemRequestClient.findByAlLUser(userId);
    }
}
