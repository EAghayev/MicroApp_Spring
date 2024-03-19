package com.microapp.orderservice.controller;

import com.microapp.orderservice.client.ProductClient;
import com.microapp.orderservice.dto.OrderCreateDto;
import com.microapp.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;
    private final ProductClient productClient;

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody @Valid OrderCreateDto createDto){
        var dto = orderService.create(createDto);

        System.out.println(("testing"));

        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("products")
    public ResponseEntity<Object> getProducts(){
        return ResponseEntity.ok(productClient.getAll());
    }

    @GetMapping()
    public ResponseEntity<Object> getAll(){
        var data = orderService.getAll();
        return  ResponseEntity.ok(data);
    }

}
