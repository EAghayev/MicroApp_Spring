package com.microapp.productservice.controller;

import com.microapp.productservice.dto.ProductCreateDto;
import com.microapp.productservice.dto.ProductGetDto;
import com.microapp.productservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<Object> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("all")
    public ResponseEntity<List<ProductGetDto>> getAll(@RequestParam List<Long> id){
        var data = productService.getAll(id);

        return ResponseEntity.ok(data);
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody @Valid ProductCreateDto createDto){
        productService.create(createDto);
        return  ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable @NotNull Long id, @RequestBody @Valid ProductCreateDto updateDto){
        productService.update(id,updateDto);
        return ResponseEntity.noContent().build();
    }
}
