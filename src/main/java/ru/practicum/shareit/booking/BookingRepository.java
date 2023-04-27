package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.exceptions.ObjectUnknownException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    default Booking get(long id) {
        return findById(id).orElseThrow(() -> new ObjectUnknownException("Бронирование с ID: " + id + " не существует"));
    }

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 ORDER BY b.start DESC")
    List<Booking> getAll(long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start <= ?2 AND b.end >= ?2 ORDER BY b.start DESC")
    List<Booking> getAllCurrent(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> getAllPast(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> getAllFuture(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> getAllByStatus(long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> getAllOwner(long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start <= ?2 AND b.end >= ?2 ORDER BY b.start DESC")
    List<Booking> getAllCurrentOwner(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> getAllPastOwner(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> getAllFutureOwner(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> getAllByStatusOwner(long bookerId, BookingStatus status);

    @Query(value = "SELECT * " +
            "FROM Bookings b " +
            "WHERE b.item_id = ?1 " +
            "AND b.start_date < ?2 " +
            "ORDER BY b.end_date DESC LIMIT 1 ", nativeQuery = true)
    List<Booking> getLastForItem(long itemId, LocalDateTime now);

    @Query(value = "SELECT * " +
            "FROM Bookings b " +
            "WHERE b.item_id = ?1 " +
            "AND b.start_date > ?2 " +
            "AND NOT b.status = 'REJECTED' " +
            "ORDER BY b.end_date ASC LIMIT 1", nativeQuery = true
    )
    List<Booking> getNextForItem(long itemId, LocalDateTime now);

    @Query(
            "SELECT COUNT (b) FROM Booking b " +
                    "WHERE b.booker.id = ?1 " +
                    "AND b.item.id = ?2 " +
                    "AND b.end < ?3 " +
                    "AND b.status = ru.practicum.shareit.booking.BookingStatus.APPROVED"
    )
    Integer getFinishedCount(long userId, long itemId, LocalDateTime now);


    List<Booking> getAllByItemId(long item);
}

