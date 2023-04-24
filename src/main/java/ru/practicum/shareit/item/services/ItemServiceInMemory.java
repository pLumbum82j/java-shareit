package ru.practicum.shareit.item.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.CommentDto;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.storages.ItemStorage;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс ItemServiceInMemory для отработки логики запросов и логирования
 */
@Log4j2
@Service
public class ItemServiceInMemory implements ItemService {
    public ItemServiceInMemory(ItemStorage itemStorage, @Qualifier("userServiceInMemory") UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public List<ItemDto> get(Long userId) {
        log.debug("Получен запрос на список ItemDto по userId: {}", userId);
        return itemStorage.get().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto get(Long userId, Long itemId) {
        userService.get(userId);
        containItemId(itemId);
        Item item = itemStorage.get(itemId);
        log.debug("Получен запрос на ItemDto по itemId: {} и userId: {}", itemId, userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Item getItem(long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        userService.get(userId);
        if (text.isEmpty() || text.isBlank()) {
            log.debug("Получен запрос на список ItemDto по text: {} - ничего не найдено", text);
            return new ArrayList<>();
        } else {
            log.debug("Получен запрос на список ItemDto по text: {} - найдены совпадения по тексту", text);
            return itemStorage.search(text.toLowerCase()).stream()
                    .map(ItemMapper::toItemDto).collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userService.get(userId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        log.debug("Получен запрос на создание Item по userId: {}", userId);
        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public CommentDto create(CommentDto commentDto, long itemId, long userId) {
        return null;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        userService.get(userId);
        Item itemOld = itemStorage.get(itemId);
        Item itemTemp = ItemMapper.toItem(itemDto);
        if (!userId.equals(itemOld.getOwner().getId())) {
            throw new ObjectUnknownException("Пользователю с идентификатором ID: " + userId + " недоступно форматирование вещи ID: " + itemId);
        }
        if (itemTemp.getName() != null) {
            itemOld.setName(itemTemp.getName());
        }
        if (itemTemp.getDescription() != null) {
            itemOld.setDescription(itemTemp.getDescription());
        }
        if (itemTemp.getAvailable() != null) {
            itemOld.setAvailable(itemTemp.getAvailable());
        }
        log.debug("Получен запрос на изменение вещи с ID: {}", itemId);
        return ItemMapper.toItemDto(itemStorage.update(itemId, itemOld));
    }

    public void delete(Long itemId) {
        containItemId(itemId);
        itemStorage.delete(itemId);
    }

    /**
     * Метод проверки вещи на сервере по её ID
     *
     * @param itemId ID вещи
     */
    private void containItemId(long itemId) {
        if (!itemStorage.isContainItemId(itemId)) {
            throw new ObjectUnknownException("Вещь с ID " + itemId + " не существует");
        }
    }
}
