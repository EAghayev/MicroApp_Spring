package com.microapp.orderservice.service;

import com.microapp.orderservice.client.InventoryClient;
import com.microapp.orderservice.client.ProductClient;
import com.microapp.orderservice.dto.*;
import com.microapp.orderservice.exception.RestException;
import com.microapp.orderservice.model.Order;
import com.microapp.orderservice.model.OrderItem;
import com.microapp.orderservice.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final WebClient webClient;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    public OrderGetDto create(OrderCreateDto createDto){
        Order entity = Order.builder()
                .createdAt(LocalDateTime.now())
                .orderNumber(UUID.randomUUID().toString()).build();



        List<Long> productIds = createDto.getOrderItems().stream().map(OrderItemCreateDto::getProductId).toList();

        log.info("testing");
        ProductResponseDto[] productArr = productClient.getAll(productIds);

        boolean allProductAreExists = productArr.length==createDto.getOrderItems().stream().count();

        if(!allProductAreExists){
            throw new RestException(HttpStatus.BAD_REQUEST,"Not all products are exist");
        }

        List<String> skuCodes = Arrays.stream(productArr).map(ProductResponseDto::getSkuCode).toList();

        InventoryStatusResponseDto[] inventoryStatusArr = inventoryClient.getAll(skuCodes);

        boolean allAreInStock = Arrays.stream(inventoryStatusArr).allMatch(InventoryStatusResponseDto::isInStock);


        if (inventoryStatusArr.length!=productArr.length || !allAreInStock){
            throw new RestException(HttpStatus.BAD_REQUEST,"Not all products is in stock!");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (var itemDto:createDto.getOrderItems()){
            ProductResponseDto pr = Arrays.stream(productArr).filter(x-> Objects.equals(x.getId(), itemDto.getProductId())).findFirst().orElseThrow();
            OrderItem item = OrderItem.builder()
                            .unitPrice(pr.getSalePrice())
                            .quantity(itemDto.getQuantity())
                            .productId(itemDto.getProductId())
                            .order(entity)
                    .build();
            orderItems.add(item);
        }

        entity.setOrderItems(orderItems);
        orderJpaRepository.save(entity);


        return mapToDto(entity);
    }

    public List<OrderGetDto> getAll(){
        var data = orderJpaRepository.findAll();

        var dto = data.stream().map(this::mapToDto).toList();
        return  dto;
    }


    private OrderItem mapToEntity(OrderItemCreateDto dto){
        return OrderItem.builder()
                .quantity(dto.getQuantity())
                .productId(dto.getProductId()).build();
    }
    private OrderGetDto mapToDto(Order entity){
        return OrderGetDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .orderItems(entity.getOrderItems().stream().map(this::mapToDto).toList()).build();
    }
    private OrderItemGetDto mapToDto(OrderItem entity){
        return OrderItemGetDto.builder()
                .id(entity.getId())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity()).build();
    }

}
