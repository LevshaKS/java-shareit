package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.Collection;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;

    private Boolean available;

    private long owner;

    private long request;

    private Collection<CommentDto> comments;

    private BookingDto lastBooking;

    private BookingDto nextBooking;
}
