package ru.practicum.shareit.request.models;

import lombok.*;
import ru.practicum.shareit.user.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Модель объекта ItemRequest
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @Column(name = "request_id")
    private int id;
    @NotBlank(message = "Поле name не может быть пустым")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;
    private LocalDateTime created;
}
