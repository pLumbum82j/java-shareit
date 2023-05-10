package ru.practicum.shareit.booking.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.OffsetPageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectAccessDeniedException;
import ru.practicum.shareit.exceptions.ObjectAvailabilityDenyException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.exceptions.UnknownStatusException;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceDbTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @InjectMocks
    BookingServiceDb bookingServiceDb;

    private UserDto userDto;
    private User user;

    private Booking booking;
    private BookingDto bookingDto;

    private Item item;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "User1", "User1@yandex.ru");
        userDto = UserMapper.toUserDto(user);
        item = new Item(1L, "item", "description", true, user, null);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.WAITING);
        bookingDto = BookingMapper.toBookingDto(booking);
    }

    @Test
    void getBooking_whenBookingIdAndUserIdFound_thenReturnedBooking() {
        when(userService.get(1L)).thenReturn(userDto);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingDto actualBooking = bookingServiceDb.get(booking.getId(), user.getId());

        assertEquals(actualBooking.getId(), bookingDto.getId());
        verify(bookingRepository, times(1)).findById(booking.getId());
    }

    @Test
    void getBooking_whenBookingIdFoundAndUserIdNotFound_thenReturnedException() {
        User wrongUser = new User(11L, "wrong", "wrong@ya.ru");
        booking.setBooker(wrongUser);
        item.setOwner(wrongUser);
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(user));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        ObjectAccessDeniedException objectAccessDeniedException = assertThrows(ObjectAccessDeniedException.class,
                () -> bookingServiceDb.get(booking.getId(), user.getId()));

        assertEquals(objectAccessDeniedException.getMessage(), "Отказано в доступе");
    }

    @Test
    void getUserBookings_whenUserFoundAndBookingStatusRejected_thenReturnedBookingList() {
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getUserBookings(user.getId(), "REJECTED", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatus(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenUserFoundAndBookingStatusWatting_thenReturnedBookingList() {
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getUserBookings(user.getId(), "WAITING", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatus(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenUserFoundAndBookingStatusFuture_thenReturnedBookingList() {
        when(bookingRepository.findAllByBookerIdAndStartIsAfter(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getUserBookings(user.getId(), "FUTURE", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStartIsAfter(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenUserFoundAndBookingStatusPast_thenReturnedBookingList() {
        when(bookingRepository.findAllByBookerIdAndEndIsBefore(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getUserBookings(user.getId(), "PAST", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndEndIsBefore(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenUserFoundAndBookingStatusCurrent_thenReturnedBookingList() {
        when(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getUserBookings(user.getId(), "CURRENT", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenUserFoundAndBookingStatusAll_thenReturnedBookingList() {
        OffsetPageRequest offsetPageRequest = new OffsetPageRequest(1, 1, Sort.by(Sort.Direction.DESC, "start"));
        when(bookingRepository.findAllByBookerId(user.getId(), offsetPageRequest)).thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getUserBookings(user.getId(), "ALL", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByBookerId(user.getId(), offsetPageRequest);
    }

    @Test
    void getUserBookings_whenOwnerFoundAndBookingStatusRejected_thenReturnedBookingList() {
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getOwnerBookings(user.getId(), "REJECTED", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByItemOwnerIdAndStatus(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenOwnerFoundAndBookingStatusWatting_thenReturnedBookingList() {
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getOwnerBookings(user.getId(), "WAITING", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByItemOwnerIdAndStatus(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenOwnerFoundAndBookingStatusFuture_thenReturnedBookingList() {
        when(bookingRepository.findAllByItemOwnerIdAndStartIsAfter(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getOwnerBookings(user.getId(), "FUTURE", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByItemOwnerIdAndStartIsAfter(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenOwnerFoundAndBookingStatusPast_thenReturnedBookingList() {
        when(bookingRepository.findAllByItemOwnerIdAndEndIsBefore(anyLong(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getOwnerBookings(user.getId(), "PAST", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByItemOwnerIdAndEndIsBefore(anyLong(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenOwnerFoundAndBookingStatusCurrent_thenReturnedBookingList() {
        when(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(), any(), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getOwnerBookings(user.getId(), "CURRENT", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(), any(), any(PageRequest.class));
    }

    @Test
    void getUserBookings_whenOwnerFoundAndBookingStatusAll_thenReturnedBookingList() {
        OffsetPageRequest offsetPageRequest = new OffsetPageRequest(1, 1, Sort.by(Sort.Direction.DESC, "start"));
        when(bookingRepository.findAllByItemOwnerId(user.getId(), offsetPageRequest)).thenReturn(List.of(booking));

        List<BookingDto> actualBooking = bookingServiceDb.getOwnerBookings(user.getId(), "ALL", 1, 1);

        assertEquals(actualBooking.get(0).getId(), booking.getId());
        verify(bookingRepository, times(1))
                .findAllByItemOwnerId(user.getId(), offsetPageRequest);
    }

    @Test
    void getUserBookings_whenNotValidStatusBooking_thenReturnedBookingList() {
       // OffsetPageRequest offsetPageRequest = new OffsetPageRequest(1, 1, Sort.by(Sort.Direction.DESC, "start"));
        //when(bookingRepository.findAllByItemOwnerId(user.getId(), offsetPageRequest)).thenReturn(List.of(booking));

        UnknownStatusException unknownStatusException  = assertThrows(UnknownStatusException.class,
                () -> bookingServiceDb.getOwnerBookings(user.getId(), "NOT_VALID", 1, 1));

        assertEquals(unknownStatusException.getMessage(), "Unknown state: NOT_VALID");
        verify(bookingRepository, never()).findAllByItemOwnerId(anyLong(), any(OffsetPageRequest.class));
    }

    @Test
    void createBooking_whenNewUserAndNewBookingValid_thenSavedBooking() {
        ReceivedBookingDto bookingDto = new ReceivedBookingDto(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        Booking newBooking = BookingMapper.toBooking(bookingDto);
        User newUser = new User(22L, "User22", "User2@yandex.ru");
        when(userService.get(any())).thenReturn(UserMapper.toUserDto(newUser));
        when(itemService.getItem(1L)).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(newBooking);

        BookingDto actualBooking = bookingServiceDb.create(bookingDto, newUser.getId());

        assertEquals(actualBooking.getItem().getId(), bookingDto.getItemId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_whenNewUserAndNewBookingTimeNotValid_thenNotSavedBooking() {
        ReceivedBookingDto bookingDto = new ReceivedBookingDto(1L, LocalDateTime.now(), LocalDateTime.now());
        User newUser = new User(22L, "User22", "User2@yandex.ru");
        when(userService.get(any())).thenReturn(UserMapper.toUserDto(newUser));
        when(itemService.getItem(1L)).thenReturn(item);

        ObjectAvailabilityDenyException objectAvailabilityDenyException = assertThrows(ObjectAvailabilityDenyException.class,
                () -> bookingServiceDb.create(bookingDto, newUser.getId()));

        assertEquals(objectAvailabilityDenyException.getMessage(), "Ошибка во времени Start/End time");
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_whenNewUserAndNewBookingAndItemNotValid_thenNotSavedBooking() {
        ReceivedBookingDto bookingDto = new ReceivedBookingDto(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        User newUser = new User(22L, "User22", "User2@yandex.ru");
        when(userService.get(any())).thenReturn(UserMapper.toUserDto(newUser));
        item.setAvailable(false);
        when(itemService.getItem(1L)).thenReturn(item);

        ObjectAvailabilityDenyException objectAvailabilityDenyException = assertThrows(ObjectAvailabilityDenyException.class,
                () -> bookingServiceDb.create(bookingDto, newUser.getId()));

        assertEquals(objectAvailabilityDenyException.getMessage(), "Бронирование Item: " + item.getId() + " недоступно");
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_whenNewUserNotValid_thenNotSavedBooking() {
        ReceivedBookingDto bookingDto = new ReceivedBookingDto(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        when(userService.get(any())).thenReturn(UserMapper.toUserDto(user));
        when(itemService.getItem(1L)).thenReturn(item);

        ObjectAccessDeniedException objectAccessDeniedException = assertThrows(ObjectAccessDeniedException.class,
                () -> bookingServiceDb.create(bookingDto, user.getId()));

        assertEquals(objectAccessDeniedException.getMessage(), "Владелец не может забронировать свой предмет");
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_whenApprovedTrueBookingIdAndUserFound_thenUpdateBookingStatus() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingServiceDb.update(booking.getId(), "true", user.getId());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_whenApprovedFalseBookingIdAndUserFound_thenUpdateBookingStatus() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingServiceDb.update(booking.getId(), "false", user.getId());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_whenApprovedStatusBookingIdAndUserFound_thenNotUpdateBookingStatus() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));
        booking.setStatus(BookingStatus.APPROVED);

        ObjectAvailabilityDenyException objectAvailabilityDenyException = assertThrows(ObjectAvailabilityDenyException.class,
                () -> bookingServiceDb.update(booking.getId(), "true", user.getId()));

        assertEquals(objectAvailabilityDenyException.getMessage(), "Статус бронирования уже установлен: APPROVED");
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_whenBookingNotFound_thenNotUpdateBookingStatus() {
        when(bookingRepository.findById(any())).thenReturn(Optional.empty());

        ObjectUnknownException objectUnknownException = assertThrows(ObjectUnknownException.class,
                () -> bookingServiceDb.update(booking.getId(), "true", user.getId()));

        assertEquals(objectUnknownException.getMessage(), "Бронирование с ID: " + booking.getId() + " не существует");
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_whenBookingFoundAndUserNotOwner_thenNotUpdateBookingStatus() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        ObjectAccessDeniedException objectAccessDeniedException = assertThrows(ObjectAccessDeniedException.class,
                () -> bookingServiceDb.update(booking.getId(), "true", 33L));

        assertEquals(objectAccessDeniedException.getMessage(), "Пользователь с ID: 33 не имеет доступа к Item");
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}