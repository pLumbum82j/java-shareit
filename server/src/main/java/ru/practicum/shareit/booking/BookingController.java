package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.booking.services.BookingService;
import ru.practicum.shareit.util.ConfigConstant;

import java.util.List;

/**
 * Класс BookingController по энпоинту Bookings
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Метод (эндпоинт) получения бронирования
     *
     * @param bookingId ID бронирования
     * @param userId    ID пользователя
     * @return Объект BookingDto
     */
    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable Long bookingId,
                          @RequestHeader(ConfigConstant.SHARER) Long userId) {
        return bookingService.get(bookingId, userId);
    }

    /**
     * Метод (эндпоинт) получения списка бронирований по пользователю
     *
     * @param userId ID пользователя
     * @param state  Статус
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return Список объектов BookingDto
     */
    @GetMapping()
    public List<BookingDto> getUserBookings(@RequestHeader(ConfigConstant.SHARER) Long userId,
                                            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getUserBookings(userId, state, from, size);
    }

    /**
     * Метод (эндпоинт) получения списка бронирований по владельцу
     *
     * @param ownerId ID владельца
     * @param state   Статус
     * @param from    индекс первого элемента
     * @param size    количество элементов для отображения
     * @return Список объектов BookingDto
     */
    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(ConfigConstant.SHARER) Long ownerId,
                                             @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getOwnerBookings(ownerId, state, from, size);
    }

    /**
     * Метод (эндпоинт) создания бронирования
     *
     * @param bookingDto Объект BookingDto
     * @param userId     ID пользователя
     * @return Созданный BookingDto
     */
    @PostMapping()
    public BookingDto create(@RequestBody ReceivedBookingDto bookingDto,
                             @RequestHeader(ConfigConstant.SHARER) Long userId) {
        return bookingService.create(bookingDto, userId);
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
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestParam("approved") boolean approved,
                             @RequestHeader(ConfigConstant.SHARER) Long userId) {
        return bookingService.update(bookingId, approved, userId);
    }
}
