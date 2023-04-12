package ru.practicum.shareit.item.storages;

import ru.practicum.shareit.item.models.Item;

import java.util.List;

/**
 * Интерфейс ItemStorage для работы с данными Item на сервере
 */
public interface ItemStorage {

    /**
     * Метод получения списка всех вещей
     *
     * @return Список всех Item
     */
    List<Item> get();

    /**
     * Метод получения объекта Item по ID вещи
     *
     * @param itemId ID вещи
     * @return Объект Item
     */
    Item get(Long itemId);

    /**
     * Метод получения списка Item с использованием поиска по тексту
     *
     * @param text Текст поиска
     * @return Список Item
     */
    List<Item> search(String text);

    /**
     * Метод создания Item
     *
     * @param item Объект Item
     * @return Созданный Item
     */
    Item create(Item item);

    /**
     * Метод обновления Item
     *
     * @param itemId ID вещи
     * @param item   Объект Item
     * @return Обновлённый Item
     */
    Item update(Long itemId, Item item);

    /**
     * Метод удаления вещи по её ID
     *
     * @param itemId ID вещи
     */
    void delete(Long itemId);

    /**
     * Метод проверки вещи на сервере по её ID
     *
     * @param itemId ID вещи
     */
    boolean isContainItemId(Long itemId);

}
