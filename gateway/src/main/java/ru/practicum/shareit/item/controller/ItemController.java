package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


@Validated
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("запрос создания вещи");
        System.out.println(itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Positive(message = "неверное значение") @PathVariable("itemId") long itemId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("запрос обновление вещи");
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void delItem(@Positive(message = "неверное значение") @RequestHeader("X-Sharer-User-Id") long userId,
                        @Positive(message = "неверное значение") @PathVariable("itemId") long itemId) {
        log.info("запрос удаления вещи");
        itemClient.delItem(userId, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@Positive(message = "неверное значение") @PathVariable("itemId") long itemId) {
        log.info("запрос вещи по id " + itemId);
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemByUserID(@Positive(message = "неверное значение")
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("запрос вещей у пользователя id " + userId);

        return itemClient.getItemByUserID(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItemByName(@RequestParam String text) {
        log.info("запрос поиска вещи name " + text);
        return itemClient.searchItemByName(text);

    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createComment(@PathVariable("id") long id,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("добавление комментария ");
        return itemClient.createComment(id, userId, commentDto);
    }

}
