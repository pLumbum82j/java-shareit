package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.ConfigConstant.SHARER;


@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    /**
     * Метод (эндпоинт) получения списка ItemDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Список ItemDto
     */
    @GetMapping()
    public ResponseEntity<Object> get(@RequestHeader(SHARER) Long userId) {
        return itemClient.get(userId);
    }

    /**
     * Метод (эндпоинт) получения ItemDto по ID вещи с проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @return Объект ItemDto
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(SHARER) Long userId, @PathVariable("itemId") Long itemId) {
        return itemClient.get(userId, itemId);
    }

    /**
     * Метод (эндпоинт) получения списка ItemDto с использованием поиска по тексту и проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param text   Текст поиска
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return Список ItemDto
     */
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(SHARER) Long userId,
                                         @RequestParam(value = "text") String text,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemClient.search(text, from, size, userId);
    }

    /**
     * Метод (эндпоинт) создания Item
     *
     * @param userId  ID пользователя
     * @param itemDto Объект ItemDto
     * @return Созданный ItemDto
     */
    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader(SHARER) Long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemClient.create(userId, itemDto);
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
    public ResponseEntity<Object> create(@RequestHeader(SHARER) Long userId,
                                         @PathVariable("itemId") Long itemId,
                                         @Valid @RequestBody CommentDto commentDto) {
        return itemClient.create(userId, itemId, commentDto);
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
    public ResponseEntity<Object> update(@RequestHeader(SHARER) Long userId,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestBody ItemDto itemDto) {
        return itemClient.update(userId, itemId, itemDto);
    }
}