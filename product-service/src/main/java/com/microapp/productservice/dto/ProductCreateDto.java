package com.microapp.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ProductCreateDto {
    @NotBlank
    @Size(min = 3, max = 25)
    private String name;
    @Min(0)
    private BigDecimal salePrice;
    @Min(0)
    private BigDecimal costPrice;
    @Size(max = 500)
    private String desc;
    private int quantity;
}
