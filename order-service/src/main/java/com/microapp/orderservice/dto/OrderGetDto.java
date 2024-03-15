package com.microapp.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderGetDto {
    private Long id;
    private LocalDateTime createdAt;
    private List<OrderItemGetDto> orderItems;
}
