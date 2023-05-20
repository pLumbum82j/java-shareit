package ru.practicum.shareit.item.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * Модель объекта Comment Data Transfer Object
 */
@Getter
public class CommentDto {
    @NotBlank(message = "Поле name не может быть пустым")
    private String text;
}