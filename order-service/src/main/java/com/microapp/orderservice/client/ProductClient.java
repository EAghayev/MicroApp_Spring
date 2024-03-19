package com.microapp.orderservice.client;

import com.microapp.orderservice.dto.ProductResponseDto;
import lombok.extern.java.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "productClient",url = "http://localhost:8080/products")
public interface ProductClient {

    @GetMapping("/all")
    ProductResponseDto[] getAll(@RequestParam List<Long> id);

    @GetMapping("")
    ProductResponseDto[] getAll();
}
