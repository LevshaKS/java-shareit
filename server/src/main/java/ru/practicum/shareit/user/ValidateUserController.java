package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Component
public class ValidateUserController {

    private final UserRepository userRepository;

    public ValidateUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserDto(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            log.warn("такой email уже есть");
            throw new DuplicateDataException("такой email уже есть");
        }
    }
}
