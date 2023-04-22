package ru.practicum.shareit.booking.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.exceptions.ObjectAccessDeniedException;
import ru.practicum.shareit.exceptions.ObjectAvailabilityDenyException;
import ru.practicum.shareit.exceptions.UnknownStatusException;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceDb implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceDb(BookingRepository bookingRepository,
                            @Qualifier("userServiceDb") UserService userService,
                            @Qualifier("itemServiceDb") ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingDto get(Long bookingId, Long userId) {
        userService.get(userId);
        Booking booking = bookingRepository.get(bookingId);
        if (Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            return BookingMapper.toBookingDto(booking);

        } else {
            throw new ObjectAccessDeniedException("Отказано в доступе");
        }
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        userService.get(userId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Booking> bookingList = null;
        BookingState bookingState = BookingState.from(state);
        if (bookingState == null) {
            throw new UnknownStatusException("Unknown state: " + state);
        }
        switch (bookingState) {
            case ALL:
                bookingList = bookingRepository.getAll(userId);
                break;
            case CURRENT:
                bookingList = bookingRepository.getAllCurrent(userId, currentDateTime);
                break;
            case PAST:
                bookingList = bookingRepository.getAllPast(userId, currentDateTime);
                break;
            case FUTURE:
                bookingList = bookingRepository.getAllFuture(userId, currentDateTime);
                break;
            case WAITING:
                bookingList = bookingRepository.getAllByStatus(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.getAllByStatus(userId, BookingStatus.REJECTED);
                break;
        }
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, String state) {
        userService.get(ownerId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Booking> bookingList = null;
        BookingState bookingState = BookingState.from(state);
        if (bookingState == null) {
            throw new UnknownStatusException("Unknown state: " + state);
        }
        switch (bookingState) {
            case ALL:
                bookingList = bookingRepository.getAllOwner(ownerId);
                break;
            case CURRENT:
                bookingList = bookingRepository.getAllCurrentOwner(ownerId, currentDateTime);
                break;
            case PAST:
                bookingList = bookingRepository.getAllPastOwner(ownerId, currentDateTime);
                break;
            case FUTURE:
                bookingList = bookingRepository.getAllFutureOwner(ownerId, currentDateTime);
                break;
            case WAITING:
                bookingList = bookingRepository.getAllByStatusOwner(ownerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.getAllByStatusOwner(ownerId, BookingStatus.REJECTED);
                break;
        }
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto create(ReceivedBookingDto bookingDto, Long userId) {
        User user = UserMapper.toUser(userService.get(userId));
        Booking booking = BookingMapper.toBooking(bookingDto);
        Item item = itemService.getItem(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new ObjectAvailabilityDenyException("Бронирование Item: " + item.getId() + " недоступно");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null
                || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isEqual(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ObjectAvailabilityDenyException("Ошибка во времени Start/End time");
        }
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new ObjectAccessDeniedException("Владелец не может забронировать свой предмет");
        }
        booking.setItem(item);
        booking.setBooker(user);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto update(Long bookingId, String approved, Long userId) {
        Booking booking = bookingRepository.get(bookingId);
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new ObjectAccessDeniedException("Пользователь с ID: " + userId + " не имеет доступа к Item");
        }
        if (Objects.equals(booking.getStatus(), BookingStatus.APPROVED)) {
            throw new ObjectAvailabilityDenyException("Статус бронирования уже установлен: " + booking.getStatus());
        }
        if (approved.equals("true")) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus((BookingStatus.REJECTED));
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }
}
