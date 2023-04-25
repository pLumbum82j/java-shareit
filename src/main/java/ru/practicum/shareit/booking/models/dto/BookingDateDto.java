package ru.practicum.shareit.booking.models.dto;

import lombok.Data;

@Data
public  class BookingDateDto {
    Long id;
    Long bookerId;

    public BookingDateDto(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}