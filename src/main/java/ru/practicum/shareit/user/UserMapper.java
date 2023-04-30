package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;

/**
 * Класс UserMapper для преобразования User в UserDto и обратно
 */
@UtilityClass
public class UserMapper {
    /**
     * Статический метод преобразования User в UserDto
     *
     * @param user Объект User
     * @return Объект UserDto
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Статический метод преобразования UserDto в User
     *
     * @param userDto Объект UserDto
     * @return Объект User
     */
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
