package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ItemRequest mapToRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId() != null ? itemRequestDto.getId() : null);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(user);
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestDto mapToRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId() != null ? itemRequest.getId() : null);
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequester(itemRequest.getRequester().getId());
        itemRequestDto.setCreated(itemRequest.getCreated());

        return itemRequestDto;
    }

}
