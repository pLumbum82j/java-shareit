package ru.practicum.shareit.request;

import lombok.Data;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor; // Возможно поле типа STRING
    private Date created;
}
