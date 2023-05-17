package ru.practicum.shareit.booking.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.util.OffsetPageRequest;
import ru.practicum.shareit.booking.BookingState;
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
import ru.practicum.shareit.user.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс BookingServiceDb для отработки логики запросов и логирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceDb implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional(readOnly = true)
    public BookingDto get(Long bookingId, Long userId) {
        userService.get(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectUnknownException("Бронирование с ID: " + bookingId + " не существует"));
        if (Objects.equals(booking.getBooker().getId(), userId) ||
                Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            log.debug("Получен запрос на поиск бронирования по bookingId: {}, userId: {}", bookingId, userId);
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new ObjectAccessDeniedException("Отказано в доступе");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookings(Long userId, String state, Integer from, Integer size) {
        BookingState bookingState = checkUserAndBookingState(userId, state);
        List<Booking> bookingList = null;
        OffsetPageRequest offsetPageRequest = new OffsetPageRequest(from, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (bookingState) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(userId, offsetPageRequest);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), offsetPageRequest);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), offsetPageRequest);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), offsetPageRequest);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING, offsetPageRequest);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED, offsetPageRequest);
                break;
        }
        log.debug("Получен запрос список бронирования по state: {}, userId: {}", state, userId);
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getOwnerBookings(Long ownerId, String state, Integer from, Integer size) {
        BookingState bookingState = checkUserAndBookingState(ownerId, state);
        List<Booking> bookingList = null;
        OffsetPageRequest offsetPageRequest = new OffsetPageRequest(from, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (bookingState) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerId(ownerId, offsetPageRequest);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId,
                        LocalDateTime.now(), LocalDateTime.now(), offsetPageRequest);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now(), offsetPageRequest);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now(), offsetPageRequest);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING, offsetPageRequest);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, offsetPageRequest);
                break;
        }
        log.debug("Получен запрос список бронирования по state: {}, ownerId: {}", state, ownerId);
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
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
        log.debug("Получен запрос создание бронирования userId: {}, itemId: {}", userId, item.getId());
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, String approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectUnknownException("Бронирование с ID: " + bookingId + " не существует"));
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
        log.debug("Получен запрос обновление бронирования bookingId: {}, approved {}, userId: {}",
                bookingId, approved, userId);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    /**
     * Метод проверки наличия пользователя в БД и статуса бронирования
     *
     * @param id    ID пользователя
     * @param state Статус
     * @return Проверенный статус
     */
    private BookingState checkUserAndBookingState(Long id, String state) {
        userService.get(id);
        BookingState bookingState = BookingState.from(state);
        if (bookingState == null) {
            throw new UnknownStatusException("Unknown state: " + state);
        }
        return bookingState;
    }
}
