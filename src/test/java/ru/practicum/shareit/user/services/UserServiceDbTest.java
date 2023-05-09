package ru.practicum.shareit.user.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceDbTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceDb userServiceDb;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "User1", "User1@yandex.ru");
        userDto = UserMapper.toUserDto(user);
    }

    @Test
    void getAllUsers_whenUsersFound_thenReturnedUserList() {
        List<User> userList = List.of(user);
        List<UserDto> userDtoList = userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> actualUserDtoList = userServiceDb.get();

        assertEquals(userDtoList.size(), actualUserDtoList.size());
        assertEquals(userDtoList.get(0), actualUserDtoList.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_whenUserFound_thenReturnedUser() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

        UserDto actualUser = userServiceDb.get(userDto.getId());

        assertEquals(actualUser, userDto);
        verify(userRepository, times(1)).findById(userDto.getId());
    }

    @Test
    void getUserById_whenUserNotFound_thenObjectUnknownException() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        ObjectUnknownException objectUnknownException = assertThrows(ObjectUnknownException.class,
                () -> userServiceDb.get(userDto.getId()));
        assertEquals(objectUnknownException.getMessage(), "Пользователь с ID: " + userDto.getId() + " не существует");
    }

    @Test
    void createUser_whenUserValid_thenSavedUser() {
        when(userRepository.save(user)).thenReturn(user);

        UserDto actualUser = userServiceDb.create(userDto);

        assertEquals(actualUser, userDto);
        verify(userRepository).save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_whenUserFound_thenUpdateOnlyName() {
        User userToUpdate = new User(1L, "User2", "User1@yandex.ru");
        UserDto userDtoToUpdate = UserMapper.toUserDto(userToUpdate);


        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        UserDto actualUser = userServiceDb.update(userDto.getId(), userDtoToUpdate);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(actualUser.getName(), savedUser.getName());
        assertEquals(actualUser.getEmail(), savedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_whenUserFound_thenUpdateOnlyEmail() {
        User userToUpdate = new User(1L, "User1", "User2@yandex.ru");
        UserDto userDtoToUpdate = UserMapper.toUserDto(userToUpdate);


        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        UserDto actualUser = userServiceDb.update(userDto.getId(), userDtoToUpdate);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(actualUser.getName(), savedUser.getName());
        assertEquals(actualUser.getEmail(), savedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_whenUserFound_thenUserDeleted() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userServiceDb.delete(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void deleteUser_whenUserNotFound_thenUserNotDeleted() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        ObjectUnknownException objectUnknownException = assertThrows(ObjectUnknownException.class,
                () -> userServiceDb.delete(user.getId()));
        assertEquals(objectUnknownException.getMessage(), "Пользователь с ID: " + userDto.getId() + " не существует");
    }
}