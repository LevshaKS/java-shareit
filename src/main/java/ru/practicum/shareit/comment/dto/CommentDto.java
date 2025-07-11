package ru.practicum.shareit.comment.dto;

import lombok.Data;

import ru.practicum.shareit.item.model.Item;

import java.time.Instant;


@Data
public class CommentDto {
    private Long id;

    private String text;
    private Item item;
    private String authorName;

    private Instant created;
}
