package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Модель объекта User Data Transfer Object
 */
@Data
@Builder
public class UserDto {
    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Поле email должно содержать @")
    private final String email;
}