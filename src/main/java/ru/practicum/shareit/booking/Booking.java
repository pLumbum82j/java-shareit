package ru.practicum.shareit.booking;

import lombok.Data;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private Date start;
    private Date end;
    private Long item; // возможно поля типа STRING
    private Long booker; // возможно поля типа STRING
    private String status;

}
