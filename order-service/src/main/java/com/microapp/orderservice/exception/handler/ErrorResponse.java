package com.microapp.orderservice.exception.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String title;
    private String message;
}
