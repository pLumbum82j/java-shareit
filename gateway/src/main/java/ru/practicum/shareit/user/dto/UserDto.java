package ru.practicum.shareit.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Модель объекта User Data Transfer Object
 */
@Getter
public class UserDto {
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Поле email должно содержать @")
    private String email;
}