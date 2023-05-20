package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.ConfigConstant.SHARER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {

    private final RequestClient requestClient;

    /**
     * Метод (эндпоинт) получения списка запросов по пользователю
     *
     * @param userId ID пользователя
     * @return Список объектов ItemRequestDto
     */
    @GetMapping()
    public ResponseEntity<Object> get(@RequestHeader(SHARER) Long userId) {
        return requestClient.get(userId);
    }

    /**
     * Метод (эндпоинт) получения списка всех запросов постранично
     *
     * @param userId ID пользователя
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return Список объектов ItemRequestDto
     */
    @GetMapping("/all")
    public ResponseEntity<Object> get(@RequestHeader(SHARER) Long userId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        return requestClient.get(from, size, userId);
    }

    /**
     * Метод (эндпоинт) получения одного запроса по пользователю
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Объект ItemRequestDto
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> get(@RequestHeader(SHARER) long userId, @PathVariable Long requestId) {
        return requestClient.get(requestId, userId);
    }

    /**
     * Метод (эндпоинт) создания запроса
     *
     * @param userId         ID пользователя
     * @param itemRequestDto Объект itemRequestDto
     * @return Созданный объект itemRequestDto
     */
    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader(SHARER) long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return requestClient.create(userId, itemRequestDto);
    }
}
