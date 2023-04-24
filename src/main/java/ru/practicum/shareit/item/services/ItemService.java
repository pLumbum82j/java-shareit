package ru.practicum.shareit.item.services;

import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.CommentDto;
import ru.practicum.shareit.item.models.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс ItemService для обработки логики запросов из ItemController
 */
public interface ItemService {

    /**
     * Метод получения списка ItemDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Список ItemDto
     */
    List<ItemDto> get(Long userId);

    /**
     * Метод получения ItemDto по ID вещи с проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @return Объект ItemDto
     */
    ItemDto get(Long userId, Long itemId);

    Item getItem(long itemId);

    /**
     * Метод получения списка ItemDto с использованием поиска по тексту и проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param text   Текст поиска
     * @return Список ItemDto
     */
    List<ItemDto> search(Long userId, String text);

    /**
     * Метод создания Item
     *
     * @param userId  ID пользователя
     * @param itemDto Объект ItemDto
     * @return Созданный ItemDto
     */
    ItemDto create(Long userId, ItemDto itemDto);

    CommentDto create(CommentDto commentDto, long itemId, long userId);

    /**
     * Метод обновления Item
     *
     * @param userId  ID пользователя
     * @param itemId  ID вещи
     * @param itemDto Объект ItemDto
     * @return Обновлённый ItemDto
     */
    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    /**
     * Метод удаления вещи по её ID
     *
     * @param itemId ID вещи
     */
    void delete(Long itemId);

}
