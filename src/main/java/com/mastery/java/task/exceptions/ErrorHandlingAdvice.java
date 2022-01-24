package com.mastery.java.task.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ErrorHandlingAdvice {

    private static Logger log = LogManager.getLogger("com.mastery.java.task");

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        ExceptionMessage message = new ExceptionMessage("Employee not found");
        message.getDetails().put("exception", exception.getMessage());
        message.setStatus(HttpStatus.NOT_FOUND.toString());
        log.warn("Employee wasn't found with message: {}", exception.getStackTrace().toString());
        return message;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handlePathVariableException(MethodArgumentTypeMismatchException exception){
        ExceptionMessage message = new ExceptionMessage("Path variables of searched resource are not valid");
        message.setStatus(HttpStatus.BAD_REQUEST.toString());
        message.getDetails().put("exception", exception.getMessage());
        log.error("Error in parsing path variable: {}", exception.getStackTrace().toString());
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
        log.error("Some of received fields are not valid: {}", message.getDetails());
        return message;
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleDatabaseException(PSQLException exception){
        ExceptionMessage message = new ExceptionMessage("Some error with database occurred");
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        message.getDetails().put("exception", exception.getMessage());
        log.error("Error with database occurred: {}", exception.getStackTrace().toString());
        return message;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleRuntimeException(RuntimeException exception){
        ExceptionMessage message = new ExceptionMessage("Some internal error occurred");
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        message.getDetails().put("exception", exception.getMessage());
        log.error("Internal error occurred: {}", exception.getStackTrace().toString());
        return message;
    }

}
