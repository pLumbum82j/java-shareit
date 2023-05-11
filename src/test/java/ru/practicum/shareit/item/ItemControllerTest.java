package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ConfigConstant;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.models.Comment;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.CommentDto;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private Item item;
    private ItemDto itemDto;

    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        item = new Item(1L, "ItemName", "description", true, new User(), new ItemRequest());
        itemDto = ItemMapper.toItemDto(item);
        comment = new Comment(1L, "text", item, new User(), LocalDateTime.now());
        commentDto = CommentMapper.toCommentDto(comment);
    }

    @Test
    @SneakyThrows
    void getItem_whenUserFound_thenReturnedItemList() {
        when(itemService.get(anyLong())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header(ConfigConstant.SHARER, 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1)).get(anyLong());
    }

    @Test
    @SneakyThrows
    void getItem_whenUserAndItemIdFound_thenReturnedItem() {
        when(itemService.get(anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(ConfigConstant.SHARER, 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1)).get(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getItemList_whenUserAndTextFound_thenReturnedItemList() {
        when(itemService.search(anyLong(), anyString())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .header(ConfigConstant.SHARER, 1L)
                        .param("text", "аккум"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).search(anyLong(), anyString());
    }

    @Test
    @SneakyThrows
    void createItem_whenInvoked_thenReturnStatusOkAndItem() {
        when(itemService.create(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(ConfigConstant.SHARER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).create(anyLong(), any(ItemDto.class));

    }
    //Не работает
    @Test
    @SneakyThrows
    void createComment_whenInvoked_thenReturnStatusOkAndItem() {
        when(itemService.create(any(CommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConfigConstant.SHARER, 1L)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(result, objectMapper.writeValueAsString(commentDto));

        verify(itemService, times(1)).create(any(CommentDto.class), anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void updateItem_whenUserAndItemFound_thenReturnStatusOkAndItem() {
        when(itemService.update(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConfigConstant.SHARER, 1L)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(itemDto));
        verify(itemService, times(1)).update(anyLong(), anyLong(), any(ItemDto.class));
    }
}