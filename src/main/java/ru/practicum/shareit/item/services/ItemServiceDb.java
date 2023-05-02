package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.exceptions.ObjectAvailabilityDenyException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.models.Comment;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.CommentDto;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс ItemServiceDb для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceDb implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> get(Long userId) {
        userService.get(userId);
        List<Item> itemList = itemRepository.findAllByOwnerIdOrderByIdAsc(userId);
        Map<Item, List<Booking>> allBookings = bookingRepository
                .findAllByItemInAndStatus(itemRepository.findAllByOwnerIdOrderByIdAsc(userId), BookingStatus.APPROVED)
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));
        List<ItemDto> resultItemList = new ArrayList<>();
        for (Item item : itemList) {
            ItemDto itemDto = ItemMapper.toItemDto(item, new ArrayList<>(),
                    allBookings.containsKey(item) ? allBookings.get(item)
                            .stream().filter(booking -> (booking.getStart().isAfter(LocalDateTime.now())))
                            .min(Comparator.comparing(Booking::getStart)).orElse(null) : null,
                    allBookings.containsKey(item) ? allBookings.get(item)
                            .stream().filter(booking -> (booking.getStart().isBefore(LocalDateTime.now())))
                            .min(Comparator.comparing(Booking::getStart)).orElse(null) : null);
            resultItemList.add(itemDto);
        }
        log.debug("Получен запрос на список ItemDto по userId {}", userId);
        return resultItemList;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto get(Long userId, Long itemId) {
        userService.get(userId);
        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(itemId);
        Item item = itemRepository.get(itemId);
        Booking lastForItem = null;
        Booking nextForItem = null;
        if (item.getOwner().getId().equals(userId)) {
            Map<Item, List<Booking>> allBookings = bookingRepository
                    .findAllByItemInAndStatus(itemRepository.findAllByOwnerIdOrderByIdAsc(userId), BookingStatus.APPROVED)
                    .stream()
                    .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));
            nextForItem = allBookings.containsKey(item) ? allBookings.get(item)
                    .stream().filter(booking -> (booking.getStart().isAfter(LocalDateTime.now())))
                    .min(Comparator.comparing(Booking::getStart)).orElse(null) : null;
            lastForItem = allBookings.containsKey(item) ? allBookings.get(item)
                    .stream().filter(booking -> (booking.getStart().isBefore(LocalDateTime.now())))
                    .max(Comparator.comparing(Booking::getStart)).orElse(null) : null;
        }
        log.debug("Получен запрос на ItemDto по itemId: {} и userId: {}", itemId, userId);
        return ItemMapper.toItemDto(item, comments, nextForItem, lastForItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItem(long itemId) {
        log.debug("Получен запрос на поиск Item по itemId: {}", itemId);
        return itemRepository.get(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(Long userId, String text) {
        UserMapper.toUser(userService.get(userId));
        if (text.isEmpty() || text.isBlank()) {
            log.debug("Получен запрос на список ItemDto по text: {} - ничего не найдено", text);
            return new ArrayList<>();
        } else {
            log.debug("Получен запрос на список ItemDto по text: {} - найдены совпадения по тексту", text);
            return itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                    .stream().map(ItemMapper::toItemDto).filter(ItemDto::getAvailable).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = UserMapper.toUser(userService.get(userId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        log.debug("Получен запрос на создание Item по userId: {}", userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto commentDto, long itemId, long userId) {
        User user = UserMapper.toUser(userService.get(userId));
        Item item = itemRepository.get(itemId);
        Comment comment = CommentMapper.toComment(commentDto);
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId,
                BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ObjectAvailabilityDenyException("Пользователю с идентификатором ID: " +
                    userId + " недоступно форматирование вещи ID: " + itemId);
        } else {
            comment.setAuthor(user);
            comment.setItem(item);
            commentRepository.save(comment);
        }
        log.debug("Получен запрос на создание Comment по userId: {}, itemId {}", userId, itemId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        userService.get(userId);
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
    @Transactional
    public void delete(Long itemId) {
        log.debug("Получен запрос на удаление Item по itemId: {}", itemId);
        itemRepository.deleteById(itemId);
    }
}
