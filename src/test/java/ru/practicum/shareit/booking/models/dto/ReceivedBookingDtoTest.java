package ru.practicum.shareit.booking.models.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ReceivedBookingDtoTest {
    @Autowired
    private JacksonTester<ReceivedBookingDto> json;

    @Test
    @SneakyThrows
    void testSerialize() {
        ReceivedBookingDto receivedBookingDto = new ReceivedBookingDto(1L,
                LocalDateTime.of(2023, 5, 9, 16, 0, 1),
                LocalDateTime.of(2023, 5, 9, 16, 0, 1));

        JsonContent<ReceivedBookingDto> result = json.write(receivedBookingDto);
        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(receivedBookingDto.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(receivedBookingDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(receivedBookingDto.getEnd().toString());
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        String content = "{\"itemId\":\"1\",\"start\":\"2023-05-09T16:00:01\",\"end\":\"2023-05-09T16:00:01\"}";

        assertThat(this.json.parseObject(content).getItemId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getStart()).isEqualTo(LocalDateTime.of(2023, 5, 9, 16, 0, 1));
        assertThat(this.json.parseObject(content).getEnd()).isEqualTo(LocalDateTime.of(2023, 5, 9, 16, 0, 1));
    }
}