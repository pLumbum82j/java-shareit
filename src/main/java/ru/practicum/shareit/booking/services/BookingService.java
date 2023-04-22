package ru.practicum.shareit.booking.services;

import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(Long bookingId, Long userId);
    List<BookingDto> getUserBookings(Long userId, String state);
    List<BookingDto> getOwnerBookings(Long ownerId, String state);
    BookingDto create(ReceivedBookingDto bookingDto, Long userId);
    BookingDto update(Long bookingId, String approved, Long userId);
}
