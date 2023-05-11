package ru.practicum.shareit.request.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.util.OffsetPageRequest;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.request.models.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestDbTest {

    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserService userService;
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestDb itemRequestDb;

    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDtoAndListItems;
    private ItemRequest itemRequest;
    private Item item;
    User requestor;
    User owner;

    @BeforeEach
    void beforeEach() {
        requestor = new User(1L, "name", "requestor@yandex.ru");
        owner = new User(2L, "name", "owner@yandex.ru");
        itemRequest = new ItemRequest(1L, "itemRequestDescription", requestor, LocalDateTime.now());
        item = new Item(1L, "nameItem", "descriptionItem", true, owner, new ItemRequest());
        ItemDto itemDto = ItemMapper.toItemDto(item);
        List<ItemDto> itemList = List.of(itemDto);
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDtoAndListItems = ItemRequestMapper.toItemRequestDto(itemRequest, itemList);
    }

    @Test
    @SneakyThrows
    void getItemRequest_whenUserAndItemRequestFound_thenReturnedItemRequestListByRequestor() {
        List<ItemRequest> itemRequestList = List.of(itemRequest);
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(requestor));
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(1L)).thenReturn(itemRequestList);

        List<ItemRequestDto> actualItemRequestDto = itemRequestDb.get(requestor.getId());

        assertEquals(actualItemRequestDto.size(), itemRequestList.size());
    }

    @Test
    @SneakyThrows
    void getItemRequest_whenUserFoundAndItemRequestIsEmpty_thenReturnedItemRequestListIsEmpty() {
        List<ItemRequest> itemRequestList = List.of();
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(requestor));
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(1L)).thenReturn(itemRequestList);

        List<ItemRequestDto> actualItemRequestDto = itemRequestDb.get(requestor.getId());

        assertTrue(actualItemRequestDto.isEmpty());

    }

    @Test
    @SneakyThrows
    void getItemRequest_whenUserAndItemRequestFound_thenReturnedItemRequestByRequestor() {
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(requestor));
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequestDto actualItemRequestDto = itemRequestDb.get(requestor.getId(), itemRequest.getId());

        assertEquals(actualItemRequestDto.getId(), itemRequest.getId());
        verify(itemRequestRepository, times(1)).findById(itemRequest.getId());
    }

    @Test
    @SneakyThrows
    void getItemRequestPagination_whenUserAndItemRequestFound_thenReturnedItemRequestListByRequestor() {
        List<ItemRequest> itemRequestList = List.of(itemRequest);
        OffsetPageRequest offsetPageRequest = new OffsetPageRequest(1, 1, Sort.by(Sort.Direction.ASC, "created"));
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(requestor));
        when(itemRequestRepository.findAllByRequestorIdNot(requestor.getId(), offsetPageRequest)).thenReturn(itemRequestList);

        List<ItemRequestDto> actualItemRequestDto = itemRequestDb.get(requestor.getId(), 1, 1);

        assertEquals(actualItemRequestDto.size(), itemRequestList.size());
        verify(itemRequestRepository, times(1)).findAllByRequestorIdNot(requestor.getId(), offsetPageRequest);
    }

    @Test
    void createItemRequest_whenUserFoundAndItemRequestDtoValid_thenSavedItemRequest() {
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(requestor));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto actualItemRequest = itemRequestDb.create(1L, itemRequestDto);

        assertEquals(actualItemRequest.getId(), 1L);
        verify(itemRequestRepository).save(any(ItemRequest.class));
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }
}