package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateUserTest {

    @Mock
    private UserDto userDto;


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ValidateUserController validateUserController;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("test");
        userDto.setEmail("test@test.ru");

    }

    @Test
    void itemDtoNotEmail() {
        validateUserController.validateUserDto(userDto);
        when(userRepository.findByEmail(anyString()) != null).thenThrow(DuplicateDataException.class);

        assertThrows(DuplicateDataException.class, () -> userRepository.findByEmail("test@test.ru"), "такой адрес электронной почты уже есть");
    }

}
