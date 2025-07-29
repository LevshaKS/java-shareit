package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserValidateTest {
    UserDto userDto;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ValidateUserController validateUserController;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("test@email.ru");

    }

    @Test
    void userDtoNotName() {
        userDto.setName(null);
        assertThrows(NotDataException.class, () -> validateUserController.validateUserDto(userDto), "name не может быть пустым");

    }

    @Test
    void userDtoNotEmail() {
        userDto.setEmail(null);
        assertThrows(NotDataException.class, () -> validateUserController.validateUserDto(userDto), "email не может быть пустым");
    }


    @Test
    void userDtoNotContains() {
        userDto.setEmail("testemail.ru");
        assertThrows(NotDataException.class, () -> validateUserController.validateUserDto(userDto), "введен не email");
    }

}
