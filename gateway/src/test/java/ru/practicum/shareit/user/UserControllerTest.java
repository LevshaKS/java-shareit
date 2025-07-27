package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    UserClient userClient;


    UserController userController;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    UserDto userDto, userDtoRequest;

    long userId;


    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        userDto = new UserDto(null, "test", "test@test.ru");
        userDtoRequest = new UserDto(1L, "test", "test@test.ru");
        userId = 1;

    }

    @Test
    void createItem() throws Exception {

        when(userClient.createUser(ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(userDtoRequest));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.ru"));

        verify(userClient, times(1)).createUser(any());
    }

    @Test
    void getUserId() throws Exception {
        when(userClient.getUserId(anyLong())).thenReturn(ResponseEntity.ok(userDtoRequest));

        mvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.ru"));

        verify(userClient, times(1)).getUserId(anyLong());
    }

    @Test
    void getAllUsers() throws Exception {
        when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok(Arrays.asList(userDtoRequest)));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[0].email").value("test@test.ru"));

        verify(userClient, times(1)).getAllUsers();
    }

    @Test
    void updateUser() throws Exception {
        userDtoRequest.setName("test2");
        when(userClient.updateUser(anyLong(), ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(userDtoRequest));


        mvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test2"))
                .andExpect(jsonPath("$.email").value("test@test.ru"));

        verify(userClient, times(1)).updateUser(anyLong(), any());
    }


    @Test
    void delUserId() throws Exception {
        when(userClient.delUser(anyLong())).thenReturn(ResponseEntity.ok(""));

        mvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userClient, times(1)).delUser(anyLong());
    }

}


