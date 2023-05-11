package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.ConfigConstant;
import ru.practicum.shareit.request.models.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Класс ItemRequestController по энпоинту Requests
 */
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    /**
     * Метод (эндпоинт) получения списка запросов по пользователю
     *
     * @param userId ID пользователя
     * @return Список объектов ItemRequestDto
     */
    @GetMapping()
    public List<ItemRequestDto> get(@RequestHeader(ConfigConstant.sharer) Long userId) {
        return itemRequestService.get(userId);
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
    public List<ItemRequestDto> get(@RequestHeader(ConfigConstant.sharer) Long userId,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemRequestService.get(userId, from, size);
    }

    /**
     * Метод (эндпоинт) получения одного запроса по пользователю
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Объект ItemRequestDto
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto get(@RequestHeader(ConfigConstant.sharer) long userId, @PathVariable Long requestId) {
        return itemRequestService.get(userId, requestId);
    }

    /**
     * Метод (эндпоинт) создания запроса
     *
     * @param userId         ID пользователя
     * @param itemRequestDto Объект itemRequestDto
     * @return Созданный объект itemRequestDto
     */
    @PostMapping()
    public ItemRequestDto create(@RequestHeader(ConfigConstant.sharer) long userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto);
    }
}
