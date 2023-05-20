package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.models.dto.CommentDto;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.util.ConfigConstant;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс ItemController по энпоинту Items
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * Метод (эндпоинт) получения списка ItemDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Список ItemDto
     */
    @GetMapping()
    public List<ItemDto> get(@RequestHeader(ConfigConstant.SHARER) Long userId) {
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
    public ItemDto get(@RequestHeader(ConfigConstant.SHARER) long userId, @PathVariable Long itemId) {
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
    public List<ItemDto> search(@RequestHeader(ConfigConstant.SHARER) long userId,
                                @RequestParam String text,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        return itemService.search(userId, text, from, size);
    }

    /**
     * Метод (эндпоинт) создания Item
     *
     * @param userId  ID пользователя
     * @param itemDto Объект ItemDto
     * @return Созданный ItemDto
     */
    @PostMapping()
    public ItemDto create(@RequestHeader(ConfigConstant.SHARER) long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    /**
     * Метод (эндпоинт) создания комментария к вещи
     *
     * @param userId     ID пользователя
     * @param itemId     ID вещи
     * @param commentDto Объект CommentDto
     * @return Созданный CommentDto
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto create(@RequestHeader(ConfigConstant.SHARER) long userId,
                             @PathVariable long itemId,
                             @RequestBody CommentDto commentDto
    ) {
        return itemService.create(commentDto, itemId, userId);
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
    public ItemDto update(@RequestHeader(ConfigConstant.SHARER) long userId,
                          @Valid @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }
}
