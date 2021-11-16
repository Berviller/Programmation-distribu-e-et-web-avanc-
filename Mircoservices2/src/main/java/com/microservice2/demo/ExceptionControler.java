package com.microservice2.demo;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControler {
    @ResponseBody
    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userExistHandler(UserExistException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(PasswordExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String passwordExistHandler(PasswordExistException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MissingTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String passwordExistHandler(MissingTokenException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()){
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }

}
