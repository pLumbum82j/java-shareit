package ru.practicum.shareit.booking.models.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.models.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ReceivedBookingDtoTest {
    @Autowired
    private JacksonTester<ReceivedBookingDto> json;

    @Test
    @SneakyThrows
    void testSerialize() {
        ReceivedBookingDto receivedBookingDto = new ReceivedBookingDto(1, LocalDateTime.of(2021, 5, 9, 16, 0,0), LocalDateTime.of(2021, 5, 9, 16, 0,0));

        JsonContent<ReceivedBookingDto> result = json.write(receivedBookingDto);
        assertThat(result).hasJsonPath("$.itemId");
        //assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(receivedBookingDto.getItemId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(receivedBookingDto.getStart());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(receivedBookingDto.getEnd());
    }
//    @Test
//    @SneakyThrows
//    void testDeserialize() {
//        String content = "{\"id\":\"1\",\"name\":\"User1\",\"email\":\"user1@yandex.ru\"}";
//        assertThat(this.json.parse(content))
//                .isEqualTo(new UserDto(1L, "User1","user1@yandex.ru"));
//        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
//        assertThat(this.json.parseObject(content).getName()).isEqualTo("User1");
//        assertThat(this.json.parseObject(content).getEmail()).isEqualTo("user1@yandex.ru");
//    }
}