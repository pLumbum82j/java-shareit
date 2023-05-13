package ru.practicum.shareit.user.models.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    @SneakyThrows
    void testSerialize() {
        UserDto userDto = new UserDto(1L, "User1", "user1@yandex.ru");

        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        String content = "{\"id\":\"1\",\"name\":\"User1\",\"email\":\"user1@yandex.ru\"}";
        assertThat(this.json.parse(content))
                .isEqualTo(new UserDto(1L, "User1","user1@yandex.ru"));
        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("User1");
        assertThat(this.json.parseObject(content).getEmail()).isEqualTo("user1@yandex.ru");
    }


}