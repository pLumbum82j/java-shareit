package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс ItemController по энпоинту Items
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Метод (эндпоинт) получения списка ItemDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Список ItemDto
     */
    @GetMapping()
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.get(userId);
    }

    /**
     * Метод (эндпоинт) получения ItemDto по ID вещи с проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @return Объект ItemDto
     */
    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader(value = "X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        return itemService.get(userId, itemId);
    }

    /**
     * Метод (эндпоинт) получения списка ItemDto с использованием поиска по тексту и проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param text   Текст поиска
     * @return Список ItemDto
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                @Valid @RequestParam String text) {
        return itemService.search(userId, text);
    }

    /**
     * Метод (эндпоинт) создания Item
     *
     * @param userId  ID пользователя
     * @param itemDto Объект ItemDto
     * @return Созданный ItemDto
     */
    @PostMapping()
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    /**
     * Метод (эндпоинт) обновления Item
     *
     * @param userId  ID пользователя
     * @param itemId  ID вещи
     * @param itemDto Объект ItemDto
     * @return Обновлённый ItemDto
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                          @Valid @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

}
