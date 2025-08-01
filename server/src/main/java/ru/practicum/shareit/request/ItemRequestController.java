package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("создание запроса создания вещи");
        return itemRequestService.saveItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto findIdItemRequestById(@Positive(message = "неверное значение") @PathVariable("id") long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("вывод запроса создания вещи id " + id);
        return itemRequestService.findIdItemRequestById(id, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> findByUser(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long id) {
        log.info("вывод всех запросов создания вещи пользователем");
        return itemRequestService.findByUserId(id);
    }

    @GetMapping("/ALL")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> findByAlLUser(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("вывод всех запросов создания вещи пользователем");
        return itemRequestService.findByAllNotUserId(userId);
    }

}
