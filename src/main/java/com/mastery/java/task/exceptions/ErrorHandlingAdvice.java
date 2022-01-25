package com.mastery.java.task.exceptions;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        ExceptionMessage message = new ExceptionMessage("Employee not found");
        message.getDetails().put("exception", exception.getMessage());
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

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleDatabaseException(PSQLException exception){
        ExceptionMessage message = new ExceptionMessage("Some error with database occurred");
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return message;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleRuntimeException(RuntimeException exception){
        ExceptionMessage message = new ExceptionMessage("Some internal error occurred");
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        //For now, until there is no loggers in project
        message.getDetails().put("Type of exception", exception.getClass().getName());
        return message;
    }
}
