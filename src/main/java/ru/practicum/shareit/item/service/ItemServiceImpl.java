package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ErrorIsNull;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.ValidateItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Validated
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;


    private final ValidateItemController validateItemController;

    @Override
    @Transactional
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        validateItemController.validateItemDto(itemDto);

        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorIsNull("нет такого пользователя"));

        Item item = ItemMapper.mapToItem(itemDto, user);
        item = itemRepository.save(item);
        log.info("передана вещь для создания");
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long id) {
        if (getItemByItemId(id) == null) {
            throw new NotDataException("нет вещи с таким id " + id);
        }
        if (getItemByItemId(id).getOwner() != userId) {
            throw new ValidationException("вы не являетесь владельцем вещи");
        }
        itemRepository.deleteByUserIdAndId(userId, id);
        log.info("удаление вещи, передан id " + id);
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorIsNull("нет такого пользователя"));

        Item item = ItemMapper.mapToItem(itemDto, user);
        Item itemOld = ItemMapper.mapToItem(getItemByItemId(itemId), user);

        if (itemOld == null) {
            throw new NotDataException("нет вещи с таким id " + itemId);
        }

        if (itemOld.getUser().getId() != userId) {
            throw new ErrorIsNull("вы не являетесь владельцем вещи");
        }

        if (item.getName() != null) {
            itemOld.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemOld.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemOld.setAvailable(item.getAvailable());
        }

        log.info("обновление вещи, передан id " + itemOld.getId());
        itemRepository.save(itemOld);
        return ItemMapper.mapToItemDto(itemOld);
    }

    @Override
    public ItemDto getItemByItemId(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ErrorIsNull("нет такой вещи"));

        log.info("передан запрос вещи по id " + id);

        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        setBookingDataPastAndFuture(List.of(itemDto));
        addCommentsForItems(List.of(itemDto));

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAllItemByUserId(long id) {
        log.info("передан запрос списка вещей пользователя id " + id);

        Collection<ItemDto> itemDto = itemRepository.findByUserId(id).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());

        setBookingDataPastAndFuture(itemDto);
        addCommentsForItems(itemDto);

        return itemDto;
    }

    @Override
    public Collection<ItemDto> findItemByName(String name) {
        System.out.println(name);
        if (name.isEmpty() || name.isBlank()) {
            log.warn("передан пустой запрос поиска");
            return new ArrayList<ItemDto>();
        }
        log.info("передан запрос поиска вещей по названию " + name.toLowerCase());
        return itemRepository.findByNameContainingIgnoreCaseAndAvailable(name, true).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(long id, long userId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorIsNull("нет такого пользователя"));
        Item item = itemRepository.findById(id).orElseThrow(() -> new ErrorIsNull("нет такой вещи"));

        Collection<Booking> bookingsPastInUser = bookingRepository.findPastByBooker_idOrderByTimeDesc(userId,
                LocalDateTime.now().toInstant(ZoneOffset.UTC));

        Collection<Booking> itemBooking = bookingsPastInUser.stream()
                .filter(booking -> booking.getItem().getId() == id).collect(Collectors.toSet());


        if (itemBooking.isEmpty()) {
            throw new NotDataException("вы не брали эту вещь в аренду");
        }
        Comment comment = CommentMapper.mapToComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(Instant.now());
        comment = commentRepository.save(comment);
        log.info("добавлен комментарий");
        return CommentMapper.mapToCommentDto(comment);
    }


    private void addCommentsForItems(Collection<ItemDto> itemsDto) {

        Collection<Long> itemsId = itemsDto.stream()
                .map(ItemDto::getId)
                .toList();

        Collection<Comment> comments = commentRepository.findAllByItemId(itemsId);
        for (ItemDto item : itemsDto) {
            item.setComments(
                    comments.stream()
                            .filter(comment -> comment.getItem().getId().equals(item.getId()))
                            .map(CommentMapper::mapToCommentDto)
                            .toList()
            );
            log.info("добавили в вещь комментарий");
        }

    }

    private void setBookingDataPastAndFuture(Collection<ItemDto> itemsDto) {
        Instant timeNow = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Collection<Long> findItems = itemsDto.stream()
                .map(ItemDto::getId).toList();  //список вещей вернушвиеся в запросе
        Collection<Booking> futureBookings = bookingRepository.findFutureByItem_idTimeDesc(findItems,
                timeNow);
        Collection<Booking> pastBookings = bookingRepository.findPastByItem_idTimeDesc(findItems,
                timeNow.plusSeconds(4)); // не знаю как решить проблему. при тестирование разница в пару секунд промежутка бронирования и запроса
        for (ItemDto itemDto : itemsDto) {
            Optional<Booking> pastBooking = pastBookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(itemDto.getId()))
                    .findFirst();

            Optional<Booking> futureBooking = futureBookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(itemDto.getId()))
                    .findFirst();

            if (!pastBooking.isEmpty()) {
                itemDto.setLastBooking(BookingMapper.mapToBookingDto(pastBooking.get()));

            }
            if (!futureBooking.isEmpty()) {

                itemDto.setNextBooking(BookingMapper.mapToBookingDto(futureBooking.get()));
            }

        }
        log.info("добавляем последнее и ближайшее бронирование");
    }

}
