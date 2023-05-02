package ru.practicum.shareit.item.mappers;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.models.Comment;
import ru.practicum.shareit.item.models.dto.CommentDto;

import java.time.LocalDateTime;

/**
 * Класс CommentMapper для преобразования Comment в CommentDto и обратно
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    /**
     * Статический метод преобразования Comment в CommentDto
     *
     * @param comment Объект Comment
     * @return Объект CommentDto
     */
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    /**
     * Статический метод преобразования Comment в CommentDto
     *
     * @param commentDto Объект CommentDto
     * @return Объект Comment
     */
    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .created(commentDto.getCreated() == null ? LocalDateTime.now() : commentDto.getCreated())
                .build();
    }
}


