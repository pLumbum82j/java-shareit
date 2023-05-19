package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.request.models.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.util.ConfigConstant;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        itemRequest = new ItemRequest(1L, "itemRequestDescription", new User(), LocalDateTime.now());
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Test
    @SneakyThrows
    void getItemRequest_whenUserIdValidAndFound_thenReturnedItemRequest() {
        when(itemRequestService.get(anyLong())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(ConfigConstant.SHARER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).get(1L);

    }

    @Test
    @SneakyThrows
    void getItemRequest_whenUserIdFoundAndParamValid_thenReturnedItemRequest() {
        when(itemRequestService.get(anyLong(), any(), any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
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
    void getItemRequest_whenUserIdFoundAndParamNotValid_thenReturnBadRequest() {
        when(itemRequestService.get(anyLong(), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header(ConfigConstant.SHARER, 1))
                //.content(objectMapper.writeValueAsString(itemRequestDto))
                // .contentType(MediaType.APPLICATION_JSON))
                //   .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).get(anyLong(), anyLong());
    }


    @Test
    @SneakyThrows
    void createItemRequest_whenInvoked_thenReturnStatusOkAndItemRequest() {
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

//    @Test
//    @SneakyThrows
//    void createItemRequest_whenItemRequestDtoNotValid_thenReturnBadRequest() {
//        itemRequest.setDescription(null);
//        ItemRequestDto wrongItemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
//
//        when(itemRequestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(wrongItemRequestDto);
//
//        mockMvc.perform(post("/requests")
//                        .header(ConfigConstant.SHARER, 1)
//                        .content(objectMapper.writeValueAsString(wrongItemRequestDto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        verify(itemRequestService, never()).create(anyLong(), any(ItemRequestDto.class));
//    }
}