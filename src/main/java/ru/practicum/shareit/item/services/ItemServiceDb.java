package ru.practicum.shareit.item.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.exceptions.ObjectAvailabilityDenyException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.models.Comment;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.CommentDto;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j

public class ItemServiceDb implements ItemService {
    public ItemServiceDb(ItemRepository itemRepository, @Qualifier("userServiceDb") UserService userService,
                         BookingRepository bookingRepository,
                         CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public List<ItemDto> get(Long userId) {
        UserMapper.toUser(userService.get(userId));
        log.debug("Получен запрос на список ItemDto по userId: {}", userId);
        List<Item> listItems = itemRepository.findAllByOwnerIdOrderByIdAsc(userId);
        return listItems.stream().map(
                i -> {
                    List<Booking> lastForItem = List.of();
                    List<Booking> nextForItem = List.of();
                    if (i.getOwner().getId().equals(userId)) {
                        lastForItem = bookingRepository.getLastForItem(i.getId(), LocalDateTime.now());
                        nextForItem = bookingRepository.getNextForItem(i.getId(), LocalDateTime.now());
                    }
                    List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(i.getId());
                    return ItemMapper.toItemDto(i, comments, nextForItem.isEmpty() ? null : nextForItem.get(0), lastForItem.isEmpty() ? null : lastForItem.get(0));
                }
        ).collect(Collectors.toList());
    }

    @Override
    public ItemDto get(Long userId, Long itemId) {
        userService.get(userId);
        // List<ItemDto> itemDtoList = get(userId);
        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(itemId);
        log.debug("Получен запрос на ItemDto по itemId: {} и userId: {}", itemId, userId);
        Item item = itemRepository.get(itemId);
        List<Booking> lastForItem = List.of();
        List<Booking> nextForItem = List.of();
        ;
        if (item.getOwner().getId().equals(userId)) {
            lastForItem = bookingRepository.getLastForItem(itemId, LocalDateTime.now());
            nextForItem = bookingRepository.getNextForItem(itemId, LocalDateTime.now());
        }
        return ItemMapper.toItemDto(item, comments, nextForItem.isEmpty() ? null : nextForItem.get(0), lastForItem.isEmpty() ? null : lastForItem.get(0));
    }

    @Override
    public Item getItem(long itemId) {
        return itemRepository.get(itemId);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        UserMapper.toUser(userService.get(userId));
        if (text.isEmpty() || text.isBlank()) {
            log.debug("Получен запрос на список ItemDto по text: {} - ничего не найдено", text);
            return new ArrayList<>();
        } else {
            log.debug("Получен запрос на список ItemDto по text: {} - найдены совпадения по тексту", text);
            return itemRepository.search(text).stream().map(ItemMapper::toItemDto).filter(ItemDto::getAvailable).collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userService.get(userId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        log.debug("Получен запрос на создание Item по userId: {}", userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public CommentDto create(CommentDto commentDto, long itemId, long userId) {
        User user = UserMapper.toUser(userService.get(userId));
        Item item = itemRepository.get(itemId);
        Comment comment = CommentMapper.toComment(commentDto);
        if (bookingRepository.getFinishedCount(userId, itemId, LocalDateTime.now()) <= 0) {
            throw new ObjectAvailabilityDenyException("Пользователю с идентификатором ID: " + userId + " недоступно форматирование вещи ID: " + itemId);
        } else {
            comment.setAuthor(user);
            comment.setItem(item);
            commentRepository.save(comment);
        }
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        User user = UserMapper.toUser(userService.get(userId));
        Item itemOld = itemRepository.findById(itemId).get();
        if (!itemOld.getOwner().getId().equals(userId)) {
            throw new ObjectUnknownException("Пользователю с идентификатором ID: " + userId + " недоступно форматирование вещи ID: " + itemId);
        }
        Item itemTemp = ItemMapper.toItem(itemDto);
        if (itemTemp.getName() != null) {
            itemOld.setName(itemTemp.getName());
        }
        if (itemTemp.getDescription() != null) {
            itemOld.setDescription(itemTemp.getDescription());
        }
        if (itemTemp.getAvailable() != null) {
            itemOld.setAvailable(itemTemp.getAvailable());
        }
        log.debug("Получен запрос на изменение вещи с ID: {}", itemId);
        return ItemMapper.toItemDto(itemRepository.save(itemOld));
    }

    @Override
    public void delete(Long itemId) {
        log.debug("Получен запрос на удаление Item по itemId: {}", itemId);
        itemRepository.deleteById(itemId);
    }
}
