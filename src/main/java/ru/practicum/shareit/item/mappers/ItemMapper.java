package ru.practicum.shareit.item.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.models.Comment;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс ItemMapper для преобразования Item в ItemDto и обратно
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    /**
     * Статический метод преобразования Item в ItemDto
     *
     * @param item Объект Item
     * @return Объект ItemDto
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

    public static ItemDto toItemDto(Item item, List<Comment> comments) {
      return ItemDto.builder()
               .id(item.getId())
               .name(item.getName())
               .description(item.getDescription())
               .available(item.getAvailable())
               .request(item.getRequest() != null ? item.getRequest() : null)
               .comments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
               .build();
            //   .comments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
//        ItemDto result =  toItemDto(item);
//        return result.builder()
//                .comments(comments.size() == 0 ? null : comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
//                .build();
    }


    /**
     * Статический метод преобразования ItemDto в Item
     *
     * @param itemDto Объект ItemDto
     * @return Объект Item
     */
    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

}
