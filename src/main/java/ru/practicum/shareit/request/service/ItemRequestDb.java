package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetPageRequest;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.request.models.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestDb implements ItemRequestService {

    final private ItemRequestRepository itemRequestRepository;
    final private UserService userService;
    final private ItemRepository itemRepository;

    @Override
    public List<ItemRequestDto> get(Long userId) {
        userService.get(userId);
        List<ItemRequest> list = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);

        return setItemsToItemRequestAndTransformToDto(list);
    }

    @Override
    public List<ItemRequestDto> get(Long userId, Integer from, Integer size) {
        OffsetPageRequest offsetPageRequest = new OffsetPageRequest(from, size, Sort.by(Sort.Direction.ASC, "created"));
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNot(userId, offsetPageRequest);
        return setItemsToItemRequestAndTransformToDto(itemRequests);
    }

    @Override
    public ItemRequestDto get(Long userId, Long requestId) {
        userService.get(userId);
        List<ItemRequest> itemRequestList = Collections.singletonList(itemRequestRepository.get(requestId));
        List<ItemRequestDto> itemRequestDtoList = setItemsToItemRequestAndTransformToDto(itemRequestList);
        return itemRequestDtoList.isEmpty() ? null : itemRequestDtoList.get(0);

    }

    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userService.get(userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        log.debug("Получен запрос на создание itemRequest по userId: {}", userId);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    /**
     * Метод добавления списка запросов
     *
     * @param itemRequests Список запросов (объектов ItemRequest)
     * @return Список запросов (объектов itemRequestDto)
     */
    private List<ItemRequestDto> setItemsToItemRequestAndTransformToDto(List<ItemRequest> itemRequests) {
        Map<ItemRequest, List<Item>> allItems = itemRepository.findAllByRequestIn(itemRequests)
                .stream()
                .collect(Collectors.groupingBy(Item::getRequest, Collectors.toList()));
        List<ItemRequestDto> valueItemRequestList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> itemList = allItems.get(itemRequest);
            if (itemList == null) {
                itemList = new ArrayList<>();
            }
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest, itemList
                    .stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList()));
            valueItemRequestList.add(itemRequestDto);
        }
        return valueItemRequestList;
    }
}
