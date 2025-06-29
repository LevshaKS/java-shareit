package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Validated
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

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
    @Transactional
    public ItemRequestDto updateItemRequest(long userId, long itemId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));

        ItemRequest itemRequest = RequestMapper.mapToRequest(itemRequestDto, user);
        ItemRequest itemRequestOld = RequestMapper.mapToRequest(findIdItemRequestById(itemId), user);

        if (itemRequestOld == null) {
            throw new NotDataException("нет запроса вещи с таким id " + itemId);
        }

        if (itemRequestOld.getUser().getId() != userId) {
            throw new NotDataException("вы не являетесь владельцем запроса вещи");
        }

        if (itemRequest.getDescription() != null) {
            itemRequestOld.setDescription(itemRequest.getDescription());
        }

        itemRequestOld.setCreated(LocalDateTime.now());
        log.info("обновление вещи, передан id " + itemId);
        itemRequestOld = itemRequestRepository.save(itemRequestOld);
        return RequestMapper.mapToRequestDto(itemRequestOld);
    }

    @Override
    public ItemRequestDto findIdItemRequestById(long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() -> new NotDataException("нет запроса с таким id " + id));
        log.info("получение запроса по id");
        return RequestMapper.mapToRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> findItemRequestByDescription(String description) {
        log.info("получение запроса по имени вещи");
        return itemRequestRepository.findByDescriptionContainingIgnoreCase(description).stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> findByUserId(long id) {
        return itemRequestRepository.findByUserId(id).stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(long userId, long id) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));

        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() -> new NotDataException("нет вещи с таким id " + id));

        if (itemRequest.getUser().getId() != userId) {
            throw new ValidationException("вы не являетесь владельцем вещи");
        }
        itemRequestRepository.deleteByUserIdAndId(userId, id);
        log.info("удаление запроса вещи, передан id " + id);

    }
}
