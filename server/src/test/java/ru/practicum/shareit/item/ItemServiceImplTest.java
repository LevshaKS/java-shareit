package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ErrorIsNull;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    private ValidateItemController validateItemController;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    User user;
    ItemDto itemDto;
    Item item, itemUpdated;
    long userId, id;

    @BeforeEach
    void setUp() {
        id = 1L;
        userId = 1L;

        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@email.ru");

        item = new Item();
        item.setId(1L);
        item.setDescription("test");
        item.setName("testName");
        item.setAvailable(true);
        item.setUser(user);

        itemDto = new ItemDto();
        itemDto.setDescription("test");
        itemDto.setName("testName");
        itemDto.setOwner(1L);
        itemDto.setAvailable(true);
    }

    @Test
    void saveItem() {
        when(itemRepository.save(any(Item.class))).thenReturn((item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ItemDto result = itemService.saveItem(userId, itemDto);

        assertNotNull(result, "не должен быть пустым");

        assertEquals(item.getId(), result.getId(), "id должно совпадать");
        assertEquals(item.getName(), result.getName(), "имя должно совпадать");
        assertEquals(item.getDescription(), result.getDescription(), "descriptopn должно совпадать");
        assertEquals(item.getAvailable(), result.getAvailable(), "available должно совпадать");
        assertEquals(item.getUser().getId(), result.getOwner(), "id user должно совпадать");

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem() {

        itemUpdated = new Item();
        itemUpdated.setId(1L);
        itemUpdated.setDescription("testupdated");
        itemUpdated.setName("testName");
        itemUpdated.setAvailable(true);
        itemUpdated.setUser(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn((itemUpdated));

        itemDto.setDescription("testupdated");
        ItemDto result = itemService.updateItem(userId, id, itemDto);
        assertNotNull(result, "не должен быть пустым");
        assertEquals(itemUpdated.getDescription(), result.getDescription(), "descriptopn должно совпадать");

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItemIsNotDataException() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ErrorIsNull.class, () -> itemService.updateItem(userId, 2L, itemDto), "должна быть ошибка не найден id");
        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void getItemByItemId() {


        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto result = itemService.getItemByItemId(1L);
        assertNotNull(result, "не должен быть пустым");
        assertEquals(item.getId(), result.getId(), "descriptopn должно совпадать");
        assertEquals(item.getName(), result.getName(), "имя должно совпадать");
        assertEquals(item.getDescription(), result.getDescription(), "descriptopn должно совпадать");
        assertEquals(item.getAvailable(), result.getAvailable(), "available должно совпадать");
        assertEquals(item.getUser().getId(), result.getOwner(), "id user должно совпадать");

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemByItemIdIsNotDataException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ErrorIsNull.class, () -> itemService.getItemByItemId(2L), "должна быть ошибка не найден id");
        verify(itemRepository, times(1)).findById(anyLong());

    }


    @Test
    void getAllItemByUserId() {
        lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(item));

        Collection<ItemDto> result = itemService.getAllItemByUserId(1L);
        assertNotNull(result, "не должен быть пустым");
        itemDto.setId(1L);
        itemDto.setComments(Collections.emptyList());
        assertEquals(itemDto, Arrays.stream(result.toArray()).toList().get(0), "descriptopn должно совпадать");
        verify(itemRepository, times(1)).findByUserId(anyLong());
    }


    @Test
    void findItemByName() {

        when(itemRepository.findByNameContainingIgnoreCaseAndAvailable(anyString(), anyBoolean())).thenReturn(Arrays.asList(item));

        Collection<ItemDto> result = itemService.findItemByName("testName");
        assertNotNull(result, "не должен быть пустым");
        itemDto.setId(1L);
        assertEquals(itemDto, Arrays.stream(result.toArray()).toList().get(0), "descriptopn должно совпадать");
        verify(itemRepository, times(1)).findByNameContainingIgnoreCaseAndAvailable(anyString(), anyBoolean());
    }

    @Test
    void deleteItem() {
        long userId = 1;
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteByUserIdAndId(userId, id);
        itemService.deleteItem(userId, id);
        verify(itemRepository, times(1)).deleteByUserIdAndId(userId, id);
    }

    @Test
    void deleteItemNotFound() {
        long id = 2L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ErrorIsNull.class, () -> {
            itemService.deleteItem(userId, id);
        });
        verify(itemRepository, never()).deleteByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    void addComment() {

        Comment comment = new Comment();
        CommentDto commentDto = new CommentDto();

        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText("test");
        comment.setCreated(Instant.now());

        commentDto.setItem(item);
        commentDto.setText("test");
        commentDto.setAuthorName(user.getName());


        Booking booking = new Booking();
        booking.setItem(item);
        booking.setId(1L);
        booking.setBooker(user);
        booking.setStart(Instant.now());
        booking.setEnd(Instant.now().plusSeconds(60));
        booking.setStatus(Status.APPROVED);

        lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findPastByBooker_idOrderByTimeDesc(anyLong(), any())).thenReturn(Arrays.asList(booking));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.addComment(1L, 1L, commentDto);
        assertNotNull(result);

        verify(commentRepository).save(any(Comment.class));

    }

}
