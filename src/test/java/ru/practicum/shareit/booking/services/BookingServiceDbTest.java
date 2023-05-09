package ru.practicum.shareit.booking.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectAccessDeniedException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.checkerframework.checker.units.UnitsTools.m;
import static org.junit.jupiter.api.Assertions.*;
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
        when(userService.get(1L)).thenReturn(UserMapper.toUserDto(user));
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
    void getUserBookings() {
    }

    @Test
    void getOwnerBookings() {
    }

    @Test
    void createBooking_whenNewUserAndNewBooking_Valid_thenSavedBooking() {
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
    void update() {
    }
}