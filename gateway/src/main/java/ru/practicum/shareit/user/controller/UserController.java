package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.ValidateUserController;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

@Validated
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    private final ValidateUserController validateUserController;


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserId(@Positive(message = "неверное значение") @PathVariable long id) {
        log.info("запрос пользателя по id " + id);
        return userClient.getUserId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("запрос списка всех пользователей");
        return userClient.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        validateUserController.validateUserDto(userDto);
        log.info("запрос добавления нового пользователя");
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@Positive(message = "неверное значение") @PathVariable("userId") long userId,
                                             @Valid @RequestBody UserDto userDto) {
        log.info("запрос обновления пользователя с id " + userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delUser(@Positive(message = "неверное значение") @PathVariable("userId") long userId) {
        log.info("удаление пользователя с id " + userId);
        userClient.delUser(userId);
    }
}
