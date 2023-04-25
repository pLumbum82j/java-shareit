package ru.practicum.shareit.item.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.models.dto.BookingDateDto;
import ru.practicum.shareit.item.models.Comment;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;

import java.util.List;
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

    /**
     * Статический метод преобразования Item в ItemDto с Comments
     *
     * @param item     Объект Item
     * @param comments Список комментариев
     * @return Объект ItemDto
     */
    public static ItemDto toItemDto(Item item, List<Comment> comments, Booking next, Booking last) {
        ItemDto dto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .comments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
        if (last != null){
        dto.setLastBooking(new BookingDateDto(last.getId(),last.getBooker().getId()));
        }
        if (next != null){
            dto.setNextBooking(new BookingDateDto(next.getId(),next.getBooker().getId()));
        }
        return dto;
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