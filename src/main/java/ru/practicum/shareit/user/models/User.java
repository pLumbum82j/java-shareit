package ru.practicum.shareit.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Модель объекта User
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Поле email должно содержать @")
    private String email;
}
