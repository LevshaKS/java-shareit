package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    ItemDto itemDto, itemDtoOut;

    Long userId, itemId;

    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();

        userId = 1L;
        itemId = 1L;

        itemDto = new ItemDto();
        itemDto.setName("test name");
        itemDto.setDescription("test description");
        itemDto.setAvailable(true);
        itemDto.setComments(Collections.emptyList());

        itemDtoOut = new ItemDto();
        itemDtoOut.setId(itemId);
        itemDtoOut.setName("test name");
        itemDtoOut.setDescription("test description");
        itemDtoOut.setAvailable(true);
        itemDto.setComments(Collections.emptyList());
    }

    @Test
    void createItem() throws Exception {

        when(itemService.saveItem(anyLong(), ArgumentMatchers.any())).thenReturn(itemDtoOut);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test name"));

        verify(itemService, times(1)).saveItem(anyLong(), any());
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), eq(itemId), ArgumentMatchers.any())).thenReturn(itemDtoOut);
        mvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("test name"));

        verify(itemService, times(1)).updateItem(anyLong(), eq(itemId), any());
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemByItemId(anyLong())).thenReturn(itemDtoOut);

        mvc.perform(get("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value("test name"));

        verify(itemService, times(1)).getItemByItemId(anyLong());
    }

    @Test
    void getAllByUserId() throws Exception {
        itemDto.setId(1L);
        when(itemService.getAllItemByUserId(anyLong())).thenReturn(Arrays.asList(itemDtoOut));

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test name"));

        verify(itemService, times(1)).getAllItemByUserId(anyLong());
    }

    @Test
    void searchItemByName() throws Exception {
        when(itemService.findItemByName(anyString())).thenReturn(Arrays.asList(itemDtoOut));

        mvc.perform(get("/items/search")
                        .param("text", "testText")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test name"));

        verify(itemService, times(1)).findItemByName("testText");
    }

    @Test
    void createComment() throws Exception {


        CommentDto commentDto = new CommentDto();
        commentDto.setId(itemId);
        commentDto.setText("testText");
        commentDto.setAuthorName("author");

        when(itemService.addComment(anyLong(), anyLong(), ArgumentMatchers.any())).thenReturn(commentDto);

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

        verify(itemService, times(1)).addComment(anyLong(), anyLong(), any());

    }

    @Test
    void delItem() throws Exception {
        mvc.perform(delete("/items/{itemId}", itemId)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(userId, itemId);
    }
}
