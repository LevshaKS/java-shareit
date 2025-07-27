package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto saveItemRequest(long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));

        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty() || itemRequestDto.getDescription().isBlank()) {
            throw new NotDataException("description не может быть пустым");
        }
        ItemRequest itemRequest = RequestMapper.mapToRequest(itemRequestDto, user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        log.info("создан запрос id " + itemRequest.getId());
        return RequestMapper.mapToRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto findIdItemRequestById(long id, long userId) {
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() -> new NotDataException("нет запроса с таким id " + id));
        log.info("получение запроса по id запроса " + id);
        Collection<Item> items = itemRepository.findByRequest_id(id);
        Set<ItemDto> itemsDto = items.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toSet());
        ItemRequestDto itemRequestDto = RequestMapper.mapToRequestDto(itemRequest);
        itemRequestDto.setItems(itemsDto);

        return itemRequestDto;
    }

    @Override
    public Collection<ItemRequestDto> findByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("пользователь не найден"));
        log.info("получение запроса  по  id пользователя " + userId);
        return itemRequestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> findByAllNotUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("пользователь не найден"));
        log.info("получение всех запросов кропе пользователя  по id " + userId);
        return itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId)
                .stream().map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

}

