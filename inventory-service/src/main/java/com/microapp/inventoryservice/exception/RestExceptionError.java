package com.microapp.inventoryservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RestExceptionError {
    private String field;
    private String message;
}
