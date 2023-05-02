package ru.practicum.shareit.item.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Модель объекта Comment
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;
    @Column(name = "created")
    private LocalDateTime created;
}
