package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto saveItemRequest(long userId, ItemRequestDto itemRequestDto);

    // ItemRequestDto updateItemRequest(long userId, long itemId, ItemRequestDto itemRequestDto);

    ItemRequestDto findIdItemRequestById(long id, long userId);

    //  Collection<ItemRequestDto> findItemRequestByDescription(String description);

    Collection<ItemRequestDto> findByUserId(long userId);

    Collection<ItemRequestDto> findByAllNotUserId (long userId);

//    void delete(long userId, long id);
}
