package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.ValidateUserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

import java.util.stream.Collectors;

@Validated
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidateUserController validateUserController;

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        validateUserController.validateUserDto(userDto);
        User user = UserMapper.mapToUser(userDto);
        log.info("пердан пользователь для создания");
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new NotDataException("нет такого пользователя"));
        if (userDto.getEmail() == null) {
            userDto.setEmail(updateUser.getEmail());
        }
        if ((userDto.getName() == null)) {
            userDto.setName(updateUser.getName());
        }
        User findUser = userRepository.findByEmail(userDto.getEmail());

        if (findUser == null || findUser.getId().equals(userId)) {
            updateUser = UserMapper.mapToUserUpdate(userId, userDto);
            updateUser = userRepository.save(updateUser);
            log.info("передали пользователя для обновления");
            return UserMapper.mapToUserDto(updateUser);

        } else {
            log.warn("такой email уже есть");
            throw new DuplicateDataException("такой email уже есть");
        }
    }

    @Override
    public UserDto findByIdUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotDataException("нет такого пользоваетля"));
        log.info("передали запрос пользователя по id " + id);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> findAllUser() {
        log.info("передали запрос вывести всех пользователей");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotDataException("нет такого пользоваетля"));
        log.info("передали удаление пользователя по id " + id);
        userRepository.deleteById(id);
    }
}
