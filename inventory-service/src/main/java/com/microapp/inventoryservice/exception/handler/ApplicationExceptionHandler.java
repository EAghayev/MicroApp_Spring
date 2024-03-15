package com.microapp.inventoryservice.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.microapp.inventoryservice.exception.RestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(HttpMessageNotReadableException e)
    {
        Map<String, String> errors = new HashMap<>();
        String propertyName = null;

        if (e.getCause() instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) e.getCause();
            propertyName = jsonMappingException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("Unknown field");
        }

        errors.put(propertyName,"Format exception!");

        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException e)
    {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(ConstraintViolationException e)
    {
        Map<String, String> errors = new HashMap<>();
        var vaolationIterator = e.getConstraintViolations().iterator();

        while (vaolationIterator.hasNext()){
            ConstraintViolation<?> violation = vaolationIterator.next();
            String field = null;
            for (Path.Node node : violation.getPropertyPath()) {
                if(node.getKind() == ElementKind.PARAMETER){
                    field = node.getName();
                    errors.put(field,violation.getMessage());
                }
            }
        }

        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String,String>> handleRestException(RestException e){
        Map<String,String> errors = new HashMap<>();

        for (var errorItem : e.getErrors()) {
            errors.put(errorItem.getField(), errorItem.getMessage());
        }
        return new ResponseEntity<>(errors, new HttpHeaders(), e.getCode());
    }
}
