package ru.practicum.shareit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Date;

@Getter
@AllArgsConstructor
@Value
public class ErrorMessage {
    Date timestamp;
    int statusCode;
    String error;
}