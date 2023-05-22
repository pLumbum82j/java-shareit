package ru.practicum.shareit.booking.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;

/**
 * Класс BookingMapper для преобразования Booking в BookingDto и обратно
 */
@UtilityClass
public class BookingMapper {

    /**
     * Статический метод преобразования Booking в BookingDto
     *
     * @param booking Объект Booking
     * @return Объект BookingDto
     */
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    /**
     * Статический метод преобразования ReceivedBookingDto в Booking
     *
     * @param bookingDto Объект ReceivedBookingDto
     * @return Объект Booking
     */
    public static Booking toBooking(ReceivedBookingDto bookingDto) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(BookingStatus.WAITING)
                .build();
    }
}
