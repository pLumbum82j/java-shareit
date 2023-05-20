package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.ConfigConstant.SHARER;

/**
 * Класс BookingController по энпоинту Bookings
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;


    /**
     * Метод (эндпоинт) получения бронирования
     *
     * @param bookingId ID бронирования
     * @param userId    ID пользователя
     * @return Объект BookingDto
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@RequestHeader(SHARER) long userId,
                                      @PathVariable Long bookingId) {
        return bookingClient.get(userId, bookingId);
    }

    /**
     * Метод (эндпоинт) получения списка бронирований по пользователю
     *
     * @param userId     ID пользователя
     * @param stateParam Статус
     * @param from       индекс первого элемента
     * @param size       количество элементов для отображения
     * @return Список объектов BookingDto
     */
    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(SHARER) long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                  @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return bookingClient.getUserBookings(userId, stateParam, from, size);
    }

    /**
     * Метод (эндпоинт) получения списка бронирований по владельцу
     *
     * @param ownerId    ID владельца
     * @param stateParam Статус
     * @param from       индекс первого элемента
     * @param size       количество элементов для отображения
     * @return Список объектов BookingDto
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(SHARER) long ownerId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        return bookingClient.getOwnerBookings(ownerId, stateParam, from, size);
    }

    /**
     * Метод (эндпоинт) создания бронирования
     *
     * @param requestDto Объект BookingDto
     * @param userId     ID пользователя
     * @return Созданный BookingDto
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(SHARER) long userId,
                                         @RequestBody @Valid BookItemRequestDto requestDto) {
        return bookingClient.create(userId, requestDto);
    }

    /**
     * Метод обновления бронирования
     *
     * @param bookingId ID бронирования
     * @param approved  Обновляемый статус
     * @param userId    ID пользователя
     * @return Обновлённый BookingDto
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable Long bookingId,
                                         @RequestHeader(SHARER) Long userId,
                                         @RequestParam("approved") boolean approved) {
        return bookingClient.update(bookingId, userId, approved);
    }
}
