package com.microapp.productservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.math.BigDecimal;

@Entity
@Data
@Table(name="products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String skuCode;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String description;
}
