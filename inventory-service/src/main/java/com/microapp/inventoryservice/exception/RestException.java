package com.microapp.inventoryservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class RestException extends RuntimeException {
    private HttpStatusCode code;
    private String message;
    private List<RestExceptionError> errors;

    public RestException(HttpStatusCode code, String errorItemKey, String errorItemMessage)
    {
        this.code = code;
        this.message = null;
        this.errors = Arrays.asList(new RestExceptionError(errorItemKey,errorItemMessage));
    }
    public RestException(HttpStatusCode code, String errorItemKey, String errorItemMessage, String message)
    {
        this.code = code;
        this.message = message;
        this.errors = Arrays.asList(new RestExceptionError(errorItemKey,errorItemMessage));
    }
    public RestException(HttpStatusCode code, List<RestExceptionError> errors, String message)
    {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
    public RestException(HttpStatusCode code, String message)
    {
        this.code = code;
        this.message = message;
        this.errors = new ArrayList<>();
    }
}
