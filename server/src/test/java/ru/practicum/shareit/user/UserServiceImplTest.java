package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private ValidateUserController validateUserController;


    @InjectMocks
    private UserServiceImpl userService;


    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {

        userDto = new UserDto();
        userDto.setName("testName");
        userDto.setEmail("test@email.com");

        user = new User();
        user.setId(1L);
        user.setName("testName");
        user.setEmail("test@email.com");
    }

    @Test
    void saveUser() {
        when(userRepository.save(any(User.class))).thenReturn((user));

        UserDto result = userService.saveUser(userDto);

        assertNotNull(result, "не должен быть пустым");
        assertEquals(user.getId(), result.getId(), "id должно совпадать");
        assertEquals(user.getName(), result.getName(), "имя должно совпадать");
        assertEquals(user.getEmail(), result.getEmail(), "емайл должно совпадать");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        User userUpdated = new User();
        userUpdated.setId(1L);
        userUpdated.setName("testNameUpdate");
        userUpdated.setEmail("test@email.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn((userUpdated));

        UserDto result = userService.updateUser(1L, userDto);

        assertNotNull(result, "не должен быть пустым");
        assertEquals(userUpdated.getId(), result.getId(), "id должно совпадать");
        assertEquals(userUpdated.getName(), result.getName(), "имя должно совпадать");
        assertEquals(userUpdated.getEmail(), result.getEmail(), "емайл должно совпадать");

        verify(userRepository, times(1)).save(any(User.class));


    }

    @Test
    void updateUserIsNotDataException() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotDataException.class, () -> userService.updateUser(2L, userDto), "должна быть ошибка не найден id");

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any());
    }


    @Test
    void findByIdUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto result = userService.findByIdUser(1L);

        assertNotNull(result, "не должен быть пустым");
        assertEquals(user.getId(), result.getId(), "id должно совпадать");
        assertEquals(user.getName(), result.getName(), "имя должно совпадать");
        assertEquals(user.getEmail(), result.getEmail(), "емайл должно совпадать");

        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void findByIdUserIsNotDataException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotDataException.class, () -> userService.findByIdUser(1L), "должна быть ошибка не найден id");

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findByAllUser() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        Collection<UserDto> result = userService.findAllUser();

        assertNotNull(result, "не должен быть пустым");
        userDto.setId(1L);
        assertEquals(userDto, Arrays.stream(result.toArray()).toList().get(0), "user должен совпадать");

        verify(userRepository, times(1)).findAll();
    }


    @Test
    void deleteUser() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserNotFound() {
        long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotDataException.class, () -> {
            userService.deleteUser(userId);
        });

        verify(userRepository, never()).deleteById(userId);
    }
}
