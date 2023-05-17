package ru.practicum.shareit.item.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
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
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceDbTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    ItemServiceDb itemServiceDb;

    private UserDto userDto;
    private User user;

    private Booking booking;

    private Item item;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "User1", "User1@yandex.ru");
        userDto = UserMapper.toUserDto(user);
        item = new Item(1L, "item", "description", true, user, null);
        booking = new Booking(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(25), item, user, BookingStatus.WAITING);
        comment = new Comment(1L, "text", item, user, LocalDateTime.now());
        commentDto = CommentMapper.toCommentDto(comment);
    }

    @Test
    void getItem_whenUserFound_thenReturnedItemList() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemInAndStatus(List.of(item), BookingStatus.APPROVED)).thenReturn(List.of(booking));

        List<ItemDto> actualItem = itemServiceDb.get(user.getId());

        assertEquals(actualItem.size(), 1);
        verify(itemRepository, times(2)).findAllByOwnerIdOrderByIdAsc(booking.getId());
    }

    @Test
    void getItem_whenUserAndItemFound_thenReturnedItem() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(bookingRepository.findAllByItemInAndStatus(List.of(item), BookingStatus.APPROVED)).thenReturn(List.of(booking));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong())).thenReturn(List.of(item));

        List<ItemDto> actualItem = itemServiceDb.get(user.getId());

        assertEquals(actualItem.size(), 1);
        verify(itemRepository, times(2)).findAllByOwnerIdOrderByIdAsc(booking.getId());
    }

    @Test
    void getItem_whenUserAndItemFoundAndValid_thenReturnedItem() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(commentRepository.findAllByItemIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(comment));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto actualItem = itemServiceDb.get(user.getId(), item.getId());

        assertEquals(actualItem.getId(), item.getId());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItem_whenItemFound_thenReturnedItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Item actualItem = itemServiceDb.getItem(anyLong());

        assertEquals(actualItem, item);
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItem_whenTextFound_thenReturnedItem() {
        when(itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(),
                anyString())).thenReturn(List.of(item));
        when(userService.get(anyLong())).thenReturn(userDto);

        List<ItemDto> actualItem = itemServiceDb.search(anyLong(), "text");

        assertEquals(actualItem.get(0).getId(), item.getId());
        verify(itemRepository, times(1))
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString());
    }

    @Test
    void getItem_whenTextIsEmpty_thenReturnedEmptyList() {
        when(userService.get(anyLong())).thenReturn(userDto);

        List<ItemDto> actualItem = itemServiceDb.search(anyLong(), " ");

        assertEquals(actualItem.size(), 0);
        verify(itemRepository, never()).findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString());
    }

    @Test
    void createItem_whenUserFoundAndItemValid_thenReturnedItem() {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemServiceDb.create(anyLong(), itemDto);

        assertEquals(actualItem.getId(), itemDto.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItem_whenUserFoundAndItemNotValid_thenNotSaved() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        item.setRequest(new ItemRequest(1L, "text", user, LocalDateTime.now()));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(userService.get(anyLong())).thenReturn(userDto);

        ObjectUnknownException objectUnknownException = assertThrows(ObjectUnknownException.class,
                () -> itemServiceDb.create(anyLong(), itemDto));

        assertEquals(objectUnknownException.getMessage(), "Запрос с ID: " + itemDto.getRequestId() + " не существует");
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void createComment_whenUserAndItemFoundAndCommentValid_thenNotSaved() {
        List<Booking> booking1 = List.of(booking);
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(any(), any(),
                any(), any())).thenReturn(booking1);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto actualCommet = itemServiceDb.create(commentDto, item.getId(), user.getId());

        assertEquals(actualCommet.getId(), comment.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_whenUserAndItemFoundAndCommentNotValid_thenNotSaved() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(any(), any(),
                any(), any())).thenReturn(List.of());

        ObjectAvailabilityDenyException objectAvailabilityDenyException = assertThrows(ObjectAvailabilityDenyException.class,
                () -> itemServiceDb.create(commentDto, item.getId(), user.getId()));

        assertEquals(objectAvailabilityDenyException.getMessage(), "Пользователю с идентификатором ID: " +
                user.getId() + " недоступно форматирование вещи ID: " + item.getId());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void updateItem() {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto actualItemDto = itemServiceDb.update(1L, 1L, itemDto);

        assertEquals(actualItemDto.getId(), itemDto.getId());
        verify(itemRepository, times(1)).save(item);

    }
}