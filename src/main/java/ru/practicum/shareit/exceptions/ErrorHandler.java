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

/**
 * Класс ErrorHandler обработчик ошибок
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleAlreadyExist(final ObjectAlreadyExistsException e) {
        log.warn("Ошибка, Запрашиваемый объект уже существует.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleObjectAvailabilityDeny(final ObjectAvailabilityDenyException e) {
        log.warn("Ошибка, объект недоступен.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //    @ExceptionHandler
//    public ResponseEntity<String> handleObjectAvailabilityDenyJSON(final ObjectAvailabilityDenyException e, MultiValueMap<String, String> headers) {
//        log.warn("Ошибка, объект недоступен.");
//        return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
//    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {UnknownStatusException.class})
    public ErrorMessage handleUnknownStatus(Exception exception, WebRequest request) {
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()
        );
        log.warn("Ошибка запроса {}: {} {}",
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getDescription(false));
        return error;
    }

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
    public ResponseEntity<String> handleInternalServerError(final Throwable e) {
        log.debug("500 {}", e.getMessage());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        e.printStackTrace(new PrintStream(out));
//        BufferedReader stack = new BufferedReader(e.printStackTrace(new PrintStream(out)));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}