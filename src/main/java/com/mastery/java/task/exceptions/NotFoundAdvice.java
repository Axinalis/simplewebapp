package com.mastery.java.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleException(NotFoundException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage());
        message.setStatus(HttpStatus.NOT_FOUND);
        return message;
    }

}
