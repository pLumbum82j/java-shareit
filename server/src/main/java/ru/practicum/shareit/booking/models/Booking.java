package ru.practicum.shareit.booking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.user.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель объекта Booking
 */
@Getter
@Setter
@Builder
@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    private User booker;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.WAITING;
}