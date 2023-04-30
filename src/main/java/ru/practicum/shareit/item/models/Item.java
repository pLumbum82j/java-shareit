package ru.practicum.shareit.item.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Модель объекта Item
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
    @Column(name = "description")
    @NotBlank(message = "Поле description не может быть пустым")
    private String description;
    @Column(name = "is_available", nullable = false)
    @NotNull(message = "Поле available не может быть пустым")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column(name = "request_id")
    private Long request;
}
