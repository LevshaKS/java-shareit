package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    ItemClient itemClient;

    @MockBean
    ValidateItemController validateItemController;
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    ItemDto itemDto, itemDtoRequest;

    Long userId, itemId;

    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();

        userId = 1L;
        itemId = 1L;

        itemDto = new ItemDto(null, "test name", "test description", true, null, null, Collections.emptyList());
        itemDtoRequest = new ItemDto(1L, "test name", "test description", true, null, null, Collections.emptyList());

    }

    @Test
    void createItem() throws Exception {

        when(itemClient.createItem(anyLong(), ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(itemDtoRequest));

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test name"));

        verify(itemClient, times(1)).createItem(eq(userId), any());
    }

    @Test
    void updateItem() throws Exception {
        when(itemClient.updateItem(anyLong(), eq(itemId), ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(itemDtoRequest));

        mvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("test name"));

        verify(itemClient, times(1)).updateItem(eq(userId), eq(itemId), any());
    }

    @Test
    void getItemById() throws Exception {
        when(itemClient.getItemById(anyLong())).thenReturn(ResponseEntity.ok(itemDtoRequest));

        mvc.perform(get("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("test name"));

        verify(itemClient, times(1)).getItemById(itemId);

    }

    @Test
    void getAllByUserId() throws Exception {
        itemDto.setId(1L);

        when(itemClient.getItemByUserID(anyLong())).thenReturn(ResponseEntity.ok(Arrays.asList(itemDtoRequest)));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test name"));

        verify(itemClient, times(1)).getItemByUserID(userId);
    }

    @Test
    void searchItemByName() throws Exception {
        when(itemClient.searchItemByName(anyString())).thenReturn(ResponseEntity.ok(Arrays.asList(itemDtoRequest)));

        mvc.perform(get("/items/search")
                        .param("text", "testText")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test name"));

        verify(itemClient, times(1)).searchItemByName("testText");
    }

    @Test
    void createComment() throws Exception {


        CommentDto commentDto = new CommentDto();
        commentDto.setId(itemId);
        commentDto.setText("testText");
        commentDto.setAuthorName("author");

        when(itemClient.createComment(anyLong(), anyLong(), ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(commentDto));
        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.authorName").value("author"))
                .andExpect(jsonPath("$.text").value("testText"));

        verify(itemClient, times(1)).createComment(anyLong(), anyLong(), any());

    }
}
