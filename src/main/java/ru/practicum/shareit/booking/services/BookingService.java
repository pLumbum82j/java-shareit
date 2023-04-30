package ru.practicum.shareit.booking.services;

import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;

import java.util.List;

/**
 * Интерфейс BookingService для обработки логики запросов из BookingController
 */
public interface BookingService {
    /**
     * Метод получения объекта BookingDto по bookingId и userId
     *
     * @param bookingId ID бронирования
     * @param userId    ID пользователя
     * @return Объект BookingDto
     */
    BookingDto get(Long bookingId, Long userId);

    /**
     * Метод получения списка BookingDto по userId и state
     *
     * @param userId ID пользователя
     * @param state  Статус
     * @return Список объектов BookingDto
     */
    List<BookingDto> getUserBookings(Long userId, String state);

    /**
     * Метод получения списка BookingDto по ownerId и state
     *
     * @param ownerId ID владельца
     * @param state   Статус
     * @return Список объектов BookingDto
     */
    List<BookingDto> getOwnerBookings(Long ownerId, String state);

    /**
     * Метод создания объекта Booking
     *
     * @param bookingDto Объект BookingDto
     * @param userId     ID пользователя
     * @return Созданный объект BookingDto
     */
    BookingDto create(ReceivedBookingDto bookingDto, Long userId);

    /**
     * Метод обновления статуса Booking по bookingId, approved, userId
     *
     * @param bookingId ID бронирования
     * @param approved  Обновляемый статус
     * @param userId    ID пользователя
     * @return Обновлённый объект BookingDto
     */
    BookingDto update(Long bookingId, String approved, Long userId);

}
