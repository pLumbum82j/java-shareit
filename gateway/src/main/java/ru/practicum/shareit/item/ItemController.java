package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    private static final String SHARER = "X-Sharer-User-Id";

    /**
     * Метод (эндпоинт) получения списка ItemDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Список ItemDto
     */
    @GetMapping()
    public ResponseEntity<Object> get(@RequestHeader(SHARER) Long userId) {
        log.info("Get all Items by user={}", userId);
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
        log.info("Get Item by user={}, with itemId={}", userId, itemId);
        return itemClient.get(userId, itemId);
    }

    /**
     * Метод (эндпоинт) получения списка ItemDto с использованием поиска по тексту и проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param text   Текст поиска
     * @return Список ItemDto
     */
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(SHARER) Long userId,
                                                     @RequestParam(value = "text") String text,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get Item from text={}, with userId={}", text, userId);
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
        log.info("Create Item by user={}, item name={}", userId, itemDto.getName());
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
        log.info("Comment added for item={}, with userId={}", itemId, userId);
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
    public ResponseEntity<Object> update(@RequestHeader(SHARER) Long userId, @PathVariable("itemId") Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Update Item by user={}, with itemId={}", userId, itemId);
        return itemClient.update(userId, itemId, itemDto);
    }
}