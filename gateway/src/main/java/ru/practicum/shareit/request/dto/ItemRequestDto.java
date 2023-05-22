package ru.practicum.shareit.request.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * Модель объекта ItemRequest Data Transfer Object
 */
@Getter
public class ItemRequestDto {
    @NotBlank(message = "Поле name не может быть пустым")
    private String description;
}