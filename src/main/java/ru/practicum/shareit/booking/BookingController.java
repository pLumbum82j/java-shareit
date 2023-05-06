package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ConfigConstant;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.booking.services.BookingService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public BookingDto get(@PathVariable long bookingId,
                          @RequestHeader(ConfigConstant.SHARER) long userId) {
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
    public List<BookingDto> getUserBookings(@RequestHeader(ConfigConstant.SHARER) long userId,
                                            @RequestParam(value = "state", defaultValue = "ALL") String state,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size) {
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
    public List<BookingDto> getOwnerBookings(@RequestHeader(ConfigConstant.SHARER) long ownerId,
                                             @RequestParam(name = "state", defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
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
                             @RequestHeader(ConfigConstant.SHARER) long userId) {
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
    public BookingDto update(@PathVariable long bookingId,
                             @RequestParam("approved") String approved,
                             @RequestHeader(ConfigConstant.SHARER) long userId) {
        return bookingService.update(bookingId, approved.toLowerCase(), userId);
    }
}
