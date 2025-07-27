package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    long id;
    long userId;

    ItemRequestDto itemRequestDto, itemRequestDto2;


    @BeforeEach
    void setUp() {
        userId = 1L;
        id = 1L;
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(id);
        itemRequestDto.setRequester(userId);
        itemRequestDto.setDescription("test");
        itemRequestDto.setItems(Collections.emptySet());

        itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(id + 1);
        itemRequestDto2.setRequester(userId + 1);
        itemRequestDto2.setDescription("test");
        itemRequestDto2.setItems(Collections.emptySet());

    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.saveItemRequest(anyLong(), ArgumentMatchers.any())).thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.requester").value(userId));
        verify(itemRequestService, times(1)).saveItemRequest(anyLong(), any());
    }

    @Test
    void findIdItemRequestById() throws Exception {
        when(itemRequestService.findIdItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{id}", id)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.requester").value(userId));
        verify(itemRequestService, times(1)).findIdItemRequestById(anyLong(), anyLong());
    }

    @Test
    void findByUser() throws Exception {
        when(itemRequestService.findByUserId(anyLong())).thenReturn(Arrays.asList(itemRequestDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].requester").value(userId));
        verify(itemRequestService, times(1)).findByUserId(anyLong());
    }

    @Test
    void findByAlLUser() throws Exception {
        when(itemRequestService.findByAllNotUserId(anyLong())).thenReturn(Arrays.asList(itemRequestDto2));

        mvc.perform(get("/requests/ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id + 1))
                .andExpect(jsonPath("$[0].description").value("test"))
                .andExpect(jsonPath("$[0].requester").value(userId + 1));
        verify(itemRequestService, times(1)).findByAllNotUserId(anyLong());
    }

}
