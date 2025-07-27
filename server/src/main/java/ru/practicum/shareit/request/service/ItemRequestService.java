package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto saveItemRequest(long userId, ItemRequestDto itemRequestDto);


    ItemRequestDto findIdItemRequestById(long id, long userId);


    Collection<ItemRequestDto> findByUserId(long userId);

    Collection<ItemRequestDto> findByAllNotUserId(long userId);


}
