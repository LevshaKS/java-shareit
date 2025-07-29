package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemValidateTest {

    ItemDto itemDto;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ValidateItemController validateItemController;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();

        itemDto.setName("test");
        itemDto.setDescription("test");
        itemDto.setAvailable(true);

    }

    @Test
    void ItemDtoNotName() {
        itemDto.setName(null);
        assertThrows(NotDataException.class, () -> validateItemController.validateItemDto(itemDto), "name не может быть пустым");

    }

    @Test
    void ItemDtoNotDescription() {
        itemDto.setDescription(null);
        assertThrows(NotDataException.class, () -> validateItemController.validateItemDto(itemDto), "Description не может быть пустым");
    }

    @Test
    void ItemDtoNotAvailable() {
        itemDto.setAvailable(null);
        assertThrows(NotDataException.class, () -> validateItemController.validateItemDto(itemDto), "Available не может быть пустым");
    }

}
