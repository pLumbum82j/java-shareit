package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ConfigConstant;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.booking.models.dto.BookingDto;
import ru.practicum.shareit.booking.models.dto.ReceivedBookingDto;
import ru.practicum.shareit.booking.services.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    Booking booking;
    BookingDto bookingDto;


    @BeforeEach
    void beforeEach() {
        booking = new Booking();
        bookingDto = BookingMapper.toBookingDto(booking);
    }


    @Test
    @SneakyThrows
    void getBooking_whenUserAndBookingFound_thenReturnedBooking() {
        when(bookingService.get(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConfigConstant.SHARER, 1L))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).get(anyLong(), anyLong());

    }

    @Test
    @SneakyThrows
    void getUserBookings() {
        when(bookingService.getUserBookings(anyLong(), anyString(), any(), any())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConfigConstant.SHARER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getUserBookings(anyLong(), anyString(), any(), any());
    }

    @Test
    @SneakyThrows
    void getOwnerBookings() {
        when(bookingService.getOwnerBookings(anyLong(), anyString(), any(), any())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConfigConstant.SHARER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getOwnerBookings(anyLong(), anyString(), any(), any());
    }

    @Test
    @SneakyThrows
    void create() {
        ReceivedBookingDto receivedBookingDto = new ReceivedBookingDto(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        when(bookingService.create(any(ReceivedBookingDto.class), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(ConfigConstant.SHARER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receivedBookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).create(any(ReceivedBookingDto.class), anyLong());
    }

    @Test
    @SneakyThrows
    void update() {
        when(bookingService.update(anyLong(), anyString(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "APPROVED")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(ConfigConstant.SHARER, 1L))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).update(anyLong(), anyString(), anyLong());

    }
}