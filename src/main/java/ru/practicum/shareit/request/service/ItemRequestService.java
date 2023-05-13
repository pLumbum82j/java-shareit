package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.models.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    /**
     * Метод получения списка запросов по пользователю
     *
     * @param userId ID пользователя
     * @return Список объектов ItemRequestDto
     */
    List<ItemRequestDto> get(Long userId);

    /**
     * Метод получения списка всех запросов постранично
     *
     * @param userId ID пользователя
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return Список объектов ItemRequestDto
     */
    List<ItemRequestDto> get(Long userId, Integer from, Integer size);

    /**
     * Метод (эндпоинт) получения одного запроса по пользователю
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Объект ItemRequestDto
     */
    ItemRequestDto get(Long userId, Long requestId);

    /**
     * Метод (эндпоинт) создания запроса
     *
     * @param userId         ID пользователя
     * @param itemRequestDto Объект itemRequestDto
     * @return Созданный объект itemRequestDto
     */
    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);
}
