package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Component
public class ValidateUserController {

    public void validateUserDto(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            log.warn("name не может быть пустым\"");
            throw new NotDataException("name не может быть пустым");
        }

        if (!userDto.getEmail().contains("@")) {
            log.warn("введен не email");
            throw new NotDataException("введен не email");
        }
    }
}
