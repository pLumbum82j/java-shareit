package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerMockMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "User1", "User1@yandex.ru");
        userDto = UserMapper.toUserDto(user);
    }

    @Test
    @SneakyThrows
    //Есть вопросы как проверить список (кол-во юзеров в нём)
    void getAllUsers_thenReturnStatusOkAndListUsers() {
        when(userService.get()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())));

        verify(userService, times(1)).get();
    }

    @Test
    @SneakyThrows
    void getOnlyOneUser_thenReturnStatusOkAndUser() {
        when(userService.get(any())).thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).get(any());
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void getOnlyOneUser_whenUserNotFound_thenReturnBadRequest() {
//        when(userService.get(any())).thenThrow(ObjectNotFoundException.class);
//
//        mockMvc.perform(get("/users/{userId}", 3L))
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never()).get(3L);
    }

    @Test
    @SneakyThrows
    void createUser_whenInvoked_thenReturnStatusOkAndUser() {
        when(userService.create(any())).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).create(any());
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void createUser_whenUserNameNotValid_thenReturnBadRequest() {
        UserDto wrongUser = UserMapper.toUserDto(new User(1L, null, "User1@yandex.ru"));

        when(userService.create(any())).thenReturn(wrongUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(wrongUser);
    }

    @Test
    @SneakyThrows
    void createUser_whenUserEmailNotValid_thenReturnBadRequest() {
        UserDto wrongUser = UserMapper.toUserDto(new User(1L, "User1", "NotEmail"));

        when(userService.create(any())).thenReturn(wrongUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(wrongUser);
    }


    @Test
    @SneakyThrows
    void updateUser_whenUserIsUpdate_thenReturnStatusOkAndUpdateUser() {
        UserDto userDtoToUpdate = UserMapper.toUserDto(new User(2L, "User2", "User2@yandex.ru"));
        when(userService.update(userDtoToUpdate.getId(), userDtoToUpdate)).thenReturn(userDtoToUpdate);

        String result = mockMvc.perform(patch("/users/{userId}", userDtoToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).update(userDtoToUpdate.getId(), userDtoToUpdate);
        assertEquals(objectMapper.writeValueAsString(userDtoToUpdate), result);
    }

    @Test
    @SneakyThrows
    void deleteUser_thenReturnStatusOk() {
        mockMvc.perform(delete("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userDto.getId());
    }

}