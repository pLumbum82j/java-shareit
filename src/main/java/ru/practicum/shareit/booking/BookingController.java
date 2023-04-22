package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.booking.services.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable long bookingId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getOwnerBookings(ownerId, state);
    }

    @PostMapping()
    public BookingDto create(@RequestBody ReceivedBookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable long bookingId,
                             @RequestParam(value = "approved") String approved,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.update(bookingId, approved.toLowerCase(), userId);
    }
}
