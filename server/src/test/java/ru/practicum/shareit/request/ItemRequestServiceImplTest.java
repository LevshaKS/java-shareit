package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;


    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    User user;
    Item item;
    long id;
    ItemRequest itemRequest;

    ItemRequestDto itemRequestDto;


    @BeforeEach
    void setUp() {
        id = 1L;

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

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("testDescription");
        itemRequest.setRequester(user);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequester(user.getId());
        itemRequestDto.setDescription("testDescription");
    }


    @Test
    void saveItemRequest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn((itemRequest));

        ItemRequestDto result = itemRequestService.saveItemRequest(id, itemRequestDto);

        assertNotNull(result, "не должен быть пустым");

        assertEquals(itemRequest.getId(), result.getId(), "id должно совпадать");
        assertEquals(itemRequest.getDescription(), result.getDescription(), "имя должно совпадать");
        assertEquals(itemRequest.getRequester().getId(), result.getRequester(), "descriptopn должно совпадать");

        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void findByUserId() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(Arrays.asList(itemRequest));

        Collection<ItemRequestDto> result = itemRequestService.findByUserId(id);

        assertNotNull(result, "не должен быть пустым");
        itemRequestDto.setId(1L);
        assertEquals(itemRequestDto, Arrays.stream(result.toArray()).toList().get(0), "id должно совпадать");

        verify(itemRequestRepository, times(1)).findAllByRequesterId(id);
    }

    @Test
    void findByAllNotUserId() {
        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(2L);
        itemRequest2.setDescription("testDescription2");
        itemRequest2.setRequester(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(anyLong())).thenReturn(Arrays.asList(itemRequest2));

        Collection<ItemRequestDto> result = itemRequestService.findByAllNotUserId(id);

        assertNotNull(result, "не должен быть пустым");
        itemRequestDto.setId(2L);
        itemRequestDto.setDescription("testDescription2");

        assertEquals(itemRequestDto, Arrays.stream(result.toArray()).toList().get(0), "id должно совпадать");

        verify(itemRequestRepository, times(1)).findByRequesterIdNotOrderByCreatedDesc(id);
    }

    @Test
    void findIdItemRequestById() {

        Item item2 = new Item();
        item2.setId(2L);
        item2.setDescription("test2");
        item2.setName("testName2");
        item2.setAvailable(true);
        item2.setUser(user);

        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        lenient().when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        lenient().when(itemRepository.findByRequest_id(anyLong())).thenReturn(Arrays.asList(item));


        ItemRequestDto result = itemRequestService.findIdItemRequestById(id, user.getId());

        assertNotNull(result, "не должен быть пустым");
        assertEquals(itemRequest.getId(), result.getId(), "id должно совпадать");
        assertEquals(itemRequest.getDescription(), result.getDescription(), "имя должно совпадать");
        assertEquals(itemRequest.getRequester().getId(), result.getRequester(), "descriptopn должно совпадать");

        verify(itemRequestRepository, times(1)).findById(id);
        verify(itemRepository, times(1)).findByRequest_id(id);
    }


}
