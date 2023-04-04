package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Long id;
    @NotBlank (message = "Поле name не может быть пустым")
    private String name;
    @NotBlank (message = "Поле email не может быть пустым")
    @Email (message = "Поле email должно содержать @")
    private String email;
}
