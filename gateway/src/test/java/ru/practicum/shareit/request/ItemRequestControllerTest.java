package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.contoller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    ItemRequestClient itemRequestClient;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    long id;
    long userId;

    ItemRequestDto itemRequestDto, itemRequestDtoRequest;


    @BeforeEach
    void setUp() {
        userId = 1L;
        id = 1L;
        itemRequestDto = new ItemRequestDto(null, "test", userId, null, Collections.emptySet());
        itemRequestDtoRequest = new ItemRequestDto(id, "test", userId, null, Collections.emptySet());

    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestClient.createItemRequest(anyLong(), ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(itemRequestDtoRequest));

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.requester").value(userId));
        verify(itemRequestClient, times(1)).createItemRequest(anyLong(), any());
    }

    @Test
    void findIdItemRequestById() throws Exception {
        when(itemRequestClient.findIdItemRequestById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(itemRequestDtoRequest));

        mvc.perform(get("/requests/{id}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.requester").value(userId));
        verify(itemRequestClient, times(1)).findIdItemRequestById(anyLong(), anyLong());
    }

    @Test
    void findByUser() throws Exception {
        when(itemRequestClient.findByUser(anyLong())).thenReturn(ResponseEntity.ok(Arrays.asList(itemRequestDtoRequest)));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].requester").value(userId));
        verify(itemRequestClient, times(1)).findByUser(anyLong());
    }

    @Test
    void findByAlLUser() throws Exception {
        when(itemRequestClient.findByAlLUser(anyLong())).thenReturn(ResponseEntity.ok(Arrays.asList(itemRequestDtoRequest)));

        mvc.perform(get("/requests/ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].requester").value(userId));
        verify(itemRequestClient, times(1)).findByAlLUser(anyLong());
    }

}
