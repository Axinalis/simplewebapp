package com.mastery.java.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNotFoundException(NotFoundException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage());
        if(exception.getMessage() == null){
            message.setMessage("Resource wasn't found");
        }
        message.setStatus(HttpStatus.NOT_FOUND.toString());
        return message;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleValidationException(MethodArgumentNotValidException exception){
        ExceptionMessage message = new ExceptionMessage("Some of the fields are not valid");
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.getDetails().put(fieldName, errorMessage);
        });
        message.setStatus(HttpStatus.BAD_REQUEST.toString());
        return message;
    }

}
