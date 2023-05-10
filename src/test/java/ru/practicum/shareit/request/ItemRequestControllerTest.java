package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ConfigConstant;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.request.models.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.models.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDtoAndListItems;
    private final String userIdHeader = "X-Sharer-User-Id";

    @BeforeEach
    void beforeEach() {
        ItemRequest itemRequest = new ItemRequest(1L, "itemRequestDescription", new User(), LocalDateTime.now());
        Item item = new Item(1L, "nameItem", "descriptionItem", true, new User(), new ItemRequest());
        ItemDto itemDto = ItemMapper.toItemDto(item);
        List<ItemDto> itemList = List.of(itemDto);
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDtoAndListItems = ItemRequestMapper.toItemRequestDto(itemRequest, itemList);
    }

    @Test
    void getAllItemRequest_thenReturnStatusOkAndListItemRequest() {
//        when(itemRequestService.get()).thenReturn(List.of(userDto));
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
//                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
//                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())));
//
//        verify(userService, times(1)).get();
    }

    @Test
    @SneakyThrows
    void testGet() {
        when(itemRequestService.get(anyLong())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(ConfigConstant.SHARER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        //.contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
//                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
//                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
//                .andExpect(jsonPath("$.[0].reqestor", is(itemRequestDto.getRequestor())))
//                .andExpect(jsonPath("$.[0].created", is(itemRequestDto.getCreated())));

        verify(itemRequestService, times(1)).get(1L);

    }

    @Test
    @SneakyThrows
    void testGet1() {
        when(itemRequestService.get(anyLong(), any(), any())).thenReturn(List.of(itemRequestDto));

        String result = mockMvc.perform(get("/requests/all")
                        .header(ConfigConstant.SHARER, 1)
                        .param("from", "0")
                        .param("size", "2")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).get(anyLong(), any(), any());

    }

    @Test
    @SneakyThrows
    void create() {
        when(itemRequestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header(ConfigConstant.SHARER, 1)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).create(anyLong(), any(ItemRequestDto.class));
        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }
}