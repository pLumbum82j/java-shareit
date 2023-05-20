package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;

/**
 * Класс ErrorHandler обработчик ошибок
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {


//    @ExceptionHandler
//    public ResponseEntity<String> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
//        log.warn("Ошибка валидации.");
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
    @ExceptionHandler
    public ResponseEntity<String> handleObjectAvailabilityDeny(final ObjectAvailabilityDenyException e) {
        log.warn("Ошибка, объект недоступен.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = {UnknownStatusException.class})
//    public ErrorMessage handleUnknownStatus(Exception exception, WebRequest request) {
//        ErrorMessage error = new ErrorMessage(
//                new Date(),
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage()
//        );
//        log.warn("Ошибка запроса {}: {} {}",
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage(),
//                request.getDescription(false));
//        return error;
//    }

    @ExceptionHandler
    public ResponseEntity<String> handleObjectUnknown(final ObjectUnknownException e) {
        log.warn("Ошибка, Запрашиваемый объект не найден.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleObjectAccessDenied(final ObjectAccessDeniedException e) {
        log.warn("Ошибка, отказано в доступе.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleInternalServerError(final Throwable e) {
        log.warn(e.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage(), "stack", e.getStackTrace()));
    }
}